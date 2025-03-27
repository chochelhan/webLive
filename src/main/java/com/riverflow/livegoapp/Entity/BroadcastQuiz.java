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
        @Index(name = "index_broadcast_quiz_parentId", columnList = "parentId"),
        @Index(name = "index_broadcast_quiz_status", columnList = "status"),
        @Index(name = "index_broadcast_quiz_createAt", columnList = "createAt")
})
public class BroadcastQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(nullable = false)
    protected Long parentId;

    @Column(nullable = false, length = 80)
    protected String question;

    @Column(nullable = true, length = 2)
    protected String good;

    @Column(nullable = true,columnDefinition = "json")
    protected String items;

    @Column(nullable = false, length = 8)
    protected String prbType;

    @Column(nullable = false, length = 8)
    protected String status;

    @Column(nullable = true, length = 255)
    protected String subjectGood;


    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;


    @Builder
    public BroadcastQuiz(Long parentId,
                         String question,
                         String good,
                         String items,
                         String prbType,
                         String subjectGood,
                         String status,
                         LocalDateTime createAt,
                         String actType,
                         Long actId) {

        this.parentId = parentId;
        this.question = question;
        this.good = good;
        this.items = items;
        this.prbType = prbType;
        this.subjectGood = subjectGood;
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
