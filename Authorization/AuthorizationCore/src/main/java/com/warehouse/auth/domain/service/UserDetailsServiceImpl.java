package com.warehouse.auth.domain.service;

import com.warehouse.auth.infrastructure.adapter.secondary.AuthenticationReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;


@AllArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthenticationReadRepository authenticationReadRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        final Optional<UserEntity> userOptional = authenticationReadRepository.
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
