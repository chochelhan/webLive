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
        @Index(name = "index_broadcast_comment_pid", columnList = "pid"),
        @Index(name = "index_broadcast_comment_ctype", columnList = "ctype"),
        @Index(name = "index_broadcast_comment_uid", columnList = "uid"),
        @Index(name = "index_broadcast_comment_name", columnList = "name"),
        @Index(name = "index_broadcast_comment_createAt", columnList = "createAt")
})


public class BroadcastComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;


    @Column(nullable = false)
    protected Long pid;

    @Column(nullable = false, length = 10)
    protected String ctype;

    @Column(nullable = true, length = 50)
    protected String uid;

    @Column(nullable = true, length = 50)
    protected String name;

    @Column(nullable = false)
    protected String content;

    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;


    @Builder
    public BroadcastComment(Long pid,
                            String ctype,
                            String uid,
                            String name,
                            String content,
                            LocalDateTime createAt,
                            String actType,
                            Long actId) {

        this.uid = uid;
        this.pid = pid;
        this.ctype = ctype;
        this.content = content;
        this.name = name;

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
