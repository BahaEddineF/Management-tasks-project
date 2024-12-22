package com.alibou.security.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin/summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummaryForAdmin() {
        return ResponseEntity.ok(dashboardService.getDashboardSummaryForAdmin());
    }

    @GetMapping("/manager/summary/{email}")
    public ResponseEntity<Map<String, Object>> getDashboardSummaryForManager(@PathVariable String email) {
        return ResponseEntity.ok(dashboardService.getDashboardSummaryForManager(email));
    }

    @GetMapping("/employee/summary/{email}")
    public ResponseEntity<Map<String, Object>> getDashboardSummaryForEmployee(@PathVariable String email) {
        return ResponseEntity.ok(dashboardService.getDashboardSummaryForEmployee(email));
    }
}
