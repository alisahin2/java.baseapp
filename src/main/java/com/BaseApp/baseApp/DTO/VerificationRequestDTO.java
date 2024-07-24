package com.BaseApp.baseApp.DTO;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VerificationRequestDTO {
    private String email;
    private String verificationCode;

}
