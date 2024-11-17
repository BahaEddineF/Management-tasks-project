package com.alibou.security.user.subclasses.employee;
import com.alibou.security.task.Task;
import com.alibou.security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Data
    @DiscriminatorValue("EMPLOYEE")
    public class Employee extends User {

        @Enumerated(EnumType.STRING)
        private Title title;

        @OneToMany(mappedBy = "employee")
        private List<Task> tasks;


        public Title getTitle(){
            return title;
        }
        public void setTitle(Title title){
            this.title = title;
        }

    }


