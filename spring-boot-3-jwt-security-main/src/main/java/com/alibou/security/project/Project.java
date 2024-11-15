package com.alibou.security.project;


import com.alibou.security.status.Status;
import com.alibou.security.user.subclasses.manager.Manager;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data


public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false, unique = true)
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private Date start_date;
    @NotNull
    private Date end_date;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;


    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

}
