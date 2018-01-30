package com.liu.springboot.quickstart.config.springconfig;

import org.jasypt.encryption.StringEncryptor;

import com.liu.springboot.quickstart.util.DESUtils;

public class DefaultEncryptor implements StringEncryptor {

    /**
     * 用于解密的方法,这里因为是测试所以自己写的，生产环境应为调用别的加密程序
     */
    @Override
    public String decrypt(String arg0) {
        // TODO Auto-generated method stub
        String key = DESUtils.getDecryptString(arg0);
        System.out.println("解密后报文为----"+key);
        return key;
    }

    /**
     * 用于加密的方法,这里因为是测试所以自己写的，生产环境应为调用别的解密程序
     */
    @Override
    public String encrypt(String arg0) {
        // TODO Auto-generated method stub
        System.out.println("加密前报文为----"+arg0);
        return DESUtils.getEncryptString(arg0);
    }

}
