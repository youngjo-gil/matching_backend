package com.matching.member.domain;

import com.matching.common.domain.BaseEntity;
import lombok.*;
import com.matching.member.dto.SignUpRequest;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.envers.AuditOverride;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "member")
public class Member extends BaseEntity implements UserDetails {
    @Id
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String profileImageUrl;
    private String role;

    public static Member from(SignUpRequest parameter) {
        return Member.builder()
                .email(parameter.getEmail())
                .password(parameter.getPassword())
                .name(parameter.getName())
                .nickname(parameter.getNickname())
                .profileImageUrl(parameter.getProfileImageUrl())
                .role("ROLE_USER")
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
