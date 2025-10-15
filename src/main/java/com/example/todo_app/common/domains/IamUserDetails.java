package com.example.todo_app.common.domains;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class IamUserDetails implements UserDetails {
    private String id;
    private String username;
    private String email;
    private String password;
    private Instant lastLogin;
    private Boolean isActive;
    private Set<String> roles;
    private Set<String> permissions;
    private LocalDateTime verifiedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        if (roles != null) {
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }

        if (permissions != null) {
            permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return verifiedAt != null;
    }

}

