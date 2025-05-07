package com.artu.fullstack_team_project_application.dto;

import com.artu.fullstack_team_project_application.entity.postings.Posting;
import com.artu.fullstack_team_project_application.entity.postings.PostingComment;
import com.artu.fullstack_team_project_application.entity.users.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostingCommentsDto {
    private String contents;
    private Integer postId;
    private String userId;
}
