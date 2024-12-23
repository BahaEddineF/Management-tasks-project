package com.alibou.security.user;

import com.alibou.security.DTO.UserRequestDTO;
import com.alibou.security.user.subclasses.employee.Title;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor

public class UserService {


    private static final String UPLOAD_DIR = "uploads/profile-images/";


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }

    public List<UserRequestDTO> findAll() {
        List<UserRequestDTO> dtos = new ArrayList<>();

        try {
            // Retrieve all users from the userRepository
            List<User> users = userRepository.findAll();

            // Map each user to a DTO, handling null cases
            for (User user : users) {
                dtos.add(UserRequestDTO.mapper(Optional.ofNullable(user)));
            }
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error occurred while fetching or mapping users: " + e.getMessage());
            e.printStackTrace();

            // Optionally, you can throw a custom exception or return an empty list
            // throw new CustomException("Failed to retrieve users");
        }

        return dtos;
    }

    public List<UserRequestDTO> findAllWithQuery() { //Used to query all data even the title of the employe
        // Fetch all users using native query
        List<Object[]> users = userRepository.findAllUsersNative();
        // Map Object[] to UserDTO
        return users.stream()
                .map(user -> new UserRequestDTO((Integer) user[0],
                                                (String) user[1],
                                                (String) user[2],
                                                (String) user[3],
                                                (String) user[4],
                                                (String) user[5],
                                                Role.valueOf((String) user[6]) ,
                                                user[7] != null ? Title.valueOf((String) user[7]) : null))

                .collect(Collectors.toList());
    }

    public UserRequestDTO findById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return UserRequestDTO.mapper(Optional.ofNullable(user.get()));
        }else{
            throw new UsernameNotFoundException("User With Id \" + id + \" not found");
        }
    }

    public UserRequestDTO findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return UserRequestDTO.mapper(Optional.ofNullable(user.get()));
        }else{
            throw new UsernameNotFoundException("User With Email " + email + " not found");
        }
    }

    public UserRequestDTO UpdateById(UserRequestDTO request, Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User With Id " + id + " not found");
        }
        user.get().setFirstname(request.getFirstname());
        user.get().setLastname(request.getLastname());
        user.get().setEmail(request.getEmail());
        user.get().setPhone(request.getPhone());
        userRepository.saveAndFlush(user.get());
        return request;

    }

    public UserRequestDTO UpdateByEmail(UserRequestDTO request, String username) {
        Optional<User> user = userRepository.findByEmail(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User With Username " + username + " not found");
        }
        user.get().setFirstname(request.getFirstname());
        user.get().setLastname(request.getLastname());
        user.get().setEmail(request.getEmail());
        user.get().setPhone(request.getPhone());
        userRepository.saveAndFlush(user.get());
        return request;
    }

    public boolean deleteById(Integer id) {
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteByEmail(String email) {
        if(userRepository.findByEmail(email).isPresent()){
            userRepository.deleteByEmail(email);
            return true;
        }
        return false;

    }


    public String uploadProfileImage(Integer userId, MultipartFile file) throws IOException {
        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate file
        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Invalid file type");
        }

        // Generate unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        // Ensure directories exist
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        // Save file name in the user entity
        user.setProfileImage(fileName);
        userRepository.save(user);

        return fileName;
    }

    public Resource getProfileImage(String fileName) throws MalformedURLException {
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("File not found");
        }

        return resource;
    }

    public Resource getProfileImageByEmail(String email) throws Exception {
        // Fetch the user by email
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new Exception("User not found");
        }

        // Get the file name from the user's profile image field
        String fileName = userOptional.get().getProfileImage();

        // Construct the file path
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new Exception("File not found or not readable");
        }
    }
}
