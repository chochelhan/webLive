package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.BroadcastUserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BroadcastUserResponseRepository extends JpaRepository<BroadcastUserResponse, Long> {

    public BroadcastUserResponse getById(Long id);

    public BroadcastUserResponse getByPidAndRtypeAndUid(Long pid,String rtype,String uid);

    @Query(value = "SELECT r.* FROM broadcast_user_response AS r LEFT JOIN broadcast_vote AS v ON v.id=r.pid WHERE r.uid=:uid AND v.parent_id=:parentId", nativeQuery = true)
    public BroadcastUserResponse  getSQLByVote(@Param(value = "parentId") Long parentId,@Param(value = "uid") String uid);

    @Query(value = "SELECT r.* FROM broadcast_user_response AS r LEFT JOIN broadcast_poll AS v  ON v.id=r.pid WHERE r.uid=:uid AND v.parent_id=:parentId", nativeQuery = true)
    public BroadcastUserResponse  getSQLByPoll(@Param(value = "parentId") Long parentId,@Param(value = "uid") String uid);

    public List<BroadcastUserResponse> getByPidAndRtypeOrderByIdDesc(Long pid,String rtype);


    @Query(value = "SELECT r.*,u.email FROM broadcast_user_response AS r LEFT JOIN broadcast_join_users AS u  ON r.uid=u.uid  AND u.parent_id=:parentId WHERE r.id=:id LIMIT 1", nativeQuery = true)
    public BroadcastUserResponse  getSQLByIdWithUserEmail(@Param(value = "parentId") Long parentId,@Param(value = "id") Long id);

}
