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
        @Index(name = "index_broadcast_join_users_parentId", columnList = "parentId"),
        @Index(name = "index_broadcast_join_users_uid", columnList = "uid"),
        @Index(name = "index_broadcast_join_users_name", columnList = "name"),
        @Index(name = "index_broadcast_join_users_email", columnList = "email"),
        @Index(name = "index_broadcast_join_users_status", columnList = "status"),
        @Index(name = "index_broadcast_join_users_createAt", columnList = "createAt")
})
public class BroadcastJoinUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(nullable = false)
    protected Long parentId;

    @Column(nullable = false, length = 50)
    protected String uid;

    @Column(nullable = false, length = 40)
    protected String name;

    @Column(nullable = false, length = 50)
    protected String email;

    @Column(nullable = true, length = 50)
    protected String job;

    @Column(nullable = true, length = 50)
    protected String part;

    @Column(nullable = true, length = 50)
    protected String posi;

    @Column(nullable = false, length = 8)
    protected String status;

    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;


    @Builder
    public BroadcastJoinUsers(Long parentId,
                              String uid,
                              String name,
                              String email,
                              String job,
                              String part,
                              String posi,
                              String status,
                              LocalDateTime createAt,
                              String actType,
                              Long actId) {

        this.parentId = parentId;
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.job = job;
        this.part = part;
        this.posi = posi;
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
