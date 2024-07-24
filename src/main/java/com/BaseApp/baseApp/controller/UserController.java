package com.BaseApp.baseApp.controller;

import com.BaseApp.baseApp.DTO.UserDTO;
import com.BaseApp.baseApp.DTO.VerificationRequestDTO;
import com.BaseApp.baseApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        String result = userService.createUser(userDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable long id, @RequestBody UserDTO userDTO) {
        String result = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        String result = userService.deleteUser(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        UserDTO userDTO = userService.getUser(id);
        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerificationRequestDTO verificationRequest) {
        String response = userService.verifyUser(verificationRequest.getEmail(), verificationRequest.getVerificationCode());
        return ResponseEntity.ok(response);
    }
}
