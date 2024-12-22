package com.alibou.security.project;

import com.alibou.security.emailSender.EmailService;
import com.alibou.security.user.subclasses.manager.Manager;
import com.alibou.security.user.subclasses.manager.ManagerRepository;
import com.alibou.security.user.subclasses.manager.ManagerService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ManagerService managerService;
    @Autowired
    EmailService emailService;


    @SneakyThrows
    public Project createProject(Project project){
        Project savedproject = projectRepository.save(project);
        Manager manager = managerService.getManagerById(project.getManager().getId());
        if (manager != null && manager.getEmail() != null) {
            String managerEmail = manager.getEmail();
            String managerFullName = manager.getFirstname()+' '+ manager.getLastname();
        emailService.sendNewProjectNotificationEmail(managerEmail,managerFullName,project);
        }
        return savedproject;
    }


    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    public Project getProjectById(Integer id){
        return projectRepository.findById(id).get();
    }


    public Project getProjectByTitle(String title){
        return projectRepository.findByTitle(title).get();
    }

    @SneakyThrows
    public Project updateProjectById(Integer id, Project updatedProject) {
        // Fetch existing project
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + id + " not found"));

        // Keep original title for email notifications
        String originalTitle = existingProject.getTitle();

        // Update project fields
        existingProject.setTitle(updatedProject.getTitle());
        existingProject.setDescription(updatedProject.getDescription());
        existingProject.setStart_date(updatedProject.getStart_date());
        existingProject.setEnd_date(updatedProject.getEnd_date());
        existingProject.setStatus(updatedProject.getStatus());

        // Check if manager is being updated
        Manager oldManager = existingProject.getManager();
        Manager newManager = updatedProject.getManager();
        existingProject.setManager(newManager); // Update manager field

        if (oldManager != null && newManager != null) {
            // Manager remains the same
            if (oldManager.getEmail().equals(newManager.getEmail())) {
                emailService.sendUpdateProjectNotificationEmail(
                        oldManager.getEmail(),
                        oldManager.getFirstname() + " " + oldManager.getLastname(),
                        existingProject,
                        originalTitle
                );
            }
            // Manager changed
            else {
                // Notify old manager
                emailService.sendNoLongerTheManagerEmail(
                        oldManager.getEmail(),
                        oldManager.getFirstname() + " " + oldManager.getLastname(),
                        originalTitle
                );

                // Notify new manager
                emailService.sendNewProjectNotificationEmail(
                        newManager.getEmail(),
                        newManager.getFirstname() + " " + newManager.getLastname(),
                        existingProject
                );
            }
        }

        // Save updated project
        return projectRepository.save(existingProject);
    }


    @SneakyThrows
    @Transactional
    public Project updateProjectByTitle(String title, Project updatedProject) {
        // Fetch the existing project by title
        Project existingProject = projectRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Project with title '" + title + "' not found"));

        // Keep original title for email notifications
        String originalTitle = existingProject.getTitle();

        // Update project fields
        existingProject.setTitle(updatedProject.getTitle());
        existingProject.setDescription(updatedProject.getDescription());
        existingProject.setStart_date(updatedProject.getStart_date());
        existingProject.setEnd_date(updatedProject.getEnd_date());
        existingProject.setStatus(updatedProject.getStatus());

        // Check if manager is being updated
        Manager oldManager = existingProject.getManager();
        Manager newManager = updatedProject.getManager();
        existingProject.setManager(newManager); // Update manager field

        if (oldManager != null && newManager != null) {
            // Manager remains the same
            if (oldManager.getEmail().equals(newManager.getEmail())) {
                emailService.sendUpdateProjectNotificationEmail(
                        oldManager.getEmail(),
                        oldManager.getFirstname() + " " + oldManager.getLastname(),
                        existingProject,
                        originalTitle
                );
            }
            // Manager changed
            else {
                // Notify old manager
                emailService.sendNoLongerTheManagerEmail(
                        oldManager.getEmail(),
                        oldManager.getFirstname() + " " + oldManager.getLastname(),
                        originalTitle
                );

                // Notify new manager
                emailService.sendNewProjectNotificationEmail(
                        newManager.getEmail(),
                        newManager.getFirstname() + " " + newManager.getLastname(),
                        existingProject
                );
            }
        }

        // Save updated project
        return projectRepository.save(existingProject);
    }


    @Transactional
    public Project updateProjectByTitleForManager(String title, Project updatedProject) {
        Project existingProject = projectRepository.findByTitle(title).get();
        existingProject.setStatus(updatedProject.getStatus());
        return projectRepository.save(existingProject);
    }



    @SneakyThrows
    @Transactional
    public void deleteProjectById(Integer id){
        // Fetch the existing project by title
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project with id '" + id + "' not found"));

        String originalTitle = existingProject.getTitle();
        Manager manager = existingProject.getManager();
        if (manager !=null){
            emailService.sendProjectDeletedEmailToManager(manager.getEmail(),manager.getFirstname()+" "+manager.getLastname(),originalTitle);
        }



        projectRepository.deleteById(id);
    }
    @SneakyThrows
    @Transactional
    public void deleteProjectByTitle(String title){

        // Fetch the existing project by title
        Project existingProject = projectRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Project with title '" + title + "' not found"));
        Manager manager = existingProject.getManager();
        if (manager !=null){
            emailService.sendProjectDeletedEmailToManager(manager.getEmail(),manager.getFirstname()+" "+manager.getLastname(),title);
        }





        projectRepository.deleteByTitle(title);
    }




    @Transactional
    public List<Project> getProjectsByManagerEmail(String email) {
        return projectRepository.findByManagerEmail(email);
    }

    public Map<String, Long> getProjectCountByStatus() {
        List<Object[]> result = projectRepository.countProjectsByStatus();
        return result.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(), // Convert status (Enum) to String
                        row -> (Long) row[1]      // Count as Long
                ));
    }

    public Map<String, Long> getProjectByManagerEmailAndCountByStatus(String email) {
        List<Object[]> result = projectRepository.ByManagerEmailAndCountByStatus(email);
        return result.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(), // Convert status (Enum) to String
                        row -> (Long) row[1]      // Count as Long
                ));
    }

}
