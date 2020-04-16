package com.chat.demo.test;

import com.chat.demo.Dao.messageRepository;
import com.chat.demo.Dao.userRepository;
import com.chat.demo.Util.code;
import com.chat.demo.annotation.PassToken;
import com.chat.demo.annotation.SecurityParameter;
import com.chat.demo.entitys.message;
import com.chat.demo.entitys.user;
import com.chat.demo.Util.AesEncrypt;
import com.chat.demo.Util.Mailservice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.logging.Logger;


@Api(value = "测试类",description = "测试")
@RestController
@RequestMapping("/test")
public class hello {
    @Autowired
    userRepository userRepository;

    @GetMapping(value = "/HelloWorld")
    public String Helloworld(@RequestParam String world){
        return world;
    }
    @PassToken
    @GetMapping(value = "/json")
    public user Helloworld(@RequestBody user user){
        return user;
    }

    @PassToken
    @ResponseBody
    @GetMapping(value="/get")
    public user get(){
       user user=new user();
       user.setEmail("123456");
       return user;
    }


    @PassToken
    @ApiOperation(value = "加密测试",notes = "加密测试")
    @GetMapping("/encrypt")
    @ApiImplicitParam(name = "text",value = "text")
    public String encrypt(@RequestParam String text) throws Exception {
        System.out.println(text);
        AesEncrypt aes=new AesEncrypt();
        return aes.encrypt(text);
    }


    @PassToken
    @GetMapping("/decrypt")
    @SecurityParameter(inDecode = false ,outDecode = false)
    @ApiOperation(value = "解密测试",notes = "解密测试")
    @ApiImplicitParam(name = "text",value = "text")
    public String decrypt(@RequestParam String text) throws Exception {
        AesEncrypt aes=new AesEncrypt();
        return aes.decrypt(text);
    }

    @PassToken
    @SecurityParameter(inDecode = true,outDecode = false)
    @ApiOperation(value = "入参",notes = "入参解密出参不加密")
    @ApiImplicitParam(value = "user")
    @PostMapping("/crypt1")
    public  user test1(@RequestBody user user)throws IOException {
        return user;
    }

    @PassToken
    @ResponseBody
    @SecurityParameter(inDecode = false,outDecode = true )
    @ApiOperation(value = "出参",notes = "出参加密")
    @ApiImplicitParam(value = "user")
    @PostMapping("/crypt2")
    public  user test2(@RequestBody user user)throws IOException {
        return user;
    }

    @PassToken
    @ResponseBody
    @SecurityParameter(inDecode = true)
    @ApiOperation(value = "入参出参均加密")
    @PostMapping("/crypt3")
    public  user test3(@RequestBody user user)throws IOException {
        user.setEmail("456");
        return user;
    }

    @PassToken
    @SecurityParameter(inDecode = true,outDecode = true)
    @ResponseBody
    @GetMapping("/getuser")
    public user getuser(){
        return new user();
    }


    @Autowired
    private Mailservice mailservice;
    @PassToken
    @GetMapping("/mailtest")
    @ApiOperation(value = "邮箱测试")
    public String mail(@PathParam(value = "email") String email,
                       @PathParam(value = "text") String text
                       ) throws Exception {
        Logger logger= Logger.getLogger("hello");
        logger.info("进入邮箱测试");
        System.out.println(email+"：发送");
        try {
            mailservice.sendmail(email, text);
            return "发送成功！";
        }catch (MailException e){
            throw new RuntimeException("mail发送失败");
        }

    }

    @PassToken
    @GetMapping("/Code")
    public String genCode(){
        code code=new code();
        return code.GenerateCode();
    }
    @Autowired
    messageRepository messageRepository;
    @PassToken
    @GetMapping("/save")
    public String savemes(){
        message message=new message("2491061139",123,"asd", null,1,"asdasd");
        messageRepository.save(message);
        return "OK";
    }


    @GetMapping("/userRetest")
    public String tet(){
        user user=userRepository.findByEmail("2491061139@qq.com");
        return user.getPassword();
    }
}
