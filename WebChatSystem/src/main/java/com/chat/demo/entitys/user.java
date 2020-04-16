package com.chat.demo.entitys;


import javax.persistence.*;

@Entity
@Table(name ="user")
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userid;
    private String name;
    private String email;
    private String password;
    private String personalname;
//    0为男1为女
    private int sex;
    private String img;
    public user(){}

    public user(String name,String email,String password,String personalname,int sex,String img){
        this.name=name;
        this.email=email;
        this.password=password;
        this.personalname=personalname;
        this.sex=sex;
        this.img=img;
    }

    public int getUserid(){
        return userid;
    }
    public void setUserid(int userid){
        this.userid=userid;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }

    public String getPersonalname(){
        return personalname;
    }
    public void setPersonalname(String personalname){
        this.personalname=personalname;
    }

    public int getSex(){
        return sex;
    }
    public void setSex(int sex){
        this.sex=sex;
    }

    public String getImg(){
        return img;
    }
    public void setImg(String img){
        this.img=img;
    }
}
