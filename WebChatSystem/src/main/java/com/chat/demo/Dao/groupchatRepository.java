package com.chat.demo.Dao;

import com.chat.demo.entitys.groupchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface groupchatRepository extends JpaRepository<groupchat, Integer> {
    com.chat.demo.entitys.groupchat findByGroupchatid(int id);
    com.chat.demo.entitys.groupchat findByUseridAndGcname(int userid,String gcname);
    void deleteByGroupchatid(int groupchatID);
}
