package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.Broadcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface BroadcastRepository extends JpaRepository<Broadcast, Long> {

    public Broadcast getById(Long id);

    @Query(value = "SELECT * FROM broadcast WHERE uid=:uid AND status='ready' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public Broadcast getByUidLatest(@Param(value = "uid") String uid);

    public List<Broadcast> getByUidOrderByIdDesc(String uid);


    @Query(value = "SELECT b.*," +
            "       bn.safety," +
            "       bn.safety_emails," +
            "       bn.observe_emails," +
            "       bn.accept_type," +
            "       bn.reqdata," +
            "       bn.join_user," +
            "       bn.after_open," +
            "       bn.exam_imm_answer," +
            "       bn.exam_down" +
            " FROM broadcast AS b LEFT JOIN broadcast_env AS bn ON b.id=bn.parent_id WHERE b.id=:id AND b.status!='complete'", nativeQuery = true)
    public Map<String,Object> getSQLBroadcastInfoWithEnvNotComplete(@Param(value = "id") Long id);


    @Query(value = "UPDATE broadcast SET status='complete',end_time=:now WHERE id=:id", nativeQuery = true)
    @Modifying
    @Transactional
    public void updateSQLEnd(@Param(value = "id") Long id, @Param(value = "now") LocalDateTime now);


    @Query(value = "UPDATE broadcast SET start_time=:now WHERE id=:id", nativeQuery = true)
    @Modifying
    @Transactional
    public void updateSQLStartTime(@Param(value = "id") Long id, @Param(value = "now") LocalDateTime now);


}
