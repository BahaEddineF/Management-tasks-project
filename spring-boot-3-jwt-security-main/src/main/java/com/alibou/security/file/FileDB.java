package com.alibou.security.file;

import com.alibou.security.project.Project;
import com.alibou.security.task.Task;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class FileDB {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String type;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = true)
    private Task task;

    public FileDB() {
    }

    public FileDB(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

}
