package com.chat.demo.Controller;


import com.auth0.jwt.JWT;
import com.chat.demo.Dao.userRepository;
import com.chat.demo.Util.fileUtil;
import com.chat.demo.annotation.SecurityParameter;
import com.chat.demo.entitys.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class user_con {
    @Autowired
    com.chat.demo.Dao.userRepository userRepository;
    @SecurityParameter
    @PostMapping("/user/info")
    public com.chat.demo.entitys.user user_info(@RequestBody com.chat.demo.entitys.user newuser, HttpServletRequest request){
        String token=request.getHeader("Token");
        String email= JWT.decode(token).getAudience().get(0);
        com.chat.demo.entitys.user auser=userRepository.findByEmail(email);
        if(auser==null) return null;
        auser.setName(newuser.getName());
        auser.setPersonalname(newuser.getPersonalname());
        auser.setSex(newuser.getSex());
        userRepository.save(auser);
        return auser;
    }

    @PostMapping("/user/img")
    public String user_img(@RequestParam("photo") MultipartFile file, HttpServletRequest request){
        String fileName=file.getOriginalFilename();
        String suffixName=fileName.substring(fileName.lastIndexOf("."));
        if(!(suffixName.equals(".png")||suffixName.equals(".jpg")||suffixName.equals(".jpeg")||suffixName.equals(".gif"))) return null;
        String token=request.getHeader("Token");
        String email=JWT.decode(token).getAudience().get(0);
        fileName=email+suffixName;
        String Path="/usr/WebChatSystem/user_img/";
//        String Path="D:\\美图\\";
        fileUtil fileUtil=new fileUtil();
        fileUtil.upload_img(file,Path,fileName);
        user user=userRepository.findByEmail(email);
        user.setImg("http://39.106.105.240:80/file/image/"+fileName);
        userRepository.save(user);
        return user.getImg();
    }
}
