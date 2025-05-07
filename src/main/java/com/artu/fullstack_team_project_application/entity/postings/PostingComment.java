package com.artu.fullstack_team_project_application.entity.postings;

import com.artu.fullstack_team_project_application.entity.users.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@ToString
@SQLDelete(sql = "UPDATE posting_comments SET is_used = false WHERE comment_id = ?")
@Where(clause = "is_used = true")
@Table(name = "posting_comments")
public class PostingComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Integer commentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"password", "userBirth", "gender", "createdAt", "isUsed", "dropoutAt", "events", "hibernateLazyInitializer", "handler"})
//    @JsonBackReference
    @ToString.Exclude
    private User user;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "post_id",insertable = false, updatable = false)
    @JsonBackReference
    @ToString.Exclude
    private Posting post;

    @Lob
    @Column(name = "contents", nullable = false)
    private String contents;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("1")
    @Column(name = "is_used")
    private Boolean isUsed=true;

    @ColumnDefault("1")
    @Column(name = "is_deleted")
    private Boolean isDeleted=false;


}