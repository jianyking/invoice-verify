package com.flamemaster.platform.infra.microservice.base;

import com.flamemaster.platform.infra.microservice.config.InvoiceConstants;

import java.util.concurrent.ConcurrentHashMap;

public class InvoiceCache {
    private static ConcurrentHashMap<String, InvoiceData> INVOICE_CACHE = new ConcurrentHashMap<>();

    public static void storageInvoice(InvoiceData invoiceData) {
        if (invoiceData != null && InvoiceConstants.CHECK_INVOICE_CODE_OK.equals(invoiceData.getResultCode())) {
            INVOICE_CACHE.put(invoiceData.getInvoiceCode() + invoiceData.getInvoiceNo(), invoiceData);
        }
    }

    public static InvoiceData getInvoice(InvoiceRequest invoiceRequest) {
        return INVOICE_CACHE.get(invoiceRequest.getInvoiceCode() + invoiceRequest.getInvoiceNo());
    }

    public static boolean checkInvoice(InvoiceRequest invoiceRequest) {
        InvoiceData invoiceData = getInvoice(invoiceRequest);
        if (invoiceData == null) {
            return false;
        }
        return invoiceRequest.getInvoiceDate().replaceAll("-", "").equals(invoiceData.getInvoiceDate()) &&
                (invoiceRequest.getInvoiceAmount() != null && invoiceRequest.getInvoiceAmount().equals(invoiceData.getInvoiceAmount()) ||
                        invoiceRequest.getCheckCode() != null && invoiceData.getCheckCode().contains(invoiceRequest.getCheckCode()));
    }
}
