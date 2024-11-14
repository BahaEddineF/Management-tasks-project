package com.alibou.security.user.subclasses;


import com.alibou.security.project.Project;
import com.alibou.security.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MANAGER")
public class Manager extends User {

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Project> projects;
}
