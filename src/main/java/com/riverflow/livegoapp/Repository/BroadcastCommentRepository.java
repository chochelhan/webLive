package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.BroadcastComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BroadcastCommentRepository extends JpaRepository<BroadcastComment, Long> {


    public BroadcastComment getById(Long id);

    public List<BroadcastComment> getByPidOrderByIdDesc(Long pid);


}
