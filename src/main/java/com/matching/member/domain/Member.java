package com.matching.member.domain;

import com.matching.common.domain.BaseEntity;
import com.matching.member.dto.SignUpRequest;
import com.matching.member.dto.UpdateMemberRequest;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class Member extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String profileImageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public void update(UpdateMemberRequest parameter) {
        this.password = (parameter.getPassword() == null) ? this.getPassword() : parameter.getPassword();
        this.nickname = (parameter.getNickname() == null) ? this.getNickname() : parameter.getNickname();
        this.profileImageUrl = (parameter.getProfileImageUrl() == null) ? this.getProfileImageUrl() : parameter.getProfileImageUrl();
    }

    public static Member from(SignUpRequest parameter) {
        return Member.builder()
                .email(parameter.getEmail())
                .password(parameter.getPassword())
                .name(parameter.getName())
                .nickname(parameter.getNickname())
                .profileImageUrl(parameter.getProfileImageUrl())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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
