package com.artu.fullstack_team_project_application.controller;

import com.artu.fullstack_team_project_application.entity.postings.UserFollow;
import com.artu.fullstack_team_project_application.entity.users.user.User;
import com.artu.fullstack_team_project_application.service.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//@Controller
@RestController
@RequestMapping("/api/posting/{userId}")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@AllArgsConstructor
public class UserFollowController {
    private final UserService userService;

    @GetMapping("/follower.do")
    public ResponseEntity<Object> follower(@PathVariable String userId
//            @RequestParam(required = false) String followerId,
//            @RequestParam(required = false) String followeeId,
//           Model model
    ) {
        Set<UserFollow> findByFollowerId = userService.findByFollowerId(userId);
//        Set<UserFollow> findByFolloweeId = userService.findByFolloweeId(followeeId);
        Optional<User> userOptional = userService.findByUserId(userId);
        User user = userOptional.get();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user", user);
        responseData.put("findByFollowerId", findByFollowerId);
//        responseData.put("findByFolloweeId", findByFolloweeId);
//        System.out.println("Follow size = " + findByFollowerId.size());
        return ResponseEntity.ok(responseData);
    }

//    @GetMapping("/follower.do")
//    public String follower(
//            @PathVariable String userId,
//            @RequestParam(required = false) String followerId,
//            @RequestParam(required = false) String followeeId,
//            Model model
//    ) {
//        Set<UserFollow> findByFollowerId = userService.findByFollowerId(followerId);
//        Set<UserFollow> findByFolloweeId = userService.findByFolloweeId(followeeId);
//        Optional<User> userOptional = userService.findByUserId(userId);
//
//
//        User user = userOptional.get();
//        model.addAttribute("user", user);
//        model.addAttribute("findByFollowerId", findByFollowerId);
//        model.addAttribute("findByFolloweeId", findByFolloweeId);
//        return "posting/follower"; // follower.html로 이동
//    }


    @GetMapping("/followee.do")
    public ResponseEntity<Object> followee(@PathVariable String userId
//               @RequestParam(required = false) String followerId,
//               @RequestParam(required = false) String followeeId,
//               Model model
    ) {
//        Set<UserFollow> findByFollowerId = userService.findByFollowerId(followerId);
        Set<UserFollow> findByFolloweeId = userService.findByFolloweeId(userId);

        Optional<User> userOptional = userService.findByUserId(userId);

        User user = userOptional.get();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user", user);
//        responseData.put("findByFollowerId", findByFollowerId);
        responseData.put("findByFolloweeId", findByFolloweeId);
        return ResponseEntity.ok(responseData);
    }

//    @GetMapping("/followee.do")
//    public String followee(
//            @PathVariable String userId,
//            @RequestParam(required = false) String followeeId,
//            @RequestParam(required = false) String followerId,
//            Model model
//    ) {
//        Set<UserFollow> findByFollowerId = userService.findByFollowerId(followerId);
//        Set<UserFollow> findByFolloweeId = userService.findByFolloweeId(followeeId);
//        Optional<User> userOptional = userService.findByUserId(userId);
//
//
//        User user = userOptional.get();
//        model.addAttribute("user", user);
//        model.addAttribute("findByFollowerId", findByFollowerId);
//        model.addAttribute("findByFolloweeId", findByFolloweeId);
//        return "posting/followee"; // followee.html로 이동
//    }

    @PostMapping("/followee.do")
    public ResponseEntity<String> userFollowRegister(
            @RequestBody Map<String, String> request
    ) {
        System.out.println(request);
        String followerId = request.get("followerId");
        String followeeId = request.get("followeeId");
        userService.registerFollow(followerId,followeeId);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/followee.do")
    public ResponseEntity<String> userFollowerDelete(
            @RequestBody Map<String, String> body
    ) {
        System.out.println(body);
        String followerId = body.get("followerId");
        String followeeId = body.get("followeeId");

        userService.removeFollow( followerId,followeeId);
        return ResponseEntity.ok("Success");
    }


}
