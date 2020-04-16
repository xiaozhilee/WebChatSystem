package com.chat.demo.Controller;

import com.auth0.jwt.JWT;
import com.chat.demo.Dao.GroupRepository;
import com.chat.demo.Dao.friendsRepository;
import com.chat.demo.Dao.messageRepository;
import com.chat.demo.Dao.userRepository;
import com.chat.demo.entitys.friends;
import com.chat.demo.entitys.message;
import com.chat.demo.entitys.user;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.sql.Timestamp;
import java.util.*;

@Api(description = "好友")
@RestController
public class friend {
    @Autowired
    userRepository userRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    com.chat.demo.Dao.friendsRepository friendsRepository;

    @ApiOperation(value = "获取好友信息",notes = "获取好友信息")
    @ApiImplicitParam(name = "femail",value = "好友email",required = true,dataType = "String")
    @GetMapping("/friends/{femail}")
    public user getfriend(@PathVariable(value = "femail") String femail){
        System.out.println(femail);
        user user=userRepository.findByEmail(femail);
        if(user==null) return null;
        user.setPassword("00000");
        return user;
    }

    @ApiOperation(value = "添加好友",notes = "添加好友")
    @ApiImplicitParam(name = "femail",value = "好友email",required = true,dataType = "String")
    @PostMapping("/friends/{femail}")
    public Map newfriend(@PathVariable(value = "femail") String femail, HttpServletRequest request,String groupname,String name2){
        String token=request.getHeader("Token");
        String email= JWT.decode(token).getAudience().get(0);
        Map<String,String> map=new HashMap<>();
        if(isfriend(femail,request)) {
            map.put("mes","你们已经是好友了");
            return map;
        }
        if(email.equals(femail)){
            map.put("mes","不能加自己为好友");
            return map;
        }
        int friendid=userRepository.findUseridByEmail(femail);
        int userid=userRepository.findUseridByEmail(email);
        int groupid;
        if(groupname!=null) {
            System.out.println(groupname);
            groupid = groupRepository.findGroupidByUseridAndGroupname(userid, groupname);
        }
        else groupid=groupRepository.findGroupidByUseridAndGroupname(userid, "好友");
        friends friend=new friends(friendid,userid,groupid,name2,new Timestamp(System.currentTimeMillis()));
        friendsRepository.save(friend);

        int groupid2;
        groupid2=groupRepository.findGroupidByUseridAndGroupname(friendid,"好友");
        friends friends2=new friends(userid,friendid,groupid2,null,new Timestamp(System.currentTimeMillis()));
        friendsRepository.save(friends2);
        map.put("mes","success");
        return map;
    }

    @ApiOperation(value = "删除好友",notes = "删除好友")
    @ApiImplicitParam(name = "femail",value = "好友email",required = true,dataType = "String")
    @DeleteMapping("/user/{uemail}/friends/{femail}")
    public Map deletefriend(@PathVariable(value = "femail") String femail,@PathVariable(value = "uemail") String uemail){
        Map<String,String> map=new HashMap<>();
        int userid=userRepository.findUseridByEmail(uemail);
        int friendid=userRepository.findUseridByEmail(femail);
        friendsRepository.deleteByUseridAndMyid(friendid,userid);
        map.put("mes","success");
        return map;
    }

    @ApiOperation(value = "修改好友备注",notes = "修改好友备注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "femail",value = "好友email",required = true,dataType = "String")
,            @ApiImplicitParam(name = "name",value = "好友备注",required = true,dataType = "String")
    })
    @PatchMapping("/friends/{femail}")
    public friends friendname2(@PathVariable(value = "femail") String femail,
                              @PathVariable(value = "name") String name
                                ){
        int frendid=userRepository.findUseridByEmail(femail);
        friends friend=friendsRepository.findByUserid(frendid);
        friend.setName2(name);
        friendsRepository.save(friend);
        return friend;
    }


    @Autowired
    com.chat.demo.Dao.messageRepository messageRepository;
    @ApiOperation(value = "获取好友历史消息",notes = "获取好友历史消息")
    @GetMapping("/friends/{femail}/messages")
    @ApiImplicitParam(name = "femail",value = "好友email",required = true,dataType = "String")
    public List<message> groupchatsnews(@PathVariable(value = "femail") String femail,HttpServletRequest request){
        String token=request.getHeader("Token");
        String email= JWT.decode(token).getAudience().get(0);
        List<message> mess=messageRepository.findAllByFromuseremailAndTouseremailOrderByMestime(email,femail);
        return mess;
    }

    @GetMapping("/user/{femail}/friend")
    public boolean isfriend(@PathVariable(value = "femail") String femail,HttpServletRequest request){
        String token=request.getHeader("Token");
        String email= JWT.decode(token).getAudience().get(0);
        int userid=userRepository.findUseridByEmail(email);
        int fid=userRepository.findUseridByEmail(femail);
        friends friends=friendsRepository.findByUseridAndMyid(fid,userid);
        if(friends==null) return false;
        return true;
    }
}
