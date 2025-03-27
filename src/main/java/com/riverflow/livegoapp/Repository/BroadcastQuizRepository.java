package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.BroadcastQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BroadcastQuizRepository extends JpaRepository<BroadcastQuiz, Long> {

    public BroadcastQuiz getById(Long id);

    public List<BroadcastQuiz> getByParentIdOrderByIdDesc(Long parentId);


    @Query(value = "UPDATE broadcast_quiz SET status=:status WHERE id=:id", nativeQuery = true)
    @Modifying
    @Transactional
    public void updateSQLStatus(@Param(value = "id") Long id, @Param(value = "status") String status);

}
