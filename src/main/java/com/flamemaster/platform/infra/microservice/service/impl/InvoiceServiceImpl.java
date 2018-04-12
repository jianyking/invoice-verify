package com.flamemaster.platform.infra.microservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.flamemaster.platform.infra.microservice.base.*;
import com.flamemaster.platform.infra.microservice.config.InvoiceConfig;
import com.flamemaster.platform.infra.microservice.config.InvoiceConstants;
import com.flamemaster.platform.infra.microservice.service.CaptchaService;
import com.flamemaster.platform.infra.microservice.service.InvoiceService;
import com.flamemaster.platform.infra.microservice.common.infrastruct.utils.HttpUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;

@Log4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Resource
    private InvoiceConfig invoiceConfig;

    @Resource
    private CaptchaService captchaService;

    private static final String CAPTCHA_ERROR_MSG = "验证码错误！";

    private static final String CAPTCHA_EMPTY_MSG = "验证码不能为空！";

    private static final int maxCycle = 15;

    private static final String[] SPECIAL_CODE = new String[]
            {"144031539110", "131001570151", "133011501118", "111001571071"};

    /**
     * 查询发票信息
     *
     * @param request 查询参数
     * @return 发票信息
     */
    public InvoiceResponse queryInvoice(InvoiceRequest request) {

        if (StringUtils.isEmpty(request.getInvoiceCode())
                || StringUtils.isEmpty(request.getInvoiceNo())
                || StringUtils.isEmpty(request.getInvoiceDate())
                || request.getInvoiceCode().length() < 8) {
            return new InvoiceResponse(InvoiceConstants.PARAM_NOT_VALID, "参数不符合规范", null, null);
        }

        InvoiceData invoiceData = InvoiceCache.getInvoice(request);
        if (invoiceData != null) {
            log.info("命中发票缓存！");
            InvoiceResponse invoiceResponse = new InvoiceResponse();
            if (InvoiceCache.checkInvoice(request)) {
                invoiceResponse.setCode(0);
                invoiceResponse.setMsg("success");
                invoiceResponse.setData(invoiceData);
            } else {
                invoiceResponse.setCode(InvoiceConstants.INVOICE_ERROR);
                invoiceResponse.setMsg("未查到发票信息");
            }
            log.info("查询发票接口返回: " + JSON.toJSONString(invoiceResponse));
            return invoiceResponse;
        }

        InnerInvoiceRequest innerRequest = new InnerInvoiceRequest(
                getInvoiceType(request.getInvoiceCode()),
                request.getInvoiceCode(),
                request.getInvoiceNo(),
                request.getInvoiceDate(),
                request.getInvoiceAmount(),
                request.getCheckCode(),
                null
        );
        InvoiceResponse invoiceResponse = doQueryInvoice(innerRequest, 0);
        InvoiceCache.storageInvoice(invoiceResponse.getData());
        return invoiceResponse;
    }

    private InvoiceResponse doQueryInvoice(InnerInvoiceRequest request, int cycleLevel) {

        String checkUrl = invoiceConfig.getCheckUrl();
        Entity entity = captchaService.identifyCaptcha(20);
        if (entity.getCode() == InvoiceConstants.SUCCESS_STATUS) {
            request.setValidCode(entity.getData());
            String requestJson = JSON.toJSONString(request);

            log.info("调用查询接口 入参: " + requestJson);
            long beforeTime = System.currentTimeMillis();
            Entity resEntity;
            try {
                resEntity = HttpUtil.postByJson(requestJson, checkUrl);
            } catch (IOException e) {
                log.error("调用检查发票服务失败", e);
                //TODO recall
                return new InvoiceResponse(InvoiceConstants.NET_WORK_ERROR, "调用检查发票服务失败，请重试", null, null);
            }
            long callTime = System.currentTimeMillis() - beforeTime;
            log.info("调用接口用时: " + callTime + "ms");

            String responseStr = resEntity.getData();
            log.info("调用查询发表接口返回:" + responseStr);
            InvoiceResponse response = JSON.toJavaObject(JSON.parseObject(responseStr), InvoiceResponse.class);

            //调用失败处理
            if (response.getCode() == InvoiceConstants.FAIL_STATUS) {
                if (response.getMsg().equals(CAPTCHA_ERROR_MSG)
                        || response.getMsg().equals(CAPTCHA_EMPTY_MSG)) {
                    //验证码错误或为空，需要重新生成验证码
                    if (cycleLevel < maxCycle) {
                        return doQueryInvoice(request, cycleLevel + 1);
                    }
                } else {
                    //其余失败类型返回
                    response.setMsg("未查到发票信息");
                    response.setCode(InvoiceConstants.INVOICE_ERROR);
                    return response;
                }
            }
            //调用成功
            return response;
        } else {
            //验证码服务调用失败
            log.warn("无法获取有效的验证码");
            return new InvoiceResponse(InvoiceConstants.CAPTCHA_CODE_ERROR, "验证码服务调用失败", null, null);
        }
    }

    /**
     * 获取发票类型
     *
     * @param invoiceCode 发票
     * @return
     */
    private String getInvoiceType(String invoiceCode) {

        assert (invoiceCode != null && invoiceCode.length() >= 10);
        String b = invoiceCode.substring(7, 8);
        String c = "99";

        if (invoiceCode.length() == 12) {
            for (String special : SPECIAL_CODE) {
                if (invoiceCode.equals(special)) {
                    return "10";
                }
            }
            char c0 = invoiceCode.charAt(0);
            String d = invoiceCode.substring(10, 12);

            if (c0 == '0') {
                if ("11".equals(d)) {
                    return "10";//增值税普通发票（电子）
                }
                if ("06".equals(d) || "07".equals(d)) {  //判断是否为卷式发票  第1位为0且第11-12位为06或07
                    return "11";//增值税普通发票（卷式）
                }
                if ("04".equals(d) || "05".equals(d)) {  //第11-12位代表票种和联次，其中04代表二联增值税普通发票（折叠票）、05代表五联增值税普通发票（折叠票）
                    return "04";//2018年1月1日执行的增值税普通发票
                }
            } else if ("2".equals(b)) { //如果第8位是2，则是机动车发票
                return "03";//机动车发票
            }
        } else if (invoiceCode.length() == 10) {
            switch (b) {
                case "1":
                case "5":
                    return "01";
                case "3":
                case "6":
                    return "04";
                case "2":
                case "7":
                    return "02";
                default:
                    return "01";
            }
        }
        return c;
    }
}
