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
        @Index(name = "index_broadcast_board_parentId", columnList = "parentId"),
        @Index(name = "index_broadcast_board_boardName", columnList = "boardName"),
        @Index(name = "index_broadcast_board_createAt", columnList = "createAt")
})
public class BroadcastBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(nullable = false)
    protected Long parentId;


    @Column(nullable = false, length = 80)
    protected String boardName;

    @Column(nullable = false, length = 5)
    protected String writeAuth;

    @Column(nullable = false, length = 5)
    protected String ufileAuth;

    @Column(nullable = false, length = 3)
    protected String repleUse;


    @Column(nullable = false, length = 5)
    protected String downLimit;


    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;


    @Builder
    public BroadcastBoard(Long parentId,
                          String boardName,
                          String writeAuth,
                          String ufileAuth,
                          String repleUse,
                          String downLimit,
                          LocalDateTime createAt,
                          String actType,
                          Long actId) {

        this.parentId = parentId;
        this.boardName = boardName;
        this.writeAuth = writeAuth;
        this.ufileAuth = ufileAuth;
        this.repleUse = repleUse;
        this.downLimit = downLimit;

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
