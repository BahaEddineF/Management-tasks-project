package com.alibou.security.dashboard;

import com.alibou.security.project.ProjectService;
import com.alibou.security.task.TaskService;
import com.alibou.security.user.subclasses.admin.AdminService;
import com.alibou.security.user.subclasses.employee.EmployeeService;
import com.alibou.security.user.subclasses.manager.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Users Summary
        Map<String, Long> userSummary = new HashMap<>();
        userSummary.put("admin", adminService.countAll());
        userSummary.put("manager", managerService.countAll());
        userSummary.put("employee", employeeService.countAll());
        long totalUsers = userSummary.values().stream().mapToLong(Long::longValue).sum();
        summary.put("users", Map.of("total", totalUsers, "byType", userSummary));

        // Projects Summary
        Map<String, Long> projectSummary = projectService.getProjectCountByStatus();
        long totalProjects = projectSummary.values().stream().mapToLong(Long::longValue).sum();
        summary.put("projects", Map.of("total", totalProjects, "byStatus", projectSummary));

        // Tasks Summary
        Map<String, Long> taskSummary = taskService.getTaskCountByStatus();
        long totalTasks = taskSummary.values().stream().mapToLong(Long::longValue).sum();
        summary.put("tasks", Map.of("total", totalTasks, "byStatus", taskSummary));

        return summary;
    }
}
