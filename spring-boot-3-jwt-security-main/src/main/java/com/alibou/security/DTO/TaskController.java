package com.alibou.security.DTO;

import com.alibou.security.task.Task;
import com.alibou.security.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/task")
public class TaskController {

    @Autowired
    TaskService taskService;
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


}
