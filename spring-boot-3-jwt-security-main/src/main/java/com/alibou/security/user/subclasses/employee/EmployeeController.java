package com.alibou.security.user.subclasses.employee;

import com.alibou.security.DTO.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
//@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    @PutMapping("/email/{email}")
    public ResponseEntity<UserRequestDTO> updateUserByEmail(
            @PathVariable String email,
            @RequestBody UserRequestDTO userRequestDTO) {
        UserRequestDTO updatedUser = employeeService.UpdateByEmail(userRequestDTO,email);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
