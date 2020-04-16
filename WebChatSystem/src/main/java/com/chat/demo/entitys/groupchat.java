package com.chat.demo.entitys;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "groupchat")
public class groupchat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupchatid;
//    群聊人数
    private int num;
//    群主id
    private int userid;
    private String gcname;
    private String describtion;
    private Timestamp creattime;
    private String img;

    public groupchat(){}
    public groupchat(int num,int userid,String describtion,String gcname,Timestamp creattime){
        this.num=num;
        this.userid=userid;
        this.describtion=describtion;
        this.gcname=gcname;
        this.creattime=creattime;
    }
    public int getGroupchatid(){
        return groupchatid;
    }

    public int getUserid(){
        return userid;
    }
    public void setUserid(int userid){
        this.userid=userid;
    }

    public int getNum(){
        return num;
    }
    public void setNum(int num){
        this.num=num;
    }

    public String getGcname(){
        return gcname;
    }
    public  void setGcname(String gcname){
        this.gcname=gcname;
    }

    public String getDescribtion(){
        return describtion;
    }
    public  void setDescribtion(String describtion){
        this.describtion=describtion;
    }

    public Timestamp getCreattime(){
        return creattime;
    }
    public void setCreattime(Timestamp creattime){
        this.creattime=creattime;
    }

    public String getImg(){
        return img;
    }
    public void setImg(String img){
        this.img=img;
    }
}
