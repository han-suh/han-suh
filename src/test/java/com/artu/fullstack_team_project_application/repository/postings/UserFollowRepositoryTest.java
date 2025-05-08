package com.artu.fullstack_team_project_application.repository.postings;

import com.artu.fullstack_team_project_application.entity.postings.UserFollow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserFollowRepositoryTest {
    @Autowired
    private UserFollowRepository userFollowRepository;

    @Test
    @Transactional
    void findByFollowerId() {
        System.out.println(userFollowRepository.findByFollowerIdAndIsUsedTrue("user1001"));
    }

    @Test
    @Transactional
    void findByFolloweeId() {
        System.out.println(userFollowRepository.findByFolloweeIdAndIsUsedTrue("user1001"));
    }

    @Test
    void countFolloweeByUserId() {
        System.out.println(userFollowRepository.countFolloweeByUserId("user1001"));
    }

    @Test
    void countFollowerByUserId() {
        System.out.println(userFollowRepository.countFollowerByUserId("user1001"));
    }

    @Test
    @Transactional
    void findByFollowerIdAndFolloweeId() {
        Optional<UserFollow> userFollow = userFollowRepository.findByFollowerIdAndFolloweeId("user1001","user1002");
        if(userFollow.isPresent()){
            System.out.println(userFollow.get());
        }
    }
}