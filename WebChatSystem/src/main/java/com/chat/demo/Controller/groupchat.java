package com.chat.demo.Controller;

import com.auth0.jwt.JWT;
import com.chat.demo.Dao.addgcRepository;
import com.chat.demo.Dao.groupchatRepository;
import com.chat.demo.Dao.messageRepository;
import com.chat.demo.Dao.userRepository;
import com.chat.demo.Util.fileUtil;
import com.chat.demo.annotation.SecurityParameter;
import com.chat.demo.entitys.*;
import com.chat.demo.entitys.group;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "群聊")
@RestController
public class groupchat {
    @Autowired
    userRepository userRepository;
    @Autowired
    groupchatRepository groupchatRepository;
    @Autowired
    com.chat.demo.Dao.addgcRepository addgcRepository;

    @ApiOperation(value = "获取群聊列表",notes = "获取群聊列表")
    @GetMapping("/user/{email}/groupchats")
    public List getgroupchats(@PathVariable("email") String email){
        int userid=userRepository.findUseridByEmail(email);
        List<addgc> addgc=addgcRepository.findAllByUserid(userid);
        List<com.chat.demo.entitys.groupchat> groupchats=new ArrayList<>();
        for(int i=0;i<addgc.size();i++){
            groupchats.add(groupchatRepository.findByGroupchatid(addgc.get(i).getGcid()));
        }
        return groupchats;
    }

    @ApiOperation(value = "创建群聊",notes = "创建群聊")
    @SecurityParameter
    @PostMapping("/groupchats")
    public Map newgroupchats(@RequestBody com.chat.demo.entitys.groupchat groupchat, HttpServletRequest request){
        Map<String,String> map=new HashMap<>();
        String token=request.getHeader("Token");
        String email= JWT.decode(token).getAudience().get(0);
        int userid=userRepository.findUseridByEmail(email);
        groupchat.setCreattime(new Timestamp(System.currentTimeMillis()));
        groupchat.setUserid(userid);
        groupchat.setNum(1);
        if(groupchatRepository.findByUseridAndGcname(userid,groupchat.getGcname())!=null){
            map.put("mes","该群聊已存在");
            return map;
        }
        groupchatRepository.save(groupchat);
        com.chat.demo.entitys.groupchat gc=groupchatRepository.findByUseridAndGcname(userid,groupchat.getGcname());
        addgc add=new addgc(gc.getGroupchatid(),userid,0);
        addgcRepository.save(add);
        map.put("mes","success");
        return map;
    }

    @ApiOperation(value = "解散群聊",notes = "解散群聊")
    @DeleteMapping("/groupchats/{gcID}")
    public String deletegroupchats(@PathVariable("gcID") int gcID){
        groupchatRepository.deleteByGroupchatid(gcID);
        addgcRepository.deleteAllByGcid(gcID);
        return "OK";
    }

    @ApiOperation(value = "获取群成员",notes = "获取群成员")
    @GetMapping("/groupchats/{gcID}/user")
    public List<user> getusers(@PathVariable("gcID") int gcID){
        List<addgc> users=addgcRepository.findAllByGcid(gcID);
        List<user> team_users=new ArrayList<>();
        for(int i=0;i<users.size();i++){
            team_users.add(userRepository.findByUserid(users.get(i).getUserid()));
        }
        return team_users;

    }


    @ApiOperation(value = "加入群聊",notes = "加入群聊")
    @PatchMapping("/user/{email}/groupchats/{gcID}")
    public Map addgroupchats(@PathVariable("gcID") int gcID,
                                @PathVariable("email") String email){
        Map<String,String> map=new HashMap<>();
        int userid=userRepository.findUseridByEmail(email);
        addgc add=addgcRepository.findByUseridAndGcid(userid,gcID);
        if(add!=null){
            map.put("mes","已加入该群组");
            return map;
        }
        addgc theadd=new addgc(gcID,userid,2);
        addgcRepository.save(theadd);
        com.chat.demo.entitys.groupchat group=groupchatRepository.findByGroupchatid(gcID);
        group.setNum(group.getNum()+1);
        groupchatRepository.save(group);
        map.put("mes","success");
        return map;
    }

    @GetMapping("/ismember/{email}/{gcID}")
    public boolean ismember(@PathVariable("email") String email,@PathVariable("gcID") int gcID){
        int userid=userRepository.findUseridByEmail(email);
        addgc add=addgcRepository.findByUseridAndGcid(userid,gcID);
        if(add!=null) return true;
        else return false;
    }




    @ApiOperation(value = "踢出群聊",notes = "踢出群聊")
    @PatchMapping("/groupchats/{gcID}/user/{email}")
    public Map deletegroupchats(@PathVariable("email") String email,
                                @PathVariable("gcID") int groupchatID,
                                   HttpServletRequest request
                                   ){
        Map<String,String> map=new HashMap<>();
        String token=request.getHeader("Token");
        com.chat.demo.entitys.groupchat groupchat=groupchatRepository.findByGroupchatid(groupchatID);
        String theemail= JWT.decode(token).getAudience().get(0);
        int userid=userRepository.findUseridByEmail(theemail);
        int touserid=userRepository.findUseridByEmail(email);
        addgc add=addgcRepository.findByUseridAndGcid(userid,groupchatID);
        addgc toadd=addgcRepository.findByUseridAndGcid(touserid,groupchatID);
        if(add.getRole()==0&&touserid!=userid) {
            groupchat.setNum(groupchat.getNum()-1);
            groupchatRepository.save(groupchat);
            addgcRepository.deleteByUseridAndGcid(touserid,groupchatID);
            map.put("mes","success");
            return map;
        }
        if(add.getRole()!=0&&touserid==userid) {
            groupchat.setNum(groupchat.getNum()-1);
            groupchatRepository.save(groupchat);
            addgcRepository.deleteByUseridAndGcid(touserid,groupchatID);
            map.put("mes","success");
            return map;
        }
        if(add.getRole()==1&&toadd.getRole()==2) {
            groupchat.setNum(groupchat.getNum()-1);
            groupchatRepository.save(groupchat);
            addgcRepository.deleteByUseridAndGcid(touserid,groupchatID);
            map.put("mes","success");
            return map;
        }
        else {
            map.put("mes","你没有该权限");
            return map;
        }
    }

    @Autowired
    com.chat.demo.Dao.messageRepository messageRepository;
    @ApiOperation(value = "获取群聊历史消息",notes = "获取群聊历史消息")
    @GetMapping("/groupchats/{gcID}/messages")
    public List<message> groupchatsnews(@PathVariable("gcID") int groupchatID){
        List<message> mess=messageRepository.findAllByTouseridOrderByMestime(groupchatID);
        return mess;
    }



    @ApiOperation(value = "获取群聊信息",notes = "获取群聊信息")
    @GetMapping("/groupchats/{id}")
    public com.chat.demo.entitys.groupchat groupchats_index(@PathVariable("id") int groupchatID){
        return groupchatRepository.findByGroupchatid(groupchatID);
    }

    @PostMapping("/groupchat/info")
    @SecurityParameter(inDecode = true,outDecode = false)
    public Map  gc_info(@RequestBody com.chat.demo.entitys.groupchat groupchat, HttpServletRequest request){
        Map<String,String> map=new HashMap<>();
        String token=request.getHeader("Token");
        String email=JWT.decode(token).getAudience().get(0);
        int userid=userRepository.findUseridByEmail(email);
        com.chat.demo.entitys.groupchat agroupchat=groupchatRepository.findByGroupchatid(groupchat.getGroupchatid());
        if(userid!=agroupchat.getUserid()){
            System.out.println(userid+agroupchat.getUserid());
            map.put("mes","你没有该权限");
            return map;
        }
        agroupchat.setGcname(groupchat.getGcname());
        agroupchat.setDescribtion(groupchat.getDescribtion());
        groupchatRepository.save(agroupchat);
        map.put("mes","success");
        return map;
    }

    @PostMapping("/groupchat/img")
    public String user_img(@RequestParam("photo") MultipartFile file,int gcid){
        String fileName=file.getOriginalFilename();
        String suffixName=fileName.substring(fileName.lastIndexOf("."));
        if(!(suffixName.equals(".png")||suffixName.equals(".jpg")||suffixName.equals(".jpeg")||suffixName.equals(".gif"))) return null;
        String Path="/usr/WebChatSystem/user_img/";
//        String Path="D:\\美图\\";
        fileUtil fileUtil=new fileUtil();
        fileUtil.upload_img(file,Path,fileName);
        if(gcid!=-1){
            com.chat.demo.entitys.groupchat groupchat=groupchatRepository.findByGroupchatid(gcid);
            if(groupchat!=null) {
                groupchat.setImg("http://39.106.105.240:80/file/image/"+fileName);
                groupchatRepository.save(groupchat);
            }
        }
        return "http://39.106.105.240:80/file/image/"+fileName;
    }

}
