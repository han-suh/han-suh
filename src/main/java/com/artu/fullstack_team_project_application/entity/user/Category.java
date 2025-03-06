package com.artu.fullstack_team_project_application.entity.user;

import com.artu.fullstack_team_project_application.entity.events.events.Event;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "ctgr_id", nullable = false)
    private Byte id;

    @Column(name = "ctgr_name", nullable = false, length = 50)
    private String ctgrName;

    @Column(name = "is_used_main", nullable = false)
    private Boolean isUsedMain = false;

    @Column(name = "is_used_oneday", nullable = false)
    private Boolean isUsedOneday = false;

    @OneToMany(mappedBy = "ctgr")
    private Set<Event> events = new LinkedHashSet<>();

    @OneToMany(mappedBy = "ctgr")
    private Set<UserInterest> userInterests = new LinkedHashSet<>();

}