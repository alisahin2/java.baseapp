package com.BaseApp.baseApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.BaseApp.baseApp.entity.UserType;
import com.BaseApp.baseApp.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usertypes")
public class UserTypeController {
    @Autowired
    private UserTypeService userTypeService;

    // Create
    @PostMapping
    public ResponseEntity<UserType> createUserType(@RequestBody UserType userType) {
        UserType createdUserType = userTypeService.createUserType(userType);
        return new ResponseEntity<>(createdUserType, HttpStatus.CREATED);
    }

    // Read
    @GetMapping
    public ResponseEntity<List<UserType>> getAllUserTypes() {
        List<UserType> userTypes = userTypeService.getAllUserTypes();
        return new ResponseEntity<>(userTypes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserType> getUserTypeById(@PathVariable UUID id) {
        Optional<UserType> userType = userTypeService.getUserTypeById(id);
        return userType.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<UserType> updateUserType(@PathVariable UUID id, @RequestBody UserType userTypeDetails) {
        UserType updatedUserType = userTypeService.updateUserType(id, userTypeDetails);
        return new ResponseEntity<>(updatedUserType, HttpStatus.OK);
    }

    // Delete (logical delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserType(@PathVariable UUID id) {
        userTypeService.deleteUserType(id);
        return ResponseEntity.noContent().build();
    }
}
