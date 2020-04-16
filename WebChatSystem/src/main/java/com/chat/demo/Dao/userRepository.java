package com.chat.demo.Dao;

import com.chat.demo.entitys.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface userRepository extends JpaRepository<user,Long> {
    user findByEmail(String email);
    @Query("SELECT email from user where userid=?1")
    String findEmailByuserid(int userid);
    @Query("SELECT userid from user where email=?1")
    int findUseridByEmail(String email);
    user findByUserid(int id);
}
