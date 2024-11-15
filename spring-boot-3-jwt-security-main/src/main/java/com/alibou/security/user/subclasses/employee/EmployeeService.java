package com.alibou.security.user.subclasses.employee;

import com.alibou.security.DTO.UserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public UserRequestDTO UpdateByEmail(UserRequestDTO request, String username) {
        Optional<Employee> user = employeeRepository.findByEmail(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User With Username " + username + " not found");
        }
        user.get().setFirstname(request.getFirstname());
        user.get().setLastname(request.getLastname());
        user.get().setEmail(request.getEmail());
        user.get().setPhone(request.getPhone());
        user.get().setTitle(request.getTitle());
        employeeRepository.saveAndFlush(user.get());
        return request;
    }
}
