package com.artu.fullstack_team_project_application.repository.widgets;

import com.artu.fullstack_team_project_application.entity.widgets.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface WidgetRepository extends JpaRepository<Widget, Integer> {
}




