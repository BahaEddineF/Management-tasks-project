package com.alibou.security.task;

import com.alibou.security.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    Optional<Task> findByTitle(String title);

    void deleteByTitle(String title);

    List<Task> findByEmployeeEmail(String email);

    List<Task> findByProjectTitle(String email);

    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> countTasksByStatus();

    @Query("SELECT t.status, COUNT(t) FROM Task t JOIN t.project p WHERE p.manager.email = :email GROUP BY t.status")
    List<Object[]> findTasksByManagerEmailAndCountByStatus(@Param("email") String email);

    @Query("SELECT t.status, COUNT(t) FROM Task t JOIN t.employee e WHERE e.email = :email GROUP BY t.status")
    List<Object[]> findTasksByEmployeeEmailAndCountByStatus(@Param("email") String email);

}
