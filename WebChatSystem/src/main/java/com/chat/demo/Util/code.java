package com.chat.demo.Util;


import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class code {
    public String GenerateCode(){
        String Code="";
        Random random=new Random();
        int index;
        char[] character=new char[]{'a','b','c','d','e' ,'f','g','h','i','j','k' ,'m','n','p','q','r','s' ,'t','u','v','w','x','y' ,'z',
                'A','B','C','D','E' ,'F','G','H','I','J','K' ,'M','N','P','Q','R','S' ,'T','U','V','W','X','Y' ,'Z','2','3','4','5','6','7','8','9'};
        for(int i=0;i<6;i++){
            index=random.nextInt(character.length);
            Code+=character[index];
        }
        return Code;
    }

}
