package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.BroadcastPoll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BroadcastPollRepository extends JpaRepository<BroadcastPoll, Long> {

    public BroadcastPoll getById(Long id);

    public BroadcastPoll getByParentId(Long parentId);

    @Query(value = "UPDATE broadcast_poll SET status=:status WHERE id=:id", nativeQuery = true)
    @Modifying
    @Transactional
    public void updateSQLStatus(@Param(value = "id") Long id, @Param(value = "status") String status);
}
