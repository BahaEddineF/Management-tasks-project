package com.alibou.security.user;

import com.alibou.security.DTO.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
//@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserRequestDTO>> getAllUsers() {
        List<UserRequestDTO> users = userService.findAllWithQuery();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequestDTO> getById(@PathVariable Integer id) {
        UserRequestDTO user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserRequestDTO> getByEmail(@PathVariable String email) {
        UserRequestDTO user = userService.findByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserRequestDTO> updateUserById(
            @PathVariable Integer id,
            @RequestBody UserRequestDTO userRequestDTO) {
        UserRequestDTO updatedUser = userService.UpdateById(userRequestDTO,id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<UserRequestDTO> updateUserByEmail(
            @PathVariable String email,
            @RequestBody UserRequestDTO userRequestDTO) {
        UserRequestDTO updatedUser = userService.UpdateByEmail(userRequestDTO,email);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<UserRequestDTO> deleteByEmail(
            @PathVariable String email) {
        if (!userService.deleteByEmail(email)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserRequestDTO> deleteByid(
            @PathVariable Integer id) {
        if (!userService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
