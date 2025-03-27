package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.BroadcastJoinUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BroadcastJoinUsersRepository extends JpaRepository<BroadcastJoinUsers, Long> {

    public BroadcastJoinUsers getById(Long id);

    public List<BroadcastJoinUsers> getByParentIdAndStatusOrderByNameDesc(Long parentId,String status);

    public List<BroadcastJoinUsers> getByParentIdOrderByNameDesc(Long parentId);


    public BroadcastJoinUsers getByParentIdAndUid(Long parentId,String uid);

    @Query(value = "UPDATE broadcast_join_users SET status=:status WHERE uid=:uid AND parent_id=:parentId", nativeQuery = true)
    @Modifying
    @Transactional
    public void updateSQLStatus(@Param(value = "parentId") Long parentId,@Param(value = "uid") String uid, @Param(value = "status") String status);
}
