package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.BroadcastExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BroadcastExamRepository extends JpaRepository<BroadcastExam, Long> {

    public BroadcastExam getById(Long id);

    public List<BroadcastExam> getByParentIdOrderByIdDesc(Long parentId);


    @Query(value = "UPDATE broadcast_exam SET status=:status WHERE id=:id", nativeQuery = true)
    @Modifying
    @Transactional
    public void updateSQLStatus(@Param(value = "id") Long id, @Param(value = "status") String status);

}
