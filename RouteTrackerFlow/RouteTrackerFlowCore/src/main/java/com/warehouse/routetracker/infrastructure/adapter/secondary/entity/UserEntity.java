package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import com.warehouse.routetracker.infrastructure.adapter.secondary.authority.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "route.UserEntity")
@Builder
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String departmentCode;

}
