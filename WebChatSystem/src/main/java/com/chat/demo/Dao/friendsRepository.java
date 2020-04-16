package com.chat.demo.Dao;

import com.chat.demo.entitys.friends;
import com.chat.demo.entitys.user;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
@Transactional
public interface friendsRepository extends JpaRepository<friends, Integer> {
    List<friends> findAllByGroupid(int groupid);
    friends findByUserid(int userid);
    friends findByUseridAndMyid(int userid,int myid);
    void deleteByUseridAndMyid(int userid,int myid);
}
