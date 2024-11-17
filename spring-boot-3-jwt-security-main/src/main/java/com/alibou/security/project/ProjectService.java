package com.alibou.security.project;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public Project createProject(Project project){
        return projectRepository.save(project);
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

    public Project updateProjectById(Integer id, Project updatedProject) {
        Project existingProject = projectRepository.findById(id).get();
        existingProject.setTitle(updatedProject.getTitle());
        existingProject.setDescription(updatedProject.getDescription());
        existingProject.setStart_date(updatedProject.getStart_date());
        existingProject.setEnd_date(updatedProject.getEnd_date());
        existingProject.setManager(updatedProject.getManager());
        existingProject.setStatus(updatedProject.getStatus());
        return projectRepository.save(existingProject);
    }
    @Transactional
    public Project updateProjectByTitle(String title, Project updatedProject) {
        Project existingProject = projectRepository.findByTitle(title).get();
        existingProject.setTitle(updatedProject.getTitle());
        existingProject.setDescription(updatedProject.getDescription());
        existingProject.setStart_date(updatedProject.getStart_date());
        existingProject.setEnd_date(updatedProject.getEnd_date());
        existingProject.setManager(updatedProject.getManager());
        existingProject.setStatus(updatedProject.getStatus());
        return projectRepository.save(existingProject);
    }
    @Transactional
    public Project updateProjectByTitleForManager(String title, Project updatedProject) {
        Project existingProject = projectRepository.findByTitle(title).get();
        existingProject.setStatus(updatedProject.getStatus());
        return projectRepository.save(existingProject);
    }



    @Transactional
    public void deleteProjectById(Integer id){
        projectRepository.deleteById(id);
    }
    @Transactional
    public void deleteProjectByTitle(String title){
        projectRepository.deleteByTitle(title);
    }
    @Transactional
    public List<Project> getProjectsByManagerEmail(String email) {
        return projectRepository.findByManagerEmail(email);
    }


}
