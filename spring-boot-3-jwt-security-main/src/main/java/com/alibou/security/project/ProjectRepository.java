package com.alibou.security.project;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByTitle(String title);

    void deleteByTitle(String title);


    List<Project> findByManagerId(Integer managerId);

    List<Project> findByManagerEmail(String email);

    @Query("SELECT p.status, COUNT(p) FROM Project p GROUP BY p.status")
    List<Object[]> countProjectsByStatus();

    @Query("SELECT p.status, COUNT(p) FROM Project p WHERE p.manager.email = :email GROUP BY p.status")
    List<Object[]> ByManagerEmailAndCountByStatus(@Param("email") String email);
}
