package com.alibou.security.task;

import com.alibou.security.task.Task;
import com.alibou.security.task.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    public Task createTask(Task task){
        return taskRepository.save(task);
    }
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task getTaskById(Integer id){
        return taskRepository.findById(id).get();
    }


    public Task getTaskByTitle(String title){
        return taskRepository.findByTitle(title).get();
    }

    public Task updateTaskById(Integer id, Task updatedTask) {
        Task existingTask = taskRepository.findById(id).get();
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStart_date(updatedTask.getStart_date());
        existingTask.setEnd_date(updatedTask.getEnd_date());
        existingTask.setEmployee(updatedTask.getEmployee());
        existingTask.setStatus(updatedTask.getStatus());
        return taskRepository.save(existingTask);
    }

    @Transactional
    public Task updateTaskByTitle(String title, Task updatedTask) {
        Task existingTask = taskRepository.findByTitle(title).get();
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStart_date(updatedTask.getStart_date());
        existingTask.setEnd_date(updatedTask.getEnd_date());
        existingTask.setEmployee(updatedTask.getEmployee());
        existingTask.setStatus(updatedTask.getStatus());
        return taskRepository.save(existingTask);
    }

    @Transactional
    public Task updateTaskByTitleForEmployee(String title, Task updatedTask) {
        Task existingTask = taskRepository.findByTitle(title).get();
        existingTask.setStatus(updatedTask.getStatus());
        return taskRepository.save(existingTask);
    }



    @Transactional
    public void deleteTaskById(Integer id){
        taskRepository.deleteById(id);
    }
    @Transactional
    public void deleteTaskByTitle(String title){
        taskRepository.deleteByTitle(title);
    }

    public List<Task> getTasksByEmployeeEmail(String email) {
        return taskRepository.findByEmployeeEmail(email);
    }
    public List<Task> getTasksByProjectTitle(String title) {
        return taskRepository.findByProjectTitle(title);
    }
}
