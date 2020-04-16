package com.chat.demo.Dao;

import com.chat.demo.entitys.addgc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface addgcRepository extends JpaRepository<addgc,Integer> {
    List<addgc> findAllByUserid(int id);

    List<addgc> findAllByGcid(int Gcid);

    void deleteAllByGcid(int gcID);
    addgc findByUseridAndGcid(int userid,int gcid);
    void deleteByUseridAndGcid(int userid,int gcid);
}
