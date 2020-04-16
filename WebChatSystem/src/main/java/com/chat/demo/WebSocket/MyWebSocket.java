package com.chat.demo.WebSocket;

import com.auth0.jwt.JWT;
import com.chat.demo.Dao.*;
import com.chat.demo.entitys.addgc;
import com.chat.demo.entitys.message;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.*;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Logger;

@Component
@ServerEndpoint(value = "/MyWebsocket/{Token}")
public class MyWebSocket {

    private static userRepository userRepository;
    private static addgcRepository addgcRepository;
    private static groupchatRepository groupchatRepository;
    private static messageRepository messageRepository;

    @Autowired
    public void setRepository(userRepository userRepository,addgcRepository addgcRepository,groupchatRepository groupchatRepository,messageRepository messageRepository){
        MyWebSocket.userRepository=userRepository;
        MyWebSocket.addgcRepository=addgcRepository;
        MyWebSocket.groupchatRepository=groupchatRepository;
        MyWebSocket.messageRepository=messageRepository;
    }

    //存放客户端对应的Websocket
    private static ConcurrentHashMap<String, MyWebSocket> websockets=new ConcurrentHashMap();
    private static Logger logger=Logger.getLogger("WebSocket");

    private Session session;


    private String email;

    @OnOpen
    public void OnOpen(Session session, @PathParam("Token")String token){
        System.out.println(token);
        String email = JWT.decode(token).getAudience().get(0);
        this.session=session;
        this.email=email;
        websockets.put(email,this);
        System.out.println("当前在线人数："+websockets.size());
    }

    @OnClose
    public void OnClose(){
        websockets.remove(this);
    }

    @OnError
    public void OnError(Session session,Throwable error){
        logger.info("error");
        System.out.println(error);
        error.printStackTrace();
    }


    @OnMessage
    public void OnMessage(String message,Session session) throws IOException, JSONException {
        System.out.println(message);
        JSONObject Message=new JSONObject(message);
        String email=Message.getString("touseremail");
        int toid=Message.getInt("touserid");
        int Type=Message.getInt("mestype");
        Message.put("fromuser",this.email);
        savemes(message);
        System.out.println(Type);
        if(Type==0) {
            if(websockets.containsKey(email))
            websockets.get(email).sendmessage(message);
            else System.out.println(email+"不在线");
        }
        else if(Type==1){
            List<addgc> auser;
            addgc a;
            int userid;
            String mail;
            auser=addgcRepository.findAllByGcid(toid);
            for(int i=0;i<auser.size();i++){
                a=auser.get(i);
                userid=a.getUserid();
                mail=userRepository.findEmailByuserid(userid);
                if(websockets.containsKey(mail))
                    websockets.get(mail).sendmessage(message);
                else System.out.println(email+"不在线");
            }
        }
        else if(Type==2){
            if(websockets.size()>0) {
                for (MyWebSocket myWebSocket : websockets.values()) {
                    myWebSocket.sendmessage(message);
                }
            }
        }
    }


    public void sendmessage(String message) throws IOException {
        if(this.session.isOpen()){
            this.session.getBasicRemote().sendText(message);
        }
    }


    public void savemes(String Message) throws JSONException {
        JSONObject aMessage= new JSONObject(Message);
        message message=new message();
        message.setFromuseremail(aMessage.getString("fromuseremail"));
        message.setTouseremail(aMessage.getString("touseremail"));
        message.setMestext(aMessage.getString("mestext"));
        message.setMestype(aMessage.getInt("mestype"));
        message.setTouserid(aMessage.getInt("touserid"));
        message.setMestime(new Timestamp(System.currentTimeMillis()));
        messageRepository.save(message);
    }

}
