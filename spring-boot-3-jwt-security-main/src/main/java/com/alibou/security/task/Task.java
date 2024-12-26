package com.alibou.security.task;

import com.alibou.security.file.FileDB;
import com.alibou.security.project.Project;
import com.alibou.security.status.Status;
import com.alibou.security.user.subclasses.employee.Employee;
import com.alibou.security.user.subclasses.manager.Manager;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false, unique = true)
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date start_date;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date end_date;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<FileDB> files= new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
