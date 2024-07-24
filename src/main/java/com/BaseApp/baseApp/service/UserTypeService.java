package com.BaseApp.baseApp.service;

import com.BaseApp.baseApp.entity.UserType;
import com.BaseApp.baseApp.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserTypeService {
    @Autowired
    private UserTypeRepository userTypeRepository;

    // Create
    public UserType createUserType(UserType userType) {
        return userTypeRepository.save(userType);
    }

    // Read
    public List<UserType> getAllUserTypes() {
        return userTypeRepository.findAll();
    }

    public Optional<UserType> getUserTypeById(Long id) {
        return userTypeRepository.findById(id);
    }

    // Update
    public UserType updateUserType(Long id, UserType userTypeDetails) {
        UserType userType = userTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserType not found with id " + id));

        userType.setType(userTypeDetails.getType());
        userType.setUpdatedDate(LocalDateTime.now());
        return userTypeRepository.save(userType);
    }

    // Delete
    public void deleteUserType(Long id) {
        UserType userType = userTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserType not found with id " + id));

        userType.setDeleted(true);
        userType.setActive(false);
        userTypeRepository.save(userType);
    }

}
