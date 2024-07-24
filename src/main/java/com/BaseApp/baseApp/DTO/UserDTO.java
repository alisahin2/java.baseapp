package com.BaseApp.baseApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String surname;
    private String title;
    private String description;
    private String sex;
    private boolean hasCar;
    private String email;
    private long userTypeId;
}
