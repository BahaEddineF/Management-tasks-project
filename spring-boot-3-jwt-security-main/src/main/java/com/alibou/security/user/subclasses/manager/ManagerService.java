package com.alibou.security.user.subclasses.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    @Autowired
    ManagerRepository managerRepository;

    public List<Manager> getAllManagers(){
        return managerRepository.findAll();
    }

    public Manager getManagerById(Integer id){
        return managerRepository.getReferenceById(id);
    }
    public Long countAll() {
        return managerRepository.count();
    }
}
