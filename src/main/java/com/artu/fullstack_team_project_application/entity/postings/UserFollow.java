package com.artu.fullstack_team_project_application.entity.postings;

import com.artu.fullstack_team_project_application.entity.users.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@SQLDelete(sql = "UPDATE user_follow SET is_used = false WHERE followee_id = ? AND follower_id = ?")
@Where(clause = "is_used = true")
@Table(name = "user_follow")
@IdClass(UserFollowId.class)
public class UserFollow {
    // @EmbeddedId
    // private UserFollowId id;

    @Id
    @Column(name = "follower_id", nullable = false)
    private String followerId;

    @Id
    @Column(name = "followee_id", nullable = false)
    private String followeeId;

    // followee 팔로우당한 user
    //@MapsId("followeeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "follower_id",insertable = false, updatable = false)
    @JsonIgnoreProperties({"password", "userBirth", "gender", "createdAt", "isUsed", "dropoutAt", "events", "hibernateLazyInitializer", "handler"})
    private User followers;
//    @JsonIgnore
//    @JsonBackReference

    // follower 팔로원한 user
    // @MapsId("followerId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "followee_id",insertable = false, updatable = false)
    @JsonIgnoreProperties({"password", "userBirth", "gender", "createdAt", "isUsed", "dropoutAt", "events", "hibernateLazyInitializer", "handler"})
    private User followees;
//    @JsonIgnore
//    @JsonBackReference

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "followed_at")
    private Instant followedAt;

    @ColumnDefault("true")
    @Column(name = "is_used")
    private Boolean isUsed = true;
}