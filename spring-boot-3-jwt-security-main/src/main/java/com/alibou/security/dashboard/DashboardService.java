package com.alibou.security.dashboard;

import com.alibou.security.project.ProjectService;
import com.alibou.security.status.Status;
import com.alibou.security.task.TaskService;
import com.alibou.security.user.subclasses.admin.AdminService;
import com.alibou.security.user.subclasses.employee.EmployeeService;
import com.alibou.security.user.subclasses.manager.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<String, Object> getDashboardSummaryForAdmin() {
        Map<String, Object> summary = new HashMap<>();

        // Users Summary
        Map<String, Long> userSummary = new HashMap<>();
        userSummary.put("admin", adminService.countAll());
        userSummary.put("manager", managerService.countAll());
        userSummary.put("employee", employeeService.countAll());
        long totalUsers = userSummary.values().stream().mapToLong(Long::longValue).sum();
        summary.put("users", Map.of("total", totalUsers, "byType", userSummary));

        // Projects Summary
        Map<String, Long> projectSummary = ensureAllStatusesPresent(projectService.getProjectCountByStatus());
        long totalProjects = projectSummary.values().stream().mapToLong(Long::longValue).sum();
        summary.put("projects", Map.of("total", totalProjects, "byStatus", projectSummary));

        // Tasks Summary
        Map<String, Long> taskSummary = ensureAllStatusesPresent(taskService.getTaskCountByStatus());
        long totalTasks = taskSummary.values().stream().mapToLong(Long::longValue).sum();
        summary.put("tasks", Map.of("total", totalTasks, "byStatus", taskSummary));

        return summary;
    }

    public Map<String, Object> getDashboardSummaryForManager(String email) {
        Map<String, Object> summary = new HashMap<>();

        // Projects Summary for Manager
        Map<String, Long> projectSummary = ensureAllStatusesPresent(projectService.getProjectByManagerEmailAndCountByStatus(email));
        long totalProjects = projectSummary.values().stream().mapToLong(Long::longValue).sum();
        summary.put("projects", Map.of("total", totalProjects, "byStatus", projectSummary));

        // Tasks Summary for Manager
        Map<String, Long> taskSummary = ensureAllStatusesPresent(taskService.getTasksByManagerEmailAndCountByStatus(email));
        long totalTasks = taskSummary.values().stream().mapToLong(Long::longValue).sum();
        summary.put("tasks", Map.of("total", totalTasks, "byStatus", taskSummary));


        return summary;
    }

    public Map<String, Object> getDashboardSummaryForEmployee(String email) {
        Map<String, Object> summary = new HashMap<>();

        // Tasks Summary for Manager
        Map<String, Long> taskSummary = ensureAllStatusesPresent(taskService.getTasksByEmployeeEmailAndCountByStatus(email));
        long totalTasks = taskSummary.values().stream().mapToLong(Long::longValue).sum();
        summary.put("tasks", Map.of("total", totalTasks, "byStatus", taskSummary));


        return summary;
    }



        private Map<String, Long> ensureAllStatusesPresent(Map<String, Long> statusCounts) {
        // Initialize with all possible statuses set to 0
        Map<String, Long> result = EnumSet.allOf(Status.class).stream()
                .collect(Collectors.toMap(Enum::name, status -> 0L));

        // Merge with existing counts
        result.putAll(statusCounts);
        return result;
    }
}
