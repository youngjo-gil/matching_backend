package com.matching.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matching.common.domain.BaseEntity;
import com.matching.member.dto.SignUpRequest;
import com.matching.member.dto.MemberUpdateRequest;
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
    private String organization;
    private String job;
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberSkill> memberSkills = new ArrayList<>();

    public void update(MemberUpdateRequest parameter) {
        this.password = (parameter.getPassword() == null) ? this.getPassword() : parameter.getPassword();
        this.profileImageUrl = (parameter.getProfileImageUrl() == null) ? this.getProfileImageUrl() : parameter.getProfileImageUrl();
        this.organization = (parameter.getOrganization() == null) ? this.getOrganization() : parameter.getOrganization();
        this.job = (parameter.getJob() == null) ? this.getJob() : parameter.getJob();
    }

    public List<String> getRoles(MemberRole role) {
        List<String> roles = new ArrayList<>();
        roles.add(role.getRole());
        return roles;
    }

    public static Member from(SignUpRequest parameter) {
        return Member.builder()
                .email(parameter.getEmail())
                .password(parameter.getPassword())
                .name(parameter.getName())
                .profileImageUrl(parameter.getProfileImageUrl())
                .status(MemberStatus.REGISTERED)
                .role(MemberRole.USER)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        roles.add(this.role.getRole());

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
