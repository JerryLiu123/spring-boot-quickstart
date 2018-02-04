package com.liu.springboot.quickstart.util;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import com.liu.springboot.quickstart.config.ConstantsConfig;


public class DESUtils {

    private static Key key;
    static {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("DES");
            generator.init(new SecureRandom(ConstantsConfig.desKey.getBytes()));
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
    /**
     * 对字符串进行DES加密
     * @author lgh
     * @param str
     * @return
     */
    public static String getEncryptString(String str) {
    	Encoder encoder = Base64.getEncoder();
    	//在jdk1.9中已经废除
        //BASE64Encoder base64en = new BASE64Encoder();
        try {
            byte[] strByte = str.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptStrBytes = cipher.doFinal(strByte);
            return encoder.encodeToString(encryptStrBytes);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }
    }
    /**
     * 对字符串 进行DES解密
     * @param str
     * @return
     */
    public static String getDecryptString(String str) {
    	Decoder decoder = Base64.getDecoder();
    	//在jdk1.9中已经废除
        //BASE64Decoder base64de = new BASE64Decoder();
        try {
            byte[] strbyte = decoder.decode(str);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptStrBytes = cipher.doFinal(strbyte);
            return new String(decryptStrBytes, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }
    }
//    public static void main(String[] args) {
//        System.out.println(DESUtils.getEncryptString("123456"));
//        System.out.println(DESUtils.getDecryptString("xfcqz2HNCeY="));
//    }
}
