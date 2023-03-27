package com.myblogbackend.blog.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myblogbackend.blog.models.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.*;

public class UserPrincipal implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;

    private final String name;

    private final String email;

    @JsonIgnore
    private final String password;

    private Map<String, Object> attributes;

    public UserPrincipal(final UUID id, final String name,
                         final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static UserPrincipal build(final UserEntity userEntity) {
        return new UserPrincipal(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPassword()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrincipal user = (UserPrincipal) o;
        return Objects.equals(id, user.id);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}