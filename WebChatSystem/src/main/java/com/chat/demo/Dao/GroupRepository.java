package com.chat.demo.Dao;

import com.chat.demo.entitys.group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
@Transactional
public interface GroupRepository extends JpaRepository<group, Integer> {
    List<group> findAllByUserid(int userid);

    int findAllGroupidByUserid(int userid);
    @Query("select groupid from group where userid=?1 and groupname=?2")
    int findGroupidByUseridAndGroupname(int userid,String username);
    group findByUseridAndGroupname(int userid,String groupname);
    void deleteByGroupid(int groupid);
}
