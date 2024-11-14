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

    public Project updateProject(Project project){
        return projectRepository.saveAndFlush(project);
    }

    public void deleteProjectById(Integer id){
        projectRepository.deleteById(id);
    }
    public void deleteProjectByTitle(String title){
        projectRepository.deleteByTitle(title);
    }


}
