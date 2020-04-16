package com.chat.demo.entitys;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageid;
    private String fromuseremail;
    private String touseremail;
    private int touserid;
    //0为私聊1为群聊
    private int mestype;
    private String mestext;
    private Timestamp mestime;

    public message(){}
    public message(String fromuseremail,int touserid,String mestext,Timestamp mestime,int mestype,String touseremail){
        this.fromuseremail=fromuseremail;
        this.touserid=touserid;
        this.mestext=mestext;
        this.mestime=mestime;
        this.mestype=mestype;
        this.touseremail=touseremail;
    }

    public int getMessageid(){
        return messageid;
    }
    public void setMessageid(int messageid){
        this.messageid=messageid;
    }

    public String getFromuseremail(){
        return fromuseremail;
    }
    public void setFromuseremail(String fromuseremail){
        this.fromuseremail=fromuseremail;
    }

    public int getTouserid(){
        return touserid;
    }
    public void setTouserid(int touserid) {
        this.touserid=touserid;
    }

    public int getMestype(){
        return mestype;
    }
    public void setMestype(int mestype){
        this.mestype=mestype;
    }

    public String getMestext(){
        return mestext;
    }
    public void setMestext(String mestext){
        this.mestext=mestext;
    }

    public Timestamp getMestime(){
        return mestime;
    }
    public void setMestime(Timestamp mestime){
        this.mestime=mestime;
    }

    public String getTouseremail(){
        return touseremail;
    }
    public void setTouseremail(String touseremail){
        this.touseremail=touseremail;
    }
}
