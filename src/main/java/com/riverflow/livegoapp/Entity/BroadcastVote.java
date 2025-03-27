package com.riverflow.livegoapp.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "index_broadcast_poll_parentId", columnList = "parentId"),
        @Index(name = "index_broadcast_poll_subject", columnList = "subject"),
        @Index(name = "index_broadcast_poll_createAt", columnList = "createAt")
})
public class BroadcastVote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(nullable = false)
    protected Long parentId;

    @Column(nullable = false, length = 80)
    protected String subject;


    @Column(nullable = false,columnDefinition = "json")
    protected String items;

    @Column(nullable = false, length = 8)
    protected String multigood;

    @Column(nullable = false, length = 8)
    protected String status;


    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;


    @Builder
    public BroadcastVote(Long parentId,
                         String subject,
                         String items,
                         String multigood,
                         String status,
                         LocalDateTime createAt,
                         String actType,
                         Long actId) {

        this.parentId = parentId;
        this.subject = subject;
        this.items = items;
        this.multigood = multigood;
        this.status = status;
        LocalDateTime now = LocalDateTime.now();
        if (actType.equals("update")) {
            this.id = actId;
            this.updateAt = now;
            this.createAt = (createAt == null) ? now : createAt;
        } else {
            this.updateAt = now;
            this.createAt = now;
        }

    }
}
