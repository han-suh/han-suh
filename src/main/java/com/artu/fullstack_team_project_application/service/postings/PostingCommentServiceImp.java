package com.artu.fullstack_team_project_application.service.postings;

import com.artu.fullstack_team_project_application.entity.postings.Posting;
import com.artu.fullstack_team_project_application.entity.postings.PostingComment;
import com.artu.fullstack_team_project_application.entity.users.user.User;
import com.artu.fullstack_team_project_application.repository.postings.PostingCommentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class PostingCommentServiceImp implements PostingCommentService{
    private final PostingCommentsRepository postingCommentsRepository;

    @Override
    @Transactional
    public PostingComment save(PostingComment postingComment) {
            return postingCommentsRepository.save(postingComment);
    }

    @Override
    public PostingComment delete(Integer commentId) {
        PostingComment postingComment = postingCommentsRepository.findById(commentId).orElseThrow(()->new NoSuchElementException("없음"));
        postingCommentsRepository.delete(postingComment);
        return postingComment;
    }

    @Override
    public Optional<PostingComment> findByCommentId(Integer commentId) {
        return postingCommentsRepository.findById(commentId);
    }

    @Override
    public Set<PostingComment> findByUser_UserId(String userId) {
        return postingCommentsRepository.findByUser_UserId(userId);
    }

    @Override
    public Set<PostingComment> findCommentsByPost_PostId(Integer postId) {
        return postingCommentsRepository.findCommentsByPost_PostId(postId);
    }





}
