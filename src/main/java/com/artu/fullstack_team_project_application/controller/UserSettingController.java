package com.artu.fullstack_team_project_application.controller;

import com.artu.fullstack_team_project_application.dto.UserSettingDto;
import com.artu.fullstack_team_project_application.entity.users.user.User;
import com.artu.fullstack_team_project_application.entity.users.user.UserSetting;
import com.artu.fullstack_team_project_application.service.users.UserService;
import com.artu.fullstack_team_project_application.service.users.UserSettingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

//@Controller
//@RequestMapping("/api/posting/{userId}")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

public class UserSettingController {
    private final UserSettingService userSettingService;
    private final UserService userService;

    @GetMapping("/api/posting/{userId}/setting.do")
    public ResponseEntity<UserSettingDto> getSetting(
            @PathVariable String userId
    ){
        Optional<UserSetting> findOne = userSettingService.findOne(userId);
        Optional<User> userOptional = userService.findByUserId(userId);
        User user = userOptional.orElseThrow(() -> new RuntimeException("없음"));
        UserSetting userSetting = findOne.orElse(new UserSetting());
        userSetting.setUser(user);

        UserSettingDto userSettingDto = new UserSettingDto(
                userSetting.getSettingId(),
                user.getUserId(),
                user.getUserName(),
                user.getUserEmail(),
                userSetting.getDisplayColor() != null ? userSetting.getDisplayColor().name() : null,
                userSetting.getLanguage() != null ? userSetting.getLanguage().name() : null,
                userSetting.getSetAt()
        );
        return ResponseEntity.ok(userSettingDto);
    }

    @PutMapping("/api/posting/{userId}/setting.do")
    public ResponseEntity<UserSetting> saveUserSetting(
            @PathVariable String userId,
            @RequestBody UserSettingDto userSettingDto
    ){
        User user = userService.findByUserId(userId).orElseThrow(() -> new RuntimeException("없음"));
        UserSetting setting = userSettingService.findOne(userId).orElse(new UserSetting());

        if (userSettingDto.getSettingId() != null) {
            setting.setSettingId(userSettingDto.getSettingId());
        }
        user.setUserName(userSettingDto.getUserName());
        user.setUserEmail(userSettingDto.getUserEmail());
        setting.setUser(user);
        setting.setDisplayColor(UserSetting.DisplayColor.valueOf(userSettingDto.getDisplayColor()));
        setting.setLanguage(UserSetting.Language.valueOf(userSettingDto.getLanguage()));
        setting.setSetAt(Instant.now());
        userSettingService.save(setting);

        return ResponseEntity.ok(setting);
    }
}
