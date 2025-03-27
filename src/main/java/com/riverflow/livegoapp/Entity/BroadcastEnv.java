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
        @Index(name = "index_broadcast_env_parentId", columnList = "parentId"),
        @Index(name = "index_broadcast_env_safety", columnList = "safety"),
})
public class BroadcastEnv {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(nullable = false)
    protected Long parentId;

    @Column(nullable = false, length = 3)
    protected String safety;

    @Column(columnDefinition="TEXT",nullable = true)
    protected String safetyEmails;

    @Column(columnDefinition="TEXT",nullable = true)
    protected String observeEmails;

    @Column(nullable = false, length = 3)
    protected String acceptType;



    @Column(nullable = true,columnDefinition = "json")
    protected String reqdata;

    @Column(nullable = false, length = 10)
    protected String joinUser;

    @Column(nullable = true,columnDefinition = "json")
    protected String afterOpen;

    @Column(nullable = true, length = 3)
    protected String examImmAnswer;

    @Column(nullable = true, length = 3)
    protected String examDown;



    @Builder
    public BroadcastEnv(Long parentId,
                        String safety,
                        String safetyEmails,
                        String observeEmails,
                        String acceptType,
                        String reqdata,
                        String joinUser,
                        String afterOpen,
                        String examImmAnswer,
                        String examDown,
                        String actType,
                        Long actId) {

        this.parentId = parentId;
        this.safety = safety;
        this.safetyEmails = safetyEmails;
        this.observeEmails = observeEmails;
        this.acceptType = acceptType;
        this.reqdata = reqdata;
        this.joinUser = joinUser;
        this.afterOpen = afterOpen;
        this.examDown = examDown;
        this.examImmAnswer = examImmAnswer;
        if (actType.equals("update")) {
            this.id = actId;
        }

    }
}
