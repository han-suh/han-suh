package com.artu.fullstack_team_project_application.service.users;

import com.artu.fullstack_team_project_application.dto.UserPageDto;
import com.artu.fullstack_team_project_application.dto.users.LoginRequestDto;
import com.artu.fullstack_team_project_application.entity.postings.UserFollow;
import com.artu.fullstack_team_project_application.entity.postings.UserFollowId;
import com.artu.fullstack_team_project_application.entity.users.user.User;
import com.artu.fullstack_team_project_application.entity.users.user.UserImg;
import com.artu.fullstack_team_project_application.entity.users.user.UserInterest;
import com.artu.fullstack_team_project_application.repository.postings.PostingLikeRepository;
import com.artu.fullstack_team_project_application.repository.postings.PostingRepository;
import com.artu.fullstack_team_project_application.repository.postings.UserFollowRepository;
import com.artu.fullstack_team_project_application.repository.users.UserImageRepository;
import com.artu.fullstack_team_project_application.repository.users.UserRepository;
import com.artu.fullstack_team_project_application.service.postings.PostingService;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;
    private final PostingRepository postingRepository;
    private final PostingLikeRepository postingLikeRepository;
    private final UserImageRepository userImageRepository;
    private final EntityManager entityManager;

    @Override
    public Page<User> readAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> readOne(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(String userId) {
    }

    @Override
    public boolean checkUserExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public boolean checkUserEmailExists(String email) {
        return userRepository.existsByUserEmail(email);
    }

    @Override
    @Transactional
    public void updatePassword(String userId, String newPw) {

        // 영속성 컨텍스트에서 해당 사용자 찾기
        User existingUser = entityManager.find(User.class, userId);
        if (existingUser == null) {
            throw new IllegalArgumentException("사용자가 존재하지 않음");
        }

        existingUser.setPassword(BCrypt.hashpw(newPw, BCrypt.gensalt()));  // 새로운 비밀번호로 변경
        entityManager.merge(existingUser);         // 엔티티 업데이트 (merge)
    }

    @Override
    public Optional<User> loginHashCheck(LoginRequestDto userDto) {
        Optional<User> userOpt = userRepository.findById(userDto.getId());

        if (userOpt.isPresent()) {
            if (BCrypt.checkpw(userDto.getPw(), userOpt.get().getPassword())) {
                return userOpt;
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public UserPageDto readUserPage(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        UserPageDto userPageDto = new UserPageDto();
        if (userOptional.isPresent()) {
            Long countFollower = userFollowRepository.countFolloweeByUserId(userId);
            Long countFollowee = userFollowRepository.countFollowerByUserId(userId);
            Long countPosting = postingRepository.countpostingByUserId(userId);
            Long countPostingLike = postingLikeRepository.countPostingLikes(userId);
            userPageDto.setUser(userOptional.get());
            userPageDto.setCountFollower(countFollower);
            userPageDto.setCountFollowee(countFollowee);
            userPageDto.setCountPosting(countPosting);
            userPageDto.setCountLike(countPostingLike);
            return userPageDto;
        }

        return null;
    }


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UserFollow> findByFollowerId(String followerId) {
        return userFollowRepository.findByFollowerIdAndIsUsedTrue(followerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UserFollow> findByFolloweeId(String followeeId) {
        return userFollowRepository.findByFolloweeIdAndIsUsedTrue(followeeId);
    }

//    @Override
//    public void registerFollow(String followerId, String followeeId) {
//        UserFollow userFollow = new UserFollow();
//        userFollow.setFollowerId(followerId);
//        userFollow.setFolloweeId(followeeId);
//        UserFollowId userFollowId = new UserFollowId();
//        userFollowId.setFollowerId(followerId);
//        userFollowId.setFolloweeId(followeeId);
//        userFollowRepository.save(userFollow);
//    }

    @Override
    public void registerFollow(String followerId, String followeeId) {
//        User follower = userRepository.findById(followerId).orElseThrow();
//        User followee = userRepository.findById(followeeId).orElseThrow();

        UserFollow userFollow = new UserFollow();
        userFollow.setFollowerId(followerId);
        userFollow.setFolloweeId(followerId);
        //userFollow.setFollowers(follower);
        //userFollow.setFollowees(followee);
        userFollow.setFollowedAt(Instant.now());
        userFollow.setIsUsed(true);
        userFollowRepository.save(userFollow);
    }

//    @Override
//    public void removeFollow(String followerId, String followeeId) {
//        UserFollow userFollow = new UserFollow();
//        userFollow.setFollowerId(followerId);
//        userFollow.setFolloweeId(followeeId);
//        UserFollowId userFollowId = new UserFollowId();
//        userFollowId.setFollowerId(followerId);
//        userFollowId.setFolloweeId(followeeId);
//        if (userFollowRepository.existsById(userFollowId)) {
//            userFollowRepository.deleteById(userFollowId);
//        }
//    }

    @Override
    public void removeFollow(String followerId, String followeeId) {
        Optional<UserFollow> userFollowOptional = userFollowRepository.findByFollowerIdAndFolloweeId(followerId, followeeId);

        if(userFollowOptional.isPresent()) {
            UserFollow userFollow = userFollowOptional.get();
            userFollow.setIsUsed(false);
            userFollow.setFollowedAt(null);
            userFollowRepository.save(userFollow);
        }else {
            throw new IllegalStateException("팔로우 없음");
        }
    }

    @Override
    public Set<UserImg> findUserImgByUserId(String userId) {
        return userImageRepository.findUserImgByUser_UserId(userId);
    }

}
