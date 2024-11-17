package com.alibou.security.user.subclasses.employee;

import com.alibou.security.DTO.UserRequestDTO;
import com.alibou.security.user.subclasses.employee.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/all")
    public ResponseEntity<List<UserRequestDTO>> getall(){
        List<Employee> employees = employeeService.getAllEmployees();
        List<UserRequestDTO> dtos = new ArrayList<>();
        for (Employee employee : employees){
            dtos.add(UserRequestDTO.mapperEmployee(Optional.ofNullable(employee)));
        }
        return new ResponseEntity<List<UserRequestDTO>>(dtos,HttpStatus.OK);
    }
}
