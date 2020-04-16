package com.chat.demo.Dao;

import com.chat.demo.entitys.mailcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface codeRepository extends JpaRepository<mailcode, Integer> {
    mailcode findByEmail(String email);
}
