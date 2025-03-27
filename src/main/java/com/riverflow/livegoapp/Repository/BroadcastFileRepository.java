package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.BroadcastFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BroadcastFileRepository extends JpaRepository<BroadcastFile, Long> {

    public BroadcastFile getById(Long id);

    public List<BroadcastFile> getByParentIdOrderByIdDesc(Long parentId);


    public List<BroadcastFile> getByParentIdAndStatusOrderByIdDesc(Long parentId,String status);

}
