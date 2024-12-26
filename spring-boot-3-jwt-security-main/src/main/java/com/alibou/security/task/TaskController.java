package com.alibou.security.task;

import com.alibou.security.DTO.TaskRequestDTO;
import com.alibou.security.file.FileDB;
import com.alibou.security.file.FileStrorageService;
import com.alibou.security.file.message.ResponseFile;
import com.alibou.security.file.message.ResponseMessage;
import com.alibou.security.project.Project;
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
@RequestMapping("api/v1/task")
public class TaskController {

    @Autowired
    TaskService taskService;
    @Autowired
    FileStrorageService fileStrorageService;
    @PostMapping("/save")
    public ResponseEntity<TaskRequestDTO> save2(@RequestBody Task task){
        return new ResponseEntity<TaskRequestDTO>(TaskRequestDTO.mapper(Optional.ofNullable(taskService.createTask(task))), HttpStatus.CREATED);

    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskRequestDTO>> getall(){
        List<Task> tasks = taskService.getAllTasks();
        List<TaskRequestDTO> dtos = new ArrayList<>();
        for(Task task : tasks){
            dtos.add(TaskRequestDTO.mapper(Optional.ofNullable(task)));
        }
        if(tasks.isEmpty()){
            return new ResponseEntity<List<TaskRequestDTO>>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<List<TaskRequestDTO>>(dtos, HttpStatus.OK);
        }
    }

    @GetMapping("{id}")
    public  ResponseEntity<TaskRequestDTO> getById(@PathVariable Integer id){
        return new ResponseEntity<TaskRequestDTO>(TaskRequestDTO.mapper(Optional.ofNullable(taskService.getTaskById(id))),HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public  ResponseEntity<TaskRequestDTO> getByTitle(@PathVariable String title){
        return new ResponseEntity<TaskRequestDTO>(TaskRequestDTO.mapper(Optional.ofNullable(taskService.getTaskByTitle(title))),HttpStatus.OK);
    }



    @PutMapping("{id}")
    public  ResponseEntity<TaskRequestDTO> updateById(@PathVariable Integer id,@RequestBody Task task){
        return new ResponseEntity<TaskRequestDTO>(TaskRequestDTO.mapper(Optional.ofNullable(taskService.updateTaskById(id, task))),HttpStatus.OK);
    }

    @PutMapping("/title/{title}")
    public  ResponseEntity<TaskRequestDTO> updateByTitle(@PathVariable String title,@RequestBody Task task){
        return new ResponseEntity<TaskRequestDTO>(TaskRequestDTO.mapper(Optional.ofNullable(taskService.updateTaskByTitle(title, task))),HttpStatus.OK);
    }
    @PutMapping("/title/foremployee/{title}")
    public  ResponseEntity<TaskRequestDTO> updateByTitleForManager(@PathVariable String title,@RequestBody Task task){
        return new ResponseEntity<TaskRequestDTO>(TaskRequestDTO.mapper(Optional.ofNullable(taskService.updateTaskByTitleForEmployee(title, task))),HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public  ResponseEntity<?> deleteById(@PathVariable Integer id){
        taskService.deleteTaskById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/title/{title}")
    public  ResponseEntity<?> deleteByTitle(@PathVariable String title){
        taskService.deleteTaskByTitle(title);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/employee/{employeeEmail}")
    public ResponseEntity<List<TaskRequestDTO>> getTasksByEmployee(@PathVariable String employeeEmail) {
        List<Task> tasks= taskService.getTasksByEmployeeEmail(employeeEmail);
        List<TaskRequestDTO> dtos = new ArrayList<>();
        for(Task task : tasks){
            dtos.add(TaskRequestDTO.mapper(Optional.ofNullable(task)));
        }
        if(tasks.isEmpty()){
            return new ResponseEntity<List<TaskRequestDTO>>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<List<TaskRequestDTO>>(dtos, HttpStatus.OK);
        }
    }

    @GetMapping("/project/{projectTitle}")
    public ResponseEntity<List<TaskRequestDTO>> getTasksByProject(@PathVariable String projectTitle) {
        List<Task> tasks= taskService.getTasksByProjectTitle(projectTitle);
        List<TaskRequestDTO> dtos = new ArrayList<>();
        for(Task task : tasks){
            dtos.add(TaskRequestDTO.mapper(Optional.ofNullable(task)));
        }
        if(tasks.isEmpty()){
            return new ResponseEntity<List<TaskRequestDTO>>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<List<TaskRequestDTO>>(dtos, HttpStatus.OK);
        }
    }


    @PostMapping("/upload/{id}")
    public ResponseEntity<ResponseMessage> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable Integer id) {
        String message = "";
        try {
            // Retrieve the project by ID
            Task task = taskService.getTaskById(id);

            // Store the file
            FileDB storedFile = fileStrorageService.store(file);

            // Associate the file with the project
            storedFile.setTask(task);
            task.getFiles().add(storedFile);

            // Save the project with the new file
            taskService.createTask(task);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    @GetMapping("/files/{id}")
    public ResponseEntity<List<ResponseFile>> getListFiles(@PathVariable Integer id) {
        // Retrieve the project by ID
        Task task = taskService.getTaskById(id);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList()); // Return empty list if project not found
        }

        // Filter files belonging to the specified project
        List<ResponseFile> files = fileStrorageService.getAllFiles().filter(dbFile -> {
                    // Check if dbFile's project is not null and its ID matches the project ID
                    return dbFile.getTask() != null && dbFile.getTask().getId().equals(id);
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
}
