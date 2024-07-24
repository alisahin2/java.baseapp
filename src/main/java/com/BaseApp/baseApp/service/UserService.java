package com.BaseApp.baseApp.service;

import com.BaseApp.baseApp.DTO.UserDTO;
import com.BaseApp.baseApp.entity.User;
import com.BaseApp.baseApp.entity.UserType;
import com.BaseApp.baseApp.entity.UserVerification;
import com.BaseApp.baseApp.repository.UserRepository;
import com.BaseApp.baseApp.repository.UserTypeRepository;
import com.BaseApp.baseApp.repository.UserVerificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserVerificationRepository userVerificationRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private EmailService emailService;

    public String createUser(UserDTO userDTO) {
        try {
            // Email format validation
            if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return "İşlem başarısız: Geçersiz email formatı.";
            }

            // Create and save user
            User user = new User();

            user.setName(userDTO.getName());
            user.setSurname(userDTO.getSurname());
            user.setTitle(userDTO.getTitle());
            user.setDescription(userDTO.getDescription());
            user.setSex(userDTO.getSex());
            user.setHasCar(userDTO.isHasCar());
            user.setEmail(userDTO.getEmail());
            user.setActive(false); // Default to inactive
            user.setDeleted(false);

            // Set UserType
            Optional<UserType> userTypeOptional = userTypeRepository.findById(userDTO.getUserTypeId());
            if (userTypeOptional.isPresent()) {
                user.setUserType(userTypeOptional.get());
            } else {
                return "İşlem başarısız: Geçersiz userTypeId.";
            }
            userRepository.save(user);

            // Create and save user verification
            String verificationCode = emailService.sendVerificationEmail(user.getEmail());
            UserVerification userVerification = new UserVerification();
            userVerification.setUser(user);
            userVerification.setVerificationCode(verificationCode);
            userVerification.setVerificationCodeCreationTime(LocalDateTime.now());


            userVerificationRepository.save(userVerification);

            return "İşlem başarılı: Kullanıcı oluşturuldu.";


        } catch (Exception e) {
            return "İşlem başarısız: " + e.getMessage();
        }
    }

    public String updateUser(long id, UserDTO userDTO) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setName(userDTO.getName());
                user.setSurname(userDTO.getSurname());
                user.setTitle(userDTO.getTitle());
                user.setDescription(userDTO.getDescription());
                user.setSex(userDTO.getSex());
                user.setHasCar(userDTO.isHasCar());
                user.setEmail(userDTO.getEmail());

                // Set UserType
                Optional<UserType> userTypeOptional = userTypeRepository.findById(userDTO.getUserTypeId());
                if (userTypeOptional.isPresent()) {
                    user.setUserType(userTypeOptional.get());
                } else {
                    return "İşlem başarısız: Geçersiz userTypeId.";
                }

                userRepository.save(user);
                return "İşlem başarılı: Kullanıcı güncellendi.";
            } else {
                return "İşlem başarısız: Kullanıcı bulunamadı.";
            }
        } catch (Exception e) {
            return "İşlem başarısız: " + e.getMessage();
        }
    }

    public String deleteUser(long id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setDeleted(true); // Soft delete
                userRepository.save(user);
                return "İşlem başarılı: Kullanıcı silindi.";
            } else {
                return "İşlem başarısız: Kullanıcı bulunamadı.";
            }
        } catch (Exception e) {
            return "İşlem başarısız: " + e.getMessage();
        }
    }

    public UserDTO getUser(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UserDTO(
                    user.getName(),
                    user.getSurname(),
                    user.getTitle(),
                    user.getDescription(),
                    user.getSex(),
                    user.isHasCar(),
                    user.getEmail(),
                    user.getUserType().getId()
            );
        } else {
            return null;
        }
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(
                        user.getName(),
                        user.getSurname(),
                        user.getTitle(),
                        user.getDescription(),
                        user.getSex(),
                        user.isHasCar(),
                        user.getEmail(),
                        user.getUserType().getId()
                ))
                .collect(Collectors.toList());
    }

    public String verifyUser(String email, String verificationCode) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<UserVerification> optionalUserVerification = userVerificationRepository.findByUser(user);
            if (optionalUserVerification.isPresent()) {
                UserVerification userVerification = optionalUserVerification.get();
                if (userVerification.getVerificationCode().equals(verificationCode)) {
                    if (isVerificationCodeValid(userVerification.getVerificationCodeCreationTime())) {
                        user.setActive(true);
                        userRepository.save(user);
                        userVerification.setVerificationCodeCreationTime(LocalDateTime.now());
                        userVerificationRepository.save(userVerification);
                        return "İşlem başarılı: Kullanıcı doğrulandı.";
                    } else {
                        return "İşlem başarısız: Doğrulama kodunun süresi dolmuş.";
                    }
                } else {
                    return "İşlem başarısız: Geçersiz doğrulama kodu.";
                }
            } else {
                return "İşlem başarısız: Doğrulama kaydı bulunamadı.";
            }
        } else {
            return "İşlem başarısız: Kullanıcı bulunamadı.";
        }
    }

    private boolean isVerificationCodeValid(LocalDateTime creationTime) {
        LocalDateTime now = LocalDateTime.now();
        return creationTime.isAfter(now.minusHours(24)); // 24 saat geçerlilik süresi
    }

}
