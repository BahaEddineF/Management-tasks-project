package com.alibou.security.user.subclasses.admin;

import com.alibou.security.user.subclasses.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;
    public List<Admin> getAllAdmin(){
        return adminRepository.findAll();
    }

    public Long countAll() {
        return adminRepository.count();
    }
}
