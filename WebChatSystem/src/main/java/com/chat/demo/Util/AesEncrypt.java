package com.chat.demo.Util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class AesEncrypt {
    private static final String key="WebChatSystem202";

    private static final String ALGORITHMSTR="AES/ECB/PKCS5Padding";

    public static String encrypt(String content) throws Exception{
        Cipher cipher=Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(key.getBytes(),"AES"));
        byte[] Contentencrypt= cipher.doFinal(content.getBytes(UTF_8));
        return Base64.encodeBase64String(Contentencrypt);
    }


    public static String decrypt(String content)throws Exception{
        Cipher cipher=Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(key.getBytes(),"AES"));
        byte[] encryptcontent=Base64.decodeBase64(content);
        byte[] decrytcontent=cipher.doFinal(encryptcontent);
        System.out.println("原始数据："+content);
        System.out.println("解密后数据："+new String(decrytcontent));
        return new String(decrytcontent);
    }
}
