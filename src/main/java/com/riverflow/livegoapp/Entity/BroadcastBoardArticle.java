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
        @Index(name = "index_broadcast_board_article_parentId", columnList = "parentId"),
        @Index(name = "index_broadcast_board_article_uid", columnList = "uid"),
        @Index(name = "index_broadcast_board_article_subject", columnList = "subject"),
        @Index(name = "index_broadcast_board_article_notice", columnList = "notice"),
        @Index(name = "index_broadcast_board_article_createAt", columnList = "createAt")
})
public class BroadcastBoardArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(nullable = false)
    protected Long parentId;


    @Column(nullable = true, length = 50)
    protected String uid;

    @Column(nullable = true, length = 50)
    protected String userName;

    @Column(nullable = false, length = 80)
    protected String subject;

    @Column(columnDefinition="TEXT",nullable = true)
    protected String content;


    @Column(nullable = true, length = 50)
    protected String fileName;

    @Column(nullable = true, length = 50)
    protected String fileUrl;

    @Column(nullable = false, length = 3)
    protected String notice;

    @Column(nullable = true, length = 10)
    protected int hit;

    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;


    @Builder
    public BroadcastBoardArticle(Long parentId,
                                 String uid,
                                 String userName,
                                 String subject,
                                 String content,
                                 String fileName,
                                 String fileUrl,
                                 String notice,
                                 int hit,
                                 LocalDateTime createAt,
                                 String actType,
                                 Long actId) {

        this.parentId = parentId;
        this.uid = uid;
        this.userName = userName;
        this.subject = subject;
        this.content = content;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.notice = notice;
        this.hit = hit;
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
