package com.BaseApp.baseApp.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "UserType")
public class UserType extends BaseEntity {

    @Column(nullable = false)
    private String type;

}
