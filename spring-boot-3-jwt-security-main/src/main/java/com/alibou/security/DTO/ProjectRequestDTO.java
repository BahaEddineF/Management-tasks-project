package com.alibou.security.DTO;

import com.alibou.security.project.Project;
import com.alibou.security.status.Status;
import com.alibou.security.user.subclasses.Manager;
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

    public static ProjectRequestDTO mapper(Project project){
        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setStatus(project.getStatus());
        dto.setManager(UserRequestDTO.mapper(Optional.ofNullable(project.getManager())));
        dto.setStart_date(project.getStart_date());
        dto.setEnd_date(project.getEnd_date());
        return dto;
    }
}
