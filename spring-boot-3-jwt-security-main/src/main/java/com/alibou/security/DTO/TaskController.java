package com.alibou.security.DTO;

import com.alibou.security.task.Task;
import com.alibou.security.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/task")
public class TaskController {

    @Autowired
    TaskService taskService;
    
    @PostMapping("/save")
    public ResponseEntity<TaskRequestDTO> save2(@RequestBody Task task){
        try{
            TaskRequestDTO dto = TaskRequestDTO.mapper(taskService.createTask(task));
            System.out.println(dto);
            return new ResponseEntity<TaskRequestDTO>(dto, HttpStatus.CREATED);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskRequestDTO>> getall(){
        List<Task> tasks = taskService.getAllTasks();
        List<TaskRequestDTO> dtos = new ArrayList<>();
        for(Task task : tasks){
            dtos.add(TaskRequestDTO.mapper(task));
        }
        if(tasks.isEmpty()){
            return new ResponseEntity<List<TaskRequestDTO>>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<List<TaskRequestDTO>>(dtos, HttpStatus.OK);
        }
    }

    @GetMapping("{id}")
    public  ResponseEntity<TaskRequestDTO> getById(@PathVariable Integer id){
        return new ResponseEntity<TaskRequestDTO>(TaskRequestDTO.mapper(taskService.getTaskById(id)),HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public  ResponseEntity<TaskRequestDTO> getByTitle(@PathVariable String title){
        return new ResponseEntity<TaskRequestDTO>(TaskRequestDTO.mapper(taskService.getTaskByTitle(title)),HttpStatus.OK);
    }



    @PutMapping("{id}")
    public  ResponseEntity<TaskRequestDTO> updateById(@PathVariable Integer id,@RequestBody Task task){
        return new ResponseEntity<TaskRequestDTO>(TaskRequestDTO.mapper(taskService.updateTaskById(id,task)),HttpStatus.OK);
    }

    @PutMapping("/title/{title}")
    public  ResponseEntity<TaskRequestDTO> updateByTitle(@PathVariable String title,@RequestBody Task task){
        return new ResponseEntity<TaskRequestDTO>(TaskRequestDTO.mapper(taskService.updateTaskByTitle(title,task)),HttpStatus.OK);
    }
    @PutMapping("/title/foremployee/{title}")
    public  ResponseEntity<TaskRequestDTO> updateByTitleForManager(@PathVariable String title,@RequestBody Task task){
        return new ResponseEntity<TaskRequestDTO>(TaskRequestDTO.mapper(taskService.updateTaskByTitleForEmployee(title,task)),HttpStatus.OK);
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
            dtos.add(TaskRequestDTO.mapper(task));
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
            dtos.add(TaskRequestDTO.mapper(task));
        }
        if(tasks.isEmpty()){
            return new ResponseEntity<List<TaskRequestDTO>>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<List<TaskRequestDTO>>(dtos, HttpStatus.OK);
        }
    }


}
