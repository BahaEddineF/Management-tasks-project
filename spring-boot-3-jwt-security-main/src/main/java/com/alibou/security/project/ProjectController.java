package com.alibou.security.project;

import com.alibou.security.DTO.ProjectRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PostMapping("/save")
    public ResponseEntity<ProjectRequestDTO> save2(@RequestBody Project project){

        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(projectService.createProject(project)), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectRequestDTO>> getall(){
        List<Project> projects = projectService.getAllProjects();
        List<ProjectRequestDTO> dtos = new ArrayList<>();
        for(Project project : projects){
            dtos.add(ProjectRequestDTO.mapper(project));
        }
        if(projects.isEmpty()){
            return new ResponseEntity<List<ProjectRequestDTO>>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<List<ProjectRequestDTO>>(dtos, HttpStatus.OK);
        }
    }

    @GetMapping("/no/{id}")
    public  ResponseEntity<Project> getByIdNoDTO(@PathVariable Integer id){
        return new ResponseEntity<Project>(projectService.getProjectById(id),HttpStatus.OK);
    }
    @GetMapping("{id}")
    public  ResponseEntity<ProjectRequestDTO> getById(@PathVariable Integer id){
        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(projectService.getProjectById(id)),HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public  ResponseEntity<ProjectRequestDTO> getByTitle(@PathVariable String title){
        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(projectService.getProjectByTitle(title)),HttpStatus.OK);
    }



    @PutMapping("{id}")
    public  ResponseEntity<ProjectRequestDTO> updateById(@PathVariable Integer id,@RequestBody Project project){
        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(projectService.updateProjectById(id,project)),HttpStatus.OK);
    }

    @PutMapping("/title/{title}")
    public  ResponseEntity<ProjectRequestDTO> updateByTitle(@PathVariable String title,@RequestBody Project project){
        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(projectService.updateProjectByTitle(title,project)),HttpStatus.OK);
    }
    @PutMapping("/title/foranager/{title}")
    public  ResponseEntity<ProjectRequestDTO> updateByTitleForManager(@PathVariable String title,@RequestBody Project project){
        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(projectService.updateProjectByTitleForManager(title,project)),HttpStatus.OK);
    }




    @DeleteMapping("{id}")
    public  ResponseEntity<?> deleteById(@PathVariable Integer id){
        projectService.deleteProjectById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/title/{title}")
    public  ResponseEntity<?> deleteByTitle(@PathVariable String title){
        projectService.deleteProjectByTitle(title);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/manager/{managerEmail}")
    public ResponseEntity<List<ProjectRequestDTO>> getProjectsByManager(@PathVariable String managerEmail) {
        List<Project> projects= projectService.getProjectsByManagerEmail(managerEmail);
        List<ProjectRequestDTO> dtos = new ArrayList<>();
        for(Project project : projects){
            dtos.add(ProjectRequestDTO.mapper(project));
        }
        if(projects.isEmpty()){
            return new ResponseEntity<List<ProjectRequestDTO>>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<List<ProjectRequestDTO>>(dtos, HttpStatus.OK);
        }
    }




}
