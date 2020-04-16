package com.chat.demo.entitys;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "mailcode")
public class mailcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codeid;
    private String email;
    private Timestamp endtime;
    private String code;
    private int checkstatus;
    public mailcode(){}
    public mailcode(int codeid,String email,Timestamp endtime,String code,int checkstatus){
        this.codeid=codeid;
        this.email=email;
        this.endtime=endtime;
        this.code=code;
        this.checkstatus=checkstatus;
    }

    public int getCodeid(){
        return  codeid;
    }
    public void setCodeid(int codeid){
        this.codeid=codeid;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }

    public Timestamp getEndtime(){
        return endtime;
    }
    public void setEndtime(Timestamp endtime){
        this.endtime=endtime;
    }

    public String getCode(){
        return code;
    }
    public void setCode(String code){
        this.code=code;
    }

    public int getCheckstatus(){
        return checkstatus;
    }
    public void setCheckstatus(int checkstatus){
        this.checkstatus=checkstatus;
    }

}
