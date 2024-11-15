package com.alibou.security.project;

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




    public void deleteProjectById(Integer id){
        projectRepository.deleteById(id);
    }
    public void deleteProjectByTitle(String title){
        projectRepository.deleteByTitle(title);
    }

    public List<Project> getProjectsByManagerId(Integer managerId) {
        return projectRepository.findByManagerId(managerId);
    }


}
