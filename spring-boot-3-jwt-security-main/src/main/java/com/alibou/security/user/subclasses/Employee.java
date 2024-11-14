package com.alibou.security.user.subclasses;

import com.alibou.security.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Data
    @DiscriminatorValue("EMPLOYEE")
    public class Employee extends User {

        @Enumerated(EnumType.STRING)
        private Title title;

//        @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
//        private List<Task> tasks = new ArrayList<>();


        public Title getTitle(){
            return title;
        }
        public void setTitle(Title title){
            this.title = title;
        }

    }


