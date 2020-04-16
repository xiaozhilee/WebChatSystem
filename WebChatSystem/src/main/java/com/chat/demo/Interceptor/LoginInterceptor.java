package com.chat.demo.Interceptor;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.chat.demo.Dao.userRepository;
import com.chat.demo.Util.AesEncrypt;
import com.chat.demo.annotation.PassToken;
import com.chat.demo.entitys.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.logging.Logger;

@Component
public class LoginInterceptor implements HandlerInterceptor  {
    private Logger logger=Logger.getLogger("拦截器");
    @Autowired
    private userRepository userRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String Token=request.getHeader("Token");
        if(!(handler instanceof HandlerMethod) ) return true;
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) return true;
        else if(Token==null) {
            logger.info("URL:"+request.getRequestURL()+"被拦截");
            logger.info("未带Token,重定向到login.html");
            response.sendRedirect("Http://39.106.105.240:80/pages/login");
            return false;
        }
        String email=null;
        try {
           email = JWT.decode(Token).getAudience().get(0);
        }catch (JWTDecodeException e){
            logger.info("获取用户信息失败，请重新登陆！");
            return false;
        }
        user auser=userRepository.findByEmail(email);
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(auser.getPassword())).build();
        try{
            jwtVerifier.verify(Token);
        }catch (JWTVerificationException e){
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
