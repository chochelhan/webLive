package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.BroadcastEnv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BroadcastEnvRepository extends JpaRepository<BroadcastEnv, Long> {

    public BroadcastEnv getById(Long id);

    public BroadcastEnv getByParentId(Long parentId);

}
