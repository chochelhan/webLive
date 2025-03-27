package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.BroadcastBoard;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BroadcastBoardRepository extends JpaRepository<BroadcastBoard, Long> {

    public BroadcastBoard getById(Long id);

    @Query(value = "SELECT b.*, bc.uid, bc.role, bc.vtype FROM broadcast_board AS b LEFT JOIN broadcast AS bc ON b.parent_id=bc.id WHERE b.parent_id=:parentId ORDER BY b.id DESC LIMIT 1", nativeQuery = true)
    public Map<String,Object> getSQLByParentId(@Param(value = "parentId") Long parentId);

    public BroadcastBoard getByParentId(Long parentId);

}
