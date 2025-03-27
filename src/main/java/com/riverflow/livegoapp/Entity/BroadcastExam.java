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
        @Index(name = "index_broadcast_exam_parentId", columnList = "parentId"),
        @Index(name = "index_broadcast_exam_subject", columnList = "subject"),
        @Index(name = "index_broadcast_exam_status", columnList = "status"),
        @Index(name = "index_broadcast_exam_createAt", columnList = "createAt")
})
public class BroadcastExam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(nullable = false)
    protected Long parentId;

    @Column(nullable = false, length = 80)
    protected String subject;

    @Column(nullable = false, length = 40)
    protected String author;

    @Column(nullable = false,columnDefinition = "json")
    protected String problems;

    @Column(nullable = false, length = 8)
    protected String status;

    @Column(nullable = false, length = 8)
    protected int timeLimit;

    @Column(nullable = false, length = 8)
    protected int totalJumsu;

    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;


    @Builder
    public BroadcastExam(Long parentId,
                         String subject,
                         String author,
                         String problems,
                         String status,
                         int timeLimit,
                         int totalJumsu,
                         LocalDateTime createAt,
                         String actType,
                         Long actId) {

        this.parentId = parentId;
        this.subject = subject;
        this.author = author;
        this.problems = problems;
        this.timeLimit = timeLimit;
        this.totalJumsu = totalJumsu;
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
