package com.chat.demo.Controller;

import com.auth0.jwt.JWT;
import com.chat.demo.Dao.GroupRepository;
import com.chat.demo.Dao.friendsRepository;
import com.chat.demo.Dao.userRepository;
import com.chat.demo.annotation.SecurityParameter;
import com.chat.demo.entitys.*;
import com.chat.demo.entitys.groupchat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Api(description = "分组")
@RestController
public class group {
    private Logger logger=Logger.getLogger("group");

    @Autowired
    userRepository userRepository;
    @Autowired
    friendsRepository friendsRepository;
    @Autowired
    GroupRepository groupRepository;

    @ApiOperation(value = "获取好友列表",notes = "获取好友列表")
    @GetMapping("/user/{email}/groups")
    public List getfriendsgroup(@PathVariable(value = "email")String email){
        logger.info(email+" try to get group list!");
        user user=userRepository.findByEmail(email);
        List<com.chat.demo.entitys.group> groups=groupRepository.findAllByUserid(user.getUserid());
        return groups;
    }

    @ApiOperation(value = "创建好友列表",notes = "创建好友列表")
    @PostMapping("/user/{email}/groups")
    @SecurityParameter(inDecode = false,outDecode = false)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eamil",value = "email",required = true,dataType = "String"),
            @ApiImplicitParam(name = "groupname",value = "新的列表名称",required = true,dataType = "String")
    })
    public Map newfriendsgroup(@PathVariable(value = "email")String email, @RequestParam String groupname){
        System.out.println(groupname);
        Map<String,String> map=new HashMap<>();
        int userid=userRepository.findUseridByEmail(email);
        com.chat.demo.entitys.group group=new com.chat.demo.entitys.group(userid,groupname);
        if(groupRepository.findByUseridAndGroupname(userid,groupname)!=null){
            map.put("mes","该分组已存在");
            return map;
        }
        groupRepository.save(group);
        map.put("mes","success");
        return map;
    }


    @ApiOperation(value = "修改好友所在列表",notes = "创建好友列表")
    @PatchMapping("/user/{email}/groups")
    @SecurityParameter(inDecode = false,outDecode = false)
    public Map newgroup(@PathVariable(value = "email")String email,HttpServletRequest request,@RequestParam String groupname){
        Map<String,String> map=new HashMap<>();
        String token=request.getHeader("Token");
        String myemail=JWT.decode(token).getAudience().get(0);
        int myid=userRepository.findUseridByEmail(myemail);
        int userid=userRepository.findUseridByEmail(email);
        friends friends=friendsRepository.findByUseridAndMyid(userid,myid);
        if(friends==null){
            map.put("mes","没有该好友");
            return map;
        }
        int groupid=groupRepository.findGroupidByUseridAndGroupname(myid,groupname);
        friends.setGroupid(groupid);
        friendsRepository.save(friends);
        map.put("mes","success");
        return map;
    }


    @ApiOperation(value = "修改列表名称",notes = "修改列表名称")
    @PatchMapping("/user/{email}/groups/{groupname}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email",value = "用户email",required = true,dataType = "String"),
            @ApiImplicitParam(name = "groupname",value = "旧的列表名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "newgroupname",value = "新的列表名称",required = true,dataType = "String")
    })
    public com.chat.demo.entitys.group Patchfriendsgroup(@PathVariable(value = "email") String email,
                                                         @PathVariable(value = "groupname") String groupname,
                                                         @PathVariable(value = "newgroupname") String newgroupname
                                    ){
        int userid=userRepository.findUseridByEmail(email);
        com.chat.demo.entitys.group group=groupRepository.findByUseridAndGroupname(userid,groupname);
        group.setGroupname(groupname);
        groupRepository.save(group);
        return group;
    }



    @ApiOperation(value = "删除好友列表",notes = "删除好友列表")
    @DeleteMapping("/user/{email}/groups/{groupname}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email",value = "用户email",required = true,dataType = "String"),
            @ApiImplicitParam(name = "groupname",value = "列表名称",required = true,dataType = "String"),
    })
    public Map deletegroup(@PathVariable(value = "email") String email,
                           @PathVariable(value = "groupname") String groupname,
                           HttpServletRequest request){
        Map<String,String> map=new HashMap<>();
        String token=request.getHeader("Token");
        String realemail= JWT.decode(token).getAudience().get(0);
        if(!realemail.equals(email)){
            map.put("mes","请确认本人操作");
        }
        int userid=userRepository.findUseridByEmail(email);
        int groupid=groupRepository.findGroupidByUseridAndGroupname(userid,groupname);
        List<friends> friends=friendsRepository.findAllByGroupid(groupid);
        int defgroupid=groupRepository.findGroupidByUseridAndGroupname(userid,"好友");
        for(int i=0;i<friends.size();i++){
            friends.get(i).setGroupid(defgroupid);
            friendsRepository.save(friends.get(i));
        }
        groupRepository.deleteByGroupid(groupid);
        map.put("mes","success");
        return map;
    }


    @ApiOperation(value = "获取列表好友",notes = "获取列表好友")
    @GetMapping("groups/{groupid}/friends")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupname",value = "列表名称",required = true,dataType = "String"),
    })
    public List getfriends(@PathVariable(value = "groupid") int groupid){
        List<friends> fr=friendsRepository.findAllByGroupid(groupid);
        List<user> users=new ArrayList<>();
        for(int i=0;i<fr.size();i++)
        {
            users.add(userRepository.findByUserid(fr.get(i).getUserid()));
        }
        return users;
    }

    @ApiOperation(value = "改变好友所在分组",notes = "改变好友所在分组")
    @PatchMapping("/friends/{femail}/group/groupname")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "femail",value = "好友email",required = true,dataType = "String"),
            @ApiImplicitParam(name = "groupname",value = "新的分组名称",required = true,dataType = "String"),

    })
   public friends newfriendgroup(
            @PathVariable(value = "femail") String femail,
            @PathVariable(value = "groupname") String groupname,
            HttpServletRequest request
            ){
        String token=request.getHeader("Token");
        String realemail= JWT.decode(token).getAudience().get(0);
        int userid=userRepository.findUseridByEmail(realemail);
        int groupid=groupRepository.findGroupidByUseridAndGroupname(userid,groupname);
        int fid=userRepository.findUseridByEmail(femail);
        friends friend=friendsRepository.findByUserid(fid);
        friend.setGroupid(groupid);
        friendsRepository.save(friend);
        return friend;
    }

}
