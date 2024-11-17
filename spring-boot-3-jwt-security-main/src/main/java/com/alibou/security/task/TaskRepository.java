package com.alibou.security.task;

import com.alibou.security.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    Optional<Task> findByTitle(String title);

    void deleteByTitle(String title);

    List<Task> findByEmployeeEmail(String email);

    List<Task> findByProjectTitle(String email);
}
