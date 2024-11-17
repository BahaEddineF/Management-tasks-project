package com.alibou.security.task;

import com.alibou.security.DTO.UserRequestDTO;
import com.alibou.security.project.Project;
import com.alibou.security.status.Status;
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
public class ProjectRequestDTO {

    private Integer id;
    private String title;
    private String description;
    private Date start_date;
    private Date end_date;
    private UserRequestDTO manager;

    @Enumerated(EnumType.STRING)
    private Status status;

    public static ProjectRequestDTO mapper(Optional<Project> project) {
        if (project.isEmpty()) {
            return null; // Or throw an exception based on your requirements
        }

        ProjectRequestDTO dto = new ProjectRequestDTO();
        Project projectValue = project.get();
        dto.setId(projectValue.getId());
        dto.setTitle(projectValue.getTitle());
        dto.setDescription(projectValue.getDescription());
        dto.setStatus(projectValue.getStatus());
        dto.setManager(UserRequestDTO.mapper(Optional.ofNullable(projectValue.getManager())));
        dto.setStart_date(projectValue.getStart_date());
        dto.setEnd_date(projectValue.getEnd_date());
        return dto;
    }
}
