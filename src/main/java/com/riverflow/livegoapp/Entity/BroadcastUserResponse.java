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
        @Index(name = "index_broadcast_user_response_pid", columnList = "pid"),
        @Index(name = "index_broadcast_user_response_uid", columnList = "uid"),
        @Index(name = "index_broadcast_user_response_rtype", columnList = "rtype"),
        @Index(name = "index_broadcast_user_response_name", columnList = "name"),
        @Index(name = "index_broadcast_user_response_createAt", columnList = "createAt")
})
public class BroadcastUserResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(nullable = false)
    protected Long pid;

    @Column(nullable = false, length = 80)
    protected String uid;

    @Column(nullable = false, length = 10)
    protected String rtype;

    @Column(nullable = false, length = 40)
    protected String name;

    @Column(nullable = true, length = 8)
    protected int ptotal;

    @Column(nullable = true, length = 8)
    protected int gtotal;

    @Column(nullable = true, length = 8)
    protected int jumsu;


    @Column(nullable = true,columnDefinition = "json")
    protected String answers;


    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;


    @Builder
    public BroadcastUserResponse(Long pid,
                                 String uid,
                                 String rtype,
                                 int ptotal,
                                 int gtotal,
                                 int jumsu,
                                 String name,
                                 String answers,
                                 LocalDateTime createAt,
                                 String actType,
                                 Long actId) {

        this.pid = pid;
        this.uid = uid;
        this.rtype = rtype;
        this.ptotal = ptotal;
        this.gtotal = gtotal;
        this.jumsu = jumsu;
        this.name = name;
        this.answers = answers;

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
