package com.chat.demo.entitys;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "friends")
public class friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int friendsid;
    private int userid;
    private int myid;
    private int groupid;
    private String name2;
    private Timestamp addtime;

    public friends(){}
    public friends(int userid,int myid,int groupid,String name2,Timestamp addtime){
        this.userid=userid;
        this.myid=myid;
        this.groupid=groupid;
        this.name2=name2;
        this.addtime=addtime;
    }

    public int getFriendsid(){
        return friendsid;
    }
    public void setFriendsid(int friendsid){
        this.friendsid=friendsid;
    }

    public int getMyid(){
        return myid;
    }
    public void setMyid(int myid){
        this.myid=myid;
    }

    public int getUserid(){
        return userid;
    }
    public void setUserid(int userid){
        this.userid=userid;
    }

    public int getGroupid(){
        return groupid;
    }
    public void setGroupid(int getGroupid){
        this.groupid=getGroupid;
    }

    public String getName2(){
        return name2;
    }
    public void setName2(String name2){
        this.name2=name2;
    }

    public Timestamp getAddtime(){
        return addtime;
    }
    public void setAddtime(Timestamp addtime){
        this.addtime=addtime;
    }


}
