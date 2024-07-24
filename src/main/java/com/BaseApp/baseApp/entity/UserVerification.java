package com.BaseApp.baseApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "UserVerification")
public class UserVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private String verificationCode;
    private LocalDateTime verificationCodeCreationTime;
    private LocalDateTime verificationTime;

}
