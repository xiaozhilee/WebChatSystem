package com.chat.demo.entitys;

import javax.persistence.*;

@Entity
@Table(name = "addgc")
public class addgc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addid;
    private int gcid;
    private int userid;
//    0为群主1为管理员2为群友
    private int role;
    public addgc(){}
    public addgc(int gcid, int userid,int role){
        this.gcid=gcid;
        this.userid=userid;
        this.role=role;
    }

    public int getAddid(){
        return addid;
    }
    public void setAddid(int addid){
        this.addid=addid;
    }

    public int getGcid(){
        return gcid;
    }
    public void setGcid(int gcid){
        this.gcid=gcid;
    }

    public int getUserid(){
        return userid;
    }
    public void setUserid(int userid){
        this.userid=userid;
    }

    public int getRole(){
        return role;
    }
    public void setRole(int role){
        this.role=role;
    }
}
