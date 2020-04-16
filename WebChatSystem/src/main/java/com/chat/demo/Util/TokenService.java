package com.chat.demo.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.chat.demo.entitys.user;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class TokenService {

    public String creatToken(user user){
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 1000*24*7;//7天有效时间
        Date end = new Date(currentTime);
        String Token="";
        Token= JWT.create().withAudience(user.getEmail()).withIssuedAt(start).withExpiresAt(end).sign(Algorithm.HMAC256(user.getPassword()));
        return Token;
    }
}
