package com.chat.demo.Controller;

import com.chat.demo.annotation.PassToken;
import com.chat.demo.annotation.SecurityParameter;
import com.chat.demo.entitys.user;
import com.chat.demo.Util.AesEncrypt;
import com.chat.demo.Util.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Api(description = "登录")
@RestController
public class login {
    @Autowired
    com.chat.demo.Dao.userRepository userRepository;
    @Autowired AesEncrypt aesEncrypt;

    @ApiOperation(value = "Login",notes = "登录")
    @GetMapping(value = "/login")
    @PassToken
    @SecurityParameter(inDecode = false,outDecode = true)
    public Map login(@RequestParam String mail,
                        @RequestParam String password) throws Exception {
        Logger logger= Logger.getLogger("login");
        logger.info(mail+"尝试登陆！");
        logger.info(password);
        Map mes=new HashMap<String,String>();
        user user;
        user = userRepository.findByEmail(mail);
        String thispassword=aesEncrypt.decrypt(user.getPassword());
        String getpassword=aesEncrypt.decrypt(password);
        if(user==null){
            mes.put("message","账号不存在，请先注册!");
            return mes;
        }
        if(thispassword.equals(getpassword)){
            TokenService tokenService=new TokenService();
            String Token=tokenService.creatToken(user);
            mes.put("message","登陆成功！");
            mes.put("name",user.getName());
            mes.put("img",user.getImg());
            mes.put("Token",Token);
            mes.put("id",user.getUserid());
            System.out.println(Token);
            return mes;
        }
        else{
            mes.put("message","密码错误");
            return mes;
        }
    }



}
