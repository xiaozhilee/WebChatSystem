package com.chat.demo.entitys;


import javax.persistence.*;

@Entity
@Table(name = "groups")
public class group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupid;
    private int userid;
    private String groupname;

    public group(){}
    public group(int userid, String groupname){
        this.userid=userid;
        this.groupname=groupname;
    }

    public int getGroupid(){
        return groupid;
    }
    public void setGroupid(int groupid){
        this.groupid=groupid;
    }

    public int getUserid(){
        return userid;
    }
    public void setUserid(int userid){
        this.userid=userid;
    }

    public String getGroupname() {
        return groupname;
    }
    public void setGroupname(String groupname){
        this.groupname=groupname;
    }
}
