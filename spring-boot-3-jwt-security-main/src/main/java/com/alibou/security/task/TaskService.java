package com.alibou.security.task;

import com.alibou.security.emailSender.EmailService;
import com.alibou.security.project.ProjectRepository;
import com.alibou.security.status.Status;
import com.alibou.security.task.Task;
import com.alibou.security.task.TaskRepository;
import com.alibou.security.user.subclasses.employee.Employee;
import com.alibou.security.user.subclasses.employee.EmployeeService;
import com.alibou.security.user.subclasses.manager.Manager;
import com.alibou.security.user.subclasses.manager.ManagerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmailService emailService;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private  ManagerRepository managerRepository;

    @SneakyThrows
    public Task createTask(Task task){
        // Save the task
        Task savedTask = taskRepository.save(task);

        // Check if the employee assigned to the task is valid
        Employee assignedEmployee = task.getEmployee(); // assuming Employee is a field in Task
        if (assignedEmployee != null && assignedEmployee.getEmail() != null) {
            String employeeEmail = assignedEmployee.getEmail();
            String employeeFullName = assignedEmployee.getFirstname() + " " + assignedEmployee.getLastname();

            // Send email notification to the assigned employee
            emailService.sendNewTaskNotificationEmail(employeeEmail, employeeFullName, savedTask);
        }

        return savedTask;
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

    @SneakyThrows
    @Transactional
    public Task updateTaskById(Integer id, Task updatedTask) {
        // Fetch existing task
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + id + " not found"));

        // Keep original title for email notifications
        String originalTitle = existingTask.getTitle();

        // Update task fields
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStart_date(updatedTask.getStart_date());
        existingTask.setEnd_date(updatedTask.getEnd_date());
        existingTask.setStatus(updatedTask.getStatus());

        // Check if employee is being updated
        Employee oldEmployee = existingTask.getEmployee();
        Employee newEmployee = updatedTask.getEmployee();
        existingTask.setEmployee(newEmployee); // Update employee field

        if (oldEmployee != null && newEmployee != null) {
            // Employee remains the same
            if (oldEmployee.getEmail().equals(newEmployee.getEmail())) {
                emailService.sendTaskUpdateNotificationEmail(
                        oldEmployee.getEmail(),
                        oldEmployee.getFirstname() + " " + oldEmployee.getLastname(),
                        existingTask,
                        originalTitle
                );
            }
            // Employee changed
            else {
                // Notify old employee
                emailService.sendNoLongerAssignedTaskEmail(
                        oldEmployee.getEmail(),
                        oldEmployee.getFirstname() + " " + oldEmployee.getLastname(),
                        originalTitle
                );

                // Notify new employee
                emailService.sendNewTaskNotificationEmail(
                        newEmployee.getEmail(),
                        newEmployee.getFirstname() + " " + newEmployee.getLastname(),
                        existingTask
                );
            }
        }

        // Save updated task
        return taskRepository.save(existingTask);
    }

    @SneakyThrows
    @Transactional
    public Task updateTaskByTitle(String title, Task updatedTask) {
        // Fetch the existing task by title
        Task existingTask = taskRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Task with title '" + title + "' not found"));

        // Keep original title for email notifications
        String originalTitle = existingTask.getTitle();

        // Update task fields
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStart_date(updatedTask.getStart_date());
        existingTask.setEnd_date(updatedTask.getEnd_date());
        existingTask.setStatus(updatedTask.getStatus());

        // Check if employee is being updated
        Employee oldEmployee = existingTask.getEmployee();
        Employee newEmployee = updatedTask.getEmployee();
        existingTask.setEmployee(newEmployee); // Update employee field

        if (oldEmployee != null && newEmployee != null) {
            // Employee remains the same
            if (oldEmployee.getEmail().equals(newEmployee.getEmail())) {
                emailService.sendTaskUpdateNotificationEmail(
                        oldEmployee.getEmail(),
                        oldEmployee.getFirstname() + " " + oldEmployee.getLastname(),
                        existingTask,
                        originalTitle
                );
            }
            // Employee changed
            else {
                // Notify old employee
                emailService.sendNoLongerAssignedTaskEmail(
                        oldEmployee.getEmail(),
                        oldEmployee.getFirstname() + " " + oldEmployee.getLastname(),
                        originalTitle
                );

                // Notify new employee
                emailService.sendNewTaskNotificationEmail(
                        newEmployee.getEmail(),
                        newEmployee.getFirstname() + " " + newEmployee.getLastname(),
                        existingTask
                );
            }
        }

        // Save updated task
        return taskRepository.save(existingTask);
    }




    @SneakyThrows
    @Transactional
    public Task updateTaskByTitleForEmployee(String title, Task updatedTask) {
        Task existingTask = taskRepository.findByTitle(title).get();
        Status oldStatus = existingTask.getStatus();
        Status newStatus = updatedTask.getStatus();
        if(oldStatus !=newStatus){
            Optional<Manager> manager = managerRepository.findById(projectRepository.findById(existingTask.getProject().getId()).get().getManager().getId());
            String email= manager.get().getEmail();
            String fullname= manager.get().getFirstname() + " " + manager.get().getLastname();
            emailService.notifyManagerThatTaskStatusWasChanged(email, fullname, updatedTask.getTitle(),oldStatus,newStatus);
        }
        existingTask.setStatus(updatedTask.getStatus());
        return taskRepository.save(existingTask);
    }


        @SneakyThrows
        @Transactional
        public void deleteTaskById(Integer id){
            // Fetch the existing task by ID
            Task existingTask = taskRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Task with ID " + id + " not found"));

            // Retrieve employee details
            Employee assignedEmployee = existingTask.getEmployee();
            if (assignedEmployee != null) {
                String employeeEmail = assignedEmployee.getEmail();
                String employeeFullName = assignedEmployee.getFirstname() + " " + assignedEmployee.getLastname();

                // Send email notification to the assigned employee about task deletion
                emailService.sendTaskDeletedEmail(employeeEmail, employeeFullName, existingTask.getTitle());
            }

            // Delete the task
            taskRepository.deleteById(id);
        }

        @SneakyThrows
        @Transactional
        public void deleteTaskByTitle(String title){
            // Fetch the existing task by title
            Task existingTask = taskRepository.findByTitle(title)
                    .orElseThrow(() -> new IllegalArgumentException("Task with title '" + title + "' not found"));

            // Retrieve employee details
            Employee assignedEmployee = existingTask.getEmployee();
            if (assignedEmployee != null) {
                String employeeEmail = assignedEmployee.getEmail();
                String employeeFullName = assignedEmployee.getFirstname() + " " + assignedEmployee.getLastname();

                // Send email notification to the assigned employee about task deletion
                emailService.sendTaskDeletedEmail(employeeEmail, employeeFullName, existingTask.getTitle());
            }

            // Delete the task
            taskRepository.deleteByTitle(title);
        }


    public List<Task> getTasksByEmployeeEmail(String email) {
        return taskRepository.findByEmployeeEmail(email);
    }
    public List<Task> getTasksByProjectTitle(String title) {
        return taskRepository.findByProjectTitle(title);
    }

    public Map<String, Long> getTaskCountByStatus() {
        List<Object[]> result = taskRepository.countTasksByStatus();
        return result.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(), // Convert status (Enum) to String
                        row -> (Long) row[1]      // Count as Long
                ));
    }

    public Map<String, Long> getTasksByManagerEmailAndCountByStatus(String email) {
        List<Object[]> result = taskRepository.findTasksByManagerEmailAndCountByStatus(email);
        return result.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(), // Convert task status (Enum) to String
                        row -> (Long) row[1]      // Count as Long
                ));
    }
    public Map<String, Long> getTasksByEmployeeEmailAndCountByStatus(String email) {
        List<Object[]> result = taskRepository.findTasksByEmployeeEmailAndCountByStatus(email);
        return result.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(), // Convert status (Enum) to String
                        row -> (Long) row[1]      // Count as Long
                ));
    }
}
