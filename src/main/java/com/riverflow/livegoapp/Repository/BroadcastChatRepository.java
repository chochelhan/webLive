package com.riverflow.livegoapp.Repository;

import com.riverflow.livegoapp.Entity.BroadcastChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BroadcastChatRepository extends JpaRepository<BroadcastChat, Long> {

    public BroadcastChat getByParentId(Long parentId);


}
