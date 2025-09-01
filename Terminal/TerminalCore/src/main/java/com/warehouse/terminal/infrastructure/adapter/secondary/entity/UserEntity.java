package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.infrastructure.adapter.secondary.enumeration.Role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "device.UserEntity")
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    @Column(name = "username", unique = true, nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "username"))
    private Username username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "department_code", nullable = false)
    private String depotCode;
}
