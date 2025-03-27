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
        @Index(name = "index_broadcast_file_parentId", columnList = "parentId"),
        @Index(name = "index_broadcast_file_ext", columnList = "ext"),
        @Index(name = "index_broadcast_file_status", columnList = "status"),
        @Index(name = "index_broadcast_file_createAt", columnList = "createAt")
})
public class BroadcastFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(nullable = false)
    protected Long parentId;

    @Column(nullable = false, length = 100)
    protected String name;

    @Column(nullable = false, length = 20)
    protected String ext;

    @Column(nullable = false)
    protected String fileUrl;

    @Column(nullable = false, length = 8)
    protected String status;

    protected LocalDateTime createAt;

    @Builder
    public BroadcastFile(Long parentId,
                         String name,
                         String ext,
                         String status,
                         String fileUrl,
                         LocalDateTime createAt,
                         String actType,
                         Long actId) {

        this.parentId = parentId;
        this.name = name;
        this.ext = ext;
        this.status = status;
        this.fileUrl = fileUrl;
        LocalDateTime now = LocalDateTime.now();
        if (actType.equals("update")) {
            this.id = actId;
            this.createAt = (createAt == null) ? now : createAt;
        } else {
            this.createAt = now;
        }
    }
}
