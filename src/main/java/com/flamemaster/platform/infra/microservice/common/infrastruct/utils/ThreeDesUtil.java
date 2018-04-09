package com.flamemaster.platform.infra.microservice.common.infrastruct.utils;

import net.iharder.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class ThreeDesUtil {
	private ThreeDesUtil(){
		
	}
	private static final ThreeDesUtil des = new ThreeDesUtil(); // 实例化一个对像  
	static{
		String KEY= "vera-creditservice";//秘钥
		try {
			des.getKey(KEY);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	private SecretKey key; // 密钥
	private static final String DES_ALGORITHM = "DES";

	/**
	 * 根据参数生成KEY
	 * 
	 * @param strKey
	 *            密钥字符串
	 * @throws NoSuchAlgorithmException
	 */
	public void getKey(String strKey) throws NoSuchAlgorithmException {
		
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
	    secureRandom.setSeed(strKey.getBytes());
        
        // 为我们选择的DES算法生成一个KeyGenerator对象  
        KeyGenerator kg = null;
        try {  
            kg = KeyGenerator.getInstance(DES_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
        }  
        kg.init(secureRandom);  
          
        // 生成密钥  
        this.key = kg.generateKey();  
	}

	/**
	 * 加密String明文输入,String密文输出
	 * 
	 * @param strMing
	 *            String明文
	 * @return String密文
	 */
	public String getEncString(String strMing) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";
		try {
			byteMing = strMing.getBytes("UTF8");
			byteMi = this.getEncCode(byteMing);
			strMi = Base64.encodeBytes(byteMi);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			byteMing = null;
			byteMi = null;
		}
		return strMi;
	}

	/**
	 * 解密 以String密文输入,String明文输出
	 * 
	 * @param strMi
	 *            String密文
	 * @return String明文
	 */
	public String getDesString(String strMi) {
		byte[] byteMing = null;
		byte[] byteMi = null;
		String strMing = "";
		try {
			byteMi = Base64.decode(strMi);
			byteMing = this.getDesCode(byteMi);
			strMing = new String(byteMing, "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			byteMing = null;
			byteMi = null;
		}
		return strMing;
	}

	/**
	 * 为getEncString方法提供服务
	 * 
	 * 加密以byte[]明文输入,byte[]密文输出
	 * 
	 * @param byteS
	 *            byte[]明文
	 * @return byte[]密文
	 */
	private byte[] getEncCode(byte[] byteS) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 为getDesString方法提供服务
	 * 
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * 
	 * @param byteD
	 *            byte[]密文
	 * @return byte[]明文
	 */
	private byte[] getDesCode(byte[] byteD) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	public static ThreeDesUtil getInstance(){
        return des;
	}
	
	public static void main(String[] args) throws Exception {
		String encryStr="d7e26f579ca0d2c0bd6df4f91fd38c88";//密码
		ThreeDesUtil des = ThreeDesUtil.getInstance(); // 实例化一个对像
        String strEnc = des.getEncString(encryStr);// 加密字符串,返回String的密文
        System.out.println(strEnc);
        String strDes = des.getDesString(strEnc);// 把String 类型的密文解密
        System.out.println(strDes);
	}
}
