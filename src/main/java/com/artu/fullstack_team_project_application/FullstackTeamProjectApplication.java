package com.artu.fullstack_team_project_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySources(@PropertySource("classpath:env.properties"))
@SpringBootApplication(scanBasePackages = "com.artu")
public class FullstackTeamProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FullstackTeamProjectApplication.class, args);
    }

}
