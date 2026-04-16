package com.warehouse.auth.domain.service;

import static java.util.Collections.singletonList;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.auth.infrastructure.adapter.secondary.UserReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserReadRepository userReadRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        final Optional<UserEntity> userOptional = userReadRepository.
                findByUsername(username);

        final UserEntity user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "found with username: " + username));

        return new org.springframework.security
                .core.userdetails.User(user.getUsername(), user.getPassword(),
                true, true,
                true, true,
                getAuthorities(user.getRole().name()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }
}
