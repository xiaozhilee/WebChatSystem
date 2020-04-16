package com.chat.demo.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class Mailservice {
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String Sendermail;

    public void sendmail(String sendto,String message)throws Exception{
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setSubject("WebChatSystem:");
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom(Sendermail);
        simpleMailMessage.setTo(sendto);
        mailSender.send(simpleMailMessage);
    }
}
