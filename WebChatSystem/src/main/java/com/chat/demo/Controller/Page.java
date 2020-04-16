package com.chat.demo.Controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.chat.demo.Dao.userRepository;
import com.chat.demo.annotation.PassToken;
import com.chat.demo.entitys.user;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Api(description ="页面" )
@Controller
@RequestMapping(value = "/pages")
public class Page {
    @Autowired
    userRepository userRepository;

    @PassToken
    @ApiOperation(value = "index页面",notes = "主页")
    @GetMapping("/index")
    public String index(){
        return "html/index";
    }

    @PassToken
    @ApiOperation(value = "login页面",notes = "登陆页面")
    @GetMapping(value = "/login")
    public String login(){
        Logger logger=Logger.getLogger("/login");
        logger.info("login pages!");
        return "html/login";
    }

    @PassToken
    @ApiOperation(value = "regist页面",notes = "注册页面")
    @GetMapping(value = "/regist")
    public String regist(){
        return "html/regist";
    }

    @PassToken
    @ApiOperation(value = "finish页面",notes = "注册完成")
    @GetMapping(value = "/finish")
    public String finish(){
        return "html/finish";
    }

    @PassToken
    @ApiOperation(value = "forget页面",notes = "修改密码")
    @GetMapping(value = "/forget")
    public String forget(){
        return "html/forget";
    }

    @PassToken
    @GetMapping(value = "/repassword")
    public String repassword(){
        return "html/repassword";
    }

    @PassToken
    @GetMapping(value = "/achen/幸运大转盘")
    public String achen(){
        return "html/阿辰专属抽奖";
    }

    @PassToken
    @GetMapping(value = "/mestest/{Token}")
    public String mestest(@PathVariable("Token") String token,HttpServletResponse response) throws IOException {
        String email=JWT.decode(token).getAudience().get(0);
        user user=userRepository.findByEmail(email);
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            response.sendRedirect("Http://39.106.105.240:80/pages/login");
        }
        return "html/mestest";
    }

    @PassToken
    @GetMapping(value = "/groupmes/{Token}")
    public String gcmes(@PathVariable("Token") String token,HttpServletResponse response) throws IOException {
        String email=JWT.decode(token).getAudience().get(0);
        user user=userRepository.findByEmail(email);
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            response.sendRedirect("Http://39.106.105.240:80/pages/login");
        }
        return "html/groupMes";
    }


    @PassToken
    @ApiOperation(value = "list页面",notes = "列表页面")
    @GetMapping(value = "/list/{Token}")
    public String list(@PathVariable("Token") String token,HttpServletResponse response) throws IOException {
        String email=JWT.decode(token).getAudience().get(0);
        user user=userRepository.findByEmail(email);
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            response.sendRedirect("http://39.106.105.240:80/pages/login");
        }
        return "html/list";
    }

    @PassToken
    @ApiOperation(value = "personalindex页面",notes = "个人主页")
    @GetMapping(value = "/personalindex/{Token}")
    public String personalindex(@PathVariable("Token") String token,HttpServletResponse response) throws IOException {
        System.out.println(token);
        String email=JWT.decode(token).getAudience().get(0);
        user user=userRepository.findByEmail(email);
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            response.sendRedirect("Http://39.106.105.240:80/pages/login");
        }
        return "html/personalindex";
    }

    @PassToken
    @ApiOperation(value = "groupindex页面",notes = "群主页")
    @GetMapping(value = "/groupindex")
    public String groupindex(){
        return "html/groupindex";
    }






    @PassToken
    @ApiOperation(value = "AES算法测试",notes = "加密测试")
    @GetMapping("/aestest")
    public String getaestest(){
        return "html/aestest";
    }

    @GetMapping("/webtest")
    public String getmes(){
        return "html/mestest";
    }

    @PassToken
    @GetMapping("/search")
    public String search(){
        return "html/search";
    }

    @PassToken
    @GetMapping("/gc_info")
    public String gc_info(){
        return "html/gc_info";
    }

    @PassToken
    @GetMapping("/info/{token}")
    public String info(@PathVariable("token") String token,  HttpServletResponse response) throws IOException {
        String email=JWT.decode(token).getAudience().get(0);
        user user=userRepository.findByEmail(email);
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            response.sendRedirect("http://39.106.105.240:80/pages/login");
        }
        return "html/user_info";
    }

    @PassToken
    @GetMapping("/group/{token}")
    public String group(@PathVariable("token") String token,  HttpServletResponse response) throws IOException {
        String email=JWT.decode(token).getAudience().get(0);
        user user=userRepository.findByEmail(email);
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            response.sendRedirect("http://39.106.105.240:80/pages/login");
        }
        return "html/creatgroup";
    }

    @PassToken
    @GetMapping("/creategroupchat")
    public String create(){
        return "html/creatgroupchat";
    }



    @PassToken
    @GetMapping("/deletelist/{token}")
    public String deletelist(@PathVariable("token") String token,  HttpServletResponse response) throws IOException {
        String email=JWT.decode(token).getAudience().get(0);
        user user=userRepository.findByEmail(email);
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            response.sendRedirect("http://39.106.105.240:80/pages/login");
        }
        return "html/deletelist";
    }

    @PassToken
    @GetMapping("/deletegroupchat/{token}")
    public String deletegroupchat(@PathVariable("token") String token,  HttpServletResponse response) throws IOException {
        String email=JWT.decode(token).getAudience().get(0);
        user user=userRepository.findByEmail(email);
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            response.sendRedirect("http://39.106.105.240:80/pages/login");
        }
        return "html/deletegroupchat";
    }

    @PassToken
    @GetMapping("/getusers/{token}")
    public String getusers(@PathVariable("token") String token,  HttpServletResponse response) throws IOException {
        String email=JWT.decode(token).getAudience().get(0);
        user user=userRepository.findByEmail(email);
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            response.sendRedirect("http://39.106.105.240:80/pages/login");
        }
        return "html/team";
    }
}
