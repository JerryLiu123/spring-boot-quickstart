package com.liu.springboot.quickstart.util;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import com.liu.springboot.quickstart.config.ConstantsConfig;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
        BASE64Encoder base64en = new BASE64Encoder();
        try {
            byte[] strByte = str.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptStrBytes = cipher.doFinal(strByte);
            return base64en.encode(encryptStrBytes);
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
        BASE64Decoder base64de = new BASE64Decoder();
        try {
            byte[] strbyte = base64de.decodeBuffer(str);
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
