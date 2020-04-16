package com.chat.demo.Dao;

import com.chat.demo.entitys.message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface messageRepository extends JpaRepository<message, Integer> {
    List<message> findAllByFromuseremail(String email);
    @Query(value = "SELECT * FROM `message` m where (m.fromuseremail=?1 and m.touseremail=?2)or  (m.touseremail=?1 and m.fromuseremail=?2) order by m.mestime", nativeQuery = true)
    List<message> findAllByFromuseremailAndTouseremailOrderByMestime(String femail,String temail);
    List<message> findAllByFromuseremailAndTouseridOrderByMestime(String femail,int touserid);
    List<message> findAllByTouseridOrderByMestime(int touserid);
}
