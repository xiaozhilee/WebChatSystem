package com.chat.demo.Advice;

import com.chat.demo.Util.AesEncrypt;
import com.chat.demo.annotation.SecurityParameter;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

@ControllerAdvice
public class DecodeRequestBodyAdvice implements RequestBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        boolean encode=false;
        if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
            //获取注解配置的包含和去除字段
            SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
            //入参是否需要解密
            encode = serializedField.inDecode();
            System.out.println("入参解密："+encode);
        }

        HttpHeaders headers;
        InputStream body;
        SecurityParameter securityParameter=methodParameter.getMethodAnnotation(SecurityParameter.class);
        encode=securityParameter.inDecode();
        if(encode) {
            try {
                return new MyHttpInputMessage(httpInputMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    class MyHttpInputMessage implements HttpInputMessage{
        private HttpHeaders headers;
        private InputStream body;
        public MyHttpInputMessage(HttpInputMessage httpInputMessage)throws Exception{
            this.headers=httpInputMessage.getHeaders();
            AesEncrypt aesEncrypt=new AesEncrypt();
            this.body= IOUtils.toInputStream(aesEncrypt.decrypt(IOUtils.toString(httpInputMessage.getBody(), "UTF-8")));
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}

