package com.alibou.security.DTO;

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

    public static TaskRequestDTO mapper(Optional<Task> task) {
        if (task.isEmpty()) {
            return null; // Or throw an exception based on your requirements
        }

        TaskRequestDTO dto = new TaskRequestDTO();
        Task taskValue = task.get();
        dto.setId(taskValue.getId());
        dto.setTitle(taskValue.getTitle());
        dto.setDescription(taskValue.getDescription());
        dto.setStart_date(taskValue.getStart_date());
        dto.setEnd_date(taskValue.getEnd_date());
        dto.setStatus(taskValue.getStatus());
        dto.setEmployee(UserRequestDTO.mapper(Optional.ofNullable(taskValue.getEmployee())));
        dto.setProject(ProjectRequestDTO.mapper(Optional.ofNullable(taskValue.getProject())));
        return dto;
    }
}
