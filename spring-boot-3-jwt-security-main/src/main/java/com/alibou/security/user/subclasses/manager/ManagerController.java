package com.alibou.security.user.subclasses.manager;

import com.alibou.security.DTO.UserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/manager")
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @GetMapping("/all")
    public ResponseEntity<List<UserRequestDTO>> getall(){
        List<Manager> managers = managerService.getAllManagers();
        List<UserRequestDTO> dtos = new ArrayList<>();
        for (Manager manager : managers){
            dtos.add(UserRequestDTO.mapper(Optional.ofNullable(manager)));
        }
        return new ResponseEntity<List<UserRequestDTO>>(dtos,HttpStatus.OK);
    }
}
