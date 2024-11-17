package com.alibou.security.DTO;

import com.alibou.security.project.Project;
import com.alibou.security.status.Status;
import com.alibou.security.task.Task;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskRequestDTO {
    private Integer id;
    private String title;
    private String description;
    private Date start_date;
    private Date end_date;
    private ProjectRequestDTO project;
    private UserRequestDTO employee;

    @Enumerated(EnumType.STRING)
    private Status status;

    public static TaskRequestDTO mapper(Task task){
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStart_date(task.getStart_date());
        dto.setEnd_date(task.getEnd_date());
        dto.setStatus(task.getStatus());
        dto.setEmployee(UserRequestDTO.mapperEmployee(Optional.ofNullable(task.getEmployee())));
        dto.setProject(ProjectRequestDTO.mapper(task.getProject()));

        return dto;
    }
}
