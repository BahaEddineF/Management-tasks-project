package com.alibou.security.project;

import com.alibou.security.file.FileDB;
import com.alibou.security.file.FileStrorageService;
import com.alibou.security.file.message.ResponseFile;
import com.alibou.security.file.message.ResponseMessage;
import com.alibou.security.task.ProjectRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;
    @Autowired
    FileStrorageService fileStrorageService;

    @PostMapping("/save")
    public ResponseEntity<ProjectRequestDTO> save2(@RequestBody Project project){

        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(Optional.ofNullable(projectService.createProject(project))), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectRequestDTO>> getall(){
        List<Project> projects = projectService.getAllProjects();
        List<ProjectRequestDTO> dtos = new ArrayList<>();
        for(Project project : projects){
            dtos.add(ProjectRequestDTO.mapper(Optional.ofNullable(project)));
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
        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(Optional.ofNullable(projectService.getProjectById(id))),HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public  ResponseEntity<ProjectRequestDTO> getByTitle(@PathVariable String title){
        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(Optional.ofNullable(projectService.getProjectByTitle(title))),HttpStatus.OK);
    }



    @PutMapping("{id}")
    public  ResponseEntity<ProjectRequestDTO> updateById(@PathVariable Integer id,@RequestBody Project project){
        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(Optional.ofNullable(projectService.updateProjectById(id, project))),HttpStatus.OK);
    }

    @PutMapping("/title/{title}")
    public  ResponseEntity<ProjectRequestDTO> updateByTitle(@PathVariable String title,@RequestBody Project project){
        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(Optional.ofNullable(projectService.updateProjectByTitle(title, project))),HttpStatus.OK);
    }
    @PutMapping("/title/formanager/{title}")
    public  ResponseEntity<ProjectRequestDTO> updateByTitleForManager(@PathVariable String title,@RequestBody Project project){
        return new ResponseEntity<ProjectRequestDTO>(ProjectRequestDTO.mapper(Optional.ofNullable(projectService.updateProjectByTitleForManager(title, project))),HttpStatus.OK);
    }
    @PostMapping("/upload/{id}")
    public ResponseEntity<ResponseMessage> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable Integer id) {
        String message = "";
        try {
            // Retrieve the project by ID
            Project project = projectService.getProjectById(id);

            // Store the file
            FileDB storedFile = fileStrorageService.store(file);

            // Associate the file with the project
            storedFile.setProject(project);
            project.getFiles().add(storedFile);

            // Save the project with the new file
            projectService.createProject(project);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    @GetMapping("/filess/{id}")
    public ResponseEntity<List<ResponseFile>> getListFiles(@PathVariable Integer id) {
        // Retrieve the project by ID
        Project project = projectService.getProjectById(id);

        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList()); // Return empty list if project not found
        }

        // Filter files belonging to the specified project
        List<ResponseFile> files = fileStrorageService.getAllFiles().filter(dbFile -> {
                    // Check if dbFile's project is not null and its ID matches the project ID
                    return dbFile.getProject() != null && dbFile.getProject().getId().equals(id);
                })
                .map(dbFile -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/api/v1/files/")
                            .path(dbFile.getId().toString())
                            .toUriString();

                    return new ResponseFile(
                            dbFile.getName(),
                            fileDownloadUri,
                            dbFile.getType(),
                            dbFile.getData().length);
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
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
            dtos.add(ProjectRequestDTO.mapper(Optional.ofNullable(project)));
        }
        if(projects.isEmpty()){
            return new ResponseEntity<List<ProjectRequestDTO>>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<List<ProjectRequestDTO>>(dtos, HttpStatus.OK);
        }
    }




}
