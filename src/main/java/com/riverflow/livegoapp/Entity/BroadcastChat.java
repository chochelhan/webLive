package com.riverflow.livegoapp.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "index_broadcast_chat_parentId", columnList = "parentId")
})
public class BroadcastChat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(nullable = false)
    protected Long parentId;



    @Column(nullable = false,columnDefinition = "json")
    protected String message;

    @Builder
    public BroadcastChat(Long parentId,
                         String message,
                         String actType,
                         Long actId) {

        this.parentId = parentId;
        this.message = message;
        if (actType.equals("update")) {
            this.id = actId;
        }
    }
}
