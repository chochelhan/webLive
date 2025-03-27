package com.riverflow.livegoapp.Entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "index_broadcast_role", columnList = "role"),
        @Index(name = "index_broadcast_uid", columnList = "uid"),
        @Index(name = "index_broadcast_vtype", columnList = "vtype"),
        @Index(name = "index_broadcast_subject", columnList = "subject"),
        @Index(name = "index_broadcast_status", columnList = "status"),
        @Index(name = "index_broadcast_createAt", columnList = "createAt")
})
public class Broadcast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(nullable = false, length = 50)
    protected String uid;

    @Column(nullable = false, length = 80)
    protected String subject;

    @Column(nullable = false, length = 10)
    protected String vtype;

    @Column(nullable = false, length = 8)
    protected String role;

    @Column(nullable = false, length = 8)
    protected String status;

    @Column(nullable = true)
    protected Long meetingNo;

    @Column(columnDefinition="TEXT",nullable = true)
    protected String zak;


    @Column(nullable = true, length = 50)
    protected String logo;



    @Column(nullable = true, length = 50)
    protected String logoName;

    @Column(nullable = true)
    protected LocalDateTime startTime;

    @Column(nullable = true)
    protected LocalDateTime endTime;

    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;


    @Builder
    public Broadcast(String uid,
                     String subject,
                     String vtype,
                     String role,
                     LocalDateTime createAt,
                     String status,
                     Long meetingNo,
                     String zak,
                     String logo,
                     String logoName,
                     String actType,
                     Long actId) {

        this.uid = uid;
        this.subject = subject;
        this.role = role;
        this.vtype = vtype;
        this.logo = logo;
        this.zak = zak;
        this.meetingNo = meetingNo;
        this.logoName = logoName;
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
