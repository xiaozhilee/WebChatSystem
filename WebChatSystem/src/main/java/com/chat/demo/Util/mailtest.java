package com.chat.demo.Util;

import com.chat.demo.Dao.userRepository;
import com.chat.demo.entitys.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class mailtest {

     private static Mailservice mailservice;
     private static com.chat.demo.Dao.userRepository userRepository;
    @Autowired
    public void setau(Mailservice mailservice,userRepository userRepository){
        mailtest.mailservice =mailservice;
        mailtest.userRepository =userRepository;
    }
    @Scheduled(cron = "0 0 8 ? * *")
    public void send() throws Exception {
        List<user> users=userRepository.findAll();
        for(int i=0;i<users.size();i++){
            if(users.get(i).getEmail()!=null){
                mailservice.sendmail(users.get(i).getEmail(),"他媽的，別玩了，該學會習了呀。");
            }
        }
    }

}
