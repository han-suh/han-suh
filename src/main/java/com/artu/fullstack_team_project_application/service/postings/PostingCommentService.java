package com.artu.fullstack_team_project_application.service.postings;

import com.artu.fullstack_team_project_application.entity.postings.Posting;
import com.artu.fullstack_team_project_application.entity.postings.PostingComment;
import com.artu.fullstack_team_project_application.entity.users.user.User;

import java.util.Optional;
import java.util.Set;

public interface PostingCommentService {
    public PostingComment save(PostingComment postingComment);
    public PostingComment delete(Integer commentId);

    // 게시물 해당 댓글
//    Set<PostingComment> findByPost_PostId(Integer postId);

    // 해당 댓글 찾기
    Optional<PostingComment> findByCommentId(Integer commentId);

    // 유저의 댓글
    Set<PostingComment> findByUser_UserId(String userId);


    Set<PostingComment> findCommentsByPost_PostId(Integer postId);
}
