package com.BaseApp.baseApp.repository;

import com.BaseApp.baseApp.entity.User;
import com.BaseApp.baseApp.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    Optional<UserVerification> findByUser(User user);

}
