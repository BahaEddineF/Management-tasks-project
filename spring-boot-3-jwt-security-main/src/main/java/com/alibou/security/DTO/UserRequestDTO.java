package com.alibou.security.DTO;

import com.alibou.security.user.Role;
import com.alibou.security.user.User;
import com.alibou.security.user.subclasses.employee.Employee;
import com.alibou.security.user.subclasses.employee.Title;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestDTO {
    private Integer id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private String profile_image;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Title title;


    public static UserRequestDTO mapper(Optional<User> user) {
        if (user.isEmpty()) {
            return null; // Or throw an exception based on your requirements
        }

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        User userValue = user.get();
        userRequestDTO.setId(userValue.getId());
        userRequestDTO.setFirstname(userValue.getFirstname());
        userRequestDTO.setLastname(userValue.getLastname());
        userRequestDTO.setEmail(userValue.getEmail());
        userRequestDTO.setProfile_image(userValue.getProfileImage());
        userRequestDTO.setRole(userValue.getRole());
        userRequestDTO.setPhone(userValue.getPhone());
        return userRequestDTO;
    }


    public static UserRequestDTO mapperEmployee(Optional<Employee> user){
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setId(user.get().getId());
        userRequestDTO.setFirstname(user.get().getFirstname());
        userRequestDTO.setLastname(user.get().getLastname());
        userRequestDTO.setEmail(user.get().getEmail());
        userRequestDTO.setProfile_image(user.get().getProfileImage());
        userRequestDTO.setRole(user.get().getRole());
        userRequestDTO.setPhone(user.get().getPhone());
        userRequestDTO.setTitle(user.get().getTitle());
        return userRequestDTO;
    }
}