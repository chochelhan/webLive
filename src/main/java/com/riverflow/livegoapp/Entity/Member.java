package com.riverflow.livegoapp.Entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "index_member_role", columnList = "role"),
        @Index(name = "index_member_auth", columnList = "auth"),
        @Index(name = "index_member_uout", columnList = "uout"),
        @Index(name = "index_member_name", columnList = "name"),
        @Index(name = "index_member_zid", columnList = "zid"),
        @Index(name = "index_member_createAt", columnList = "createAt")
})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(unique = true, nullable = false, length = 50)
    protected String uid;


    @Column(nullable = false, length = 150)
    protected String passwd;


    @Column(nullable = false, length = 8)
    protected String role;

    @Column(nullable = false, length = 3)
    protected String auth;

    @Column(nullable = false, length = 30)
    protected String name;

    @Column(unique = true, nullable = false, length = 60)
    protected String email;

    @Column(length = 3)
    protected String emailSend;

    @Column(length = 100)
    protected String img;

    @Column(length = 3)
    protected String uout;

    @Column(columnDefinition="TEXT",nullable = true)
    protected String accessToken;

    @Column(columnDefinition="TEXT",nullable = true)
    protected String refreshToken;

    @Column(nullable = true, length = 11)
    protected int expires;

    @Column(nullable = true, length = 50)
    protected String zid;


    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;


    @Builder
    public Member(String uid,
                  String auth,
                  String passwd,
                  String name,
                  String role,
                  String email,
                  String emailSend,
                  String img,
                  String uout,
                  String accessToken,
                  String refreshToken,
                  int expires,
                  String zid,
                  LocalDateTime createAt,
                  String actType,
                  Long actId) {

        this.name = name;
        this.email = email;
        this.uid = uid;
        this.passwd = passwd;
        this.role = role;
        this.emailSend = emailSend;
        this.img = img;
        this.uout = uout;
        this.auth = auth;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expires = expires;
        this.zid = zid;

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
