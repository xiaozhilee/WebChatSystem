package com.chat.demo.Controller;

import com.chat.demo.Dao.*;
import com.chat.demo.Util.code;
import com.chat.demo.annotation.PassToken;
import com.chat.demo.annotation.SecurityParameter;
import com.chat.demo.entitys.group;
import com.chat.demo.entitys.mailcode;
import com.chat.demo.entitys.user;
import com.chat.demo.Util.AesEncrypt;
import com.chat.demo.Util.Mailservice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


@Api(description = "注册")
@RestController
public class regist {
    @Autowired
    codeRepository codeRepository;
    @Autowired
    Mailservice mailservice;
    @Autowired
    userRepository userRepository;
    @Autowired
    AesEncrypt aesEncrypt;
    @Autowired
    GroupRepository groupRepository;

    @PassToken
    @ApiOperation(value = "获取验证码")
    @GetMapping(value = "/code")
    public Map code(@RequestParam String email) throws Exception {
            code Code=new code();
            mailcode mailcode=codeRepository.findByEmail(email);
            if(mailcode==null) {
                mailcode smailcode=new mailcode();
                smailcode.setCheckstatus(0);
                String code=Code.GenerateCode();
                smailcode.setCode(code);
                smailcode.setEmail(email);
                Timestamp nowtime = new Timestamp(System.currentTimeMillis());
                smailcode.setEndtime(new Timestamp(nowtime.getTime() + 60 * 1000));
                codeRepository.save(smailcode);
                mailservice.sendmail(email, "您的验证码为" + code + "请在1分钟内验证。");
                Map<String, String> resmap = new HashMap<>();
                resmap.put("code", "200验证码已发送");
                return resmap;
            }
            String code = Code.GenerateCode();
            mailcode.setCheckstatus(0);
            mailcode.setCode(code);
            mailcode.setEmail(email);
            Timestamp nowtime = new Timestamp(System.currentTimeMillis());
            mailcode.setEndtime(new Timestamp(nowtime.getTime() + 60 * 1000));
            codeRepository.save(mailcode);
            mailservice.sendmail(email, "您的验证码为" + code + "请在1分钟内验证。");
            Map<String, String> resmap = new HashMap<>();
            resmap.put("code", "200验证码已发送");
            return resmap;
    }

    @ApiOperation(value = "check code",notes = "核对验证码")
    @PassToken
    @GetMapping(value = "/check")
    public Map check(@RequestParam String email,
                        @RequestParam String code){
        Logger logger=Logger.getLogger("check");
        logger.info(email+"check"+code);
        Map<String,String> resmap=new HashMap<>();
        Timestamp now=new Timestamp(System.currentTimeMillis());
        mailcode mailcode = codeRepository.findByEmail(email);
        if(mailcode.getCode()==null){
            resmap.put("mes","找不到验证码");
            return  resmap;
        }
        else if(mailcode.getCode().equals(code)&&now.before(mailcode.getEndtime())){
            mailcode.setCheckstatus(1);
            codeRepository.save(mailcode);
            resmap.put("mes","验证成功");
            return  resmap;
        }
        else {
            resmap.put("mes","验证码错误");
            return resmap;
        }
    }

    @PassToken
    @ApiOperation(value = "finish")
    @ResponseBody
    @SecurityParameter(inDecode = true,outDecode = true)
    @PostMapping(value = "/finish")
    public Map finish(@RequestBody user user
                      ) throws Exception {
        Map<String,String> resmap=new HashMap<>();
        user auser=userRepository.findByEmail(user.getEmail());
        if(auser!=null) {
            resmap.put("mes","该账户已存在");
            return resmap;
        }
        mailcode mailcode=codeRepository.findByEmail(user.getEmail());
        if(mailcode!=null&&mailcode.getCheckstatus()==1){
                mailcode.setCheckstatus(0);
                user suser=new user(user.getName(),user.getEmail(),aesEncrypt.encrypt(user.getPassword()),user.getPersonalname(),user.getSex(),"http://39.106.105.240:80/img/user.png");
                userRepository.save(suser);
                codeRepository.save(mailcode);
                int id=userRepository.findUseridByEmail(user.getEmail());
                groupRepository.save(new group(id,"好友"));
                resmap.put("mes","注册成功");
        }
        else{
            resmap.put("mes","请先获得验证码");
            return resmap;
        }
        return resmap;
    }

    @PassToken
    @ApiOperation(value = "repassword")
    @SecurityParameter
    @PatchMapping(value = "/repassword/{email}")
    public Map repassword(
            @PathVariable String email,
            @RequestBody user user
    ) throws Exception {

        Logger logger=Logger.getLogger("repassword");
        logger.info(email);
        logger.info(user.getPassword());
       Map<String,String> resmap=new HashMap<>();
       mailcode mailcode=codeRepository.findByEmail(email);
       user auser=userRepository.findByEmail(email);
        if(mailcode!=null&&user!=null&&mailcode.getCheckstatus() == 1) {
                    auser.setPassword(aesEncrypt.encrypt(user.getPassword()));
                    mailcode.setCheckstatus(0);
                    codeRepository.save(mailcode);
                    userRepository.save(auser);
                    resmap.put("mes","修改成功");
                    return resmap;
            }
        else{
            resmap.put("mes","请先获取修改资格");
            return resmap;
        }
    }

}
