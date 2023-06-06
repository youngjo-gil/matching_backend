package com.matching.util;

import com.matching.member.domain.MemberRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        List<String> roles = new ArrayList<>();

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        roles.add(MemberRole.USER.getRole());

        List<GrantedAuthority> simpleGrantedAuthorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        final UserDetails principal = new User(annotation.username(), annotation.password(), true, true, true, true, simpleGrantedAuthorities);

        System.out.println(principal.getAuthorities());
        final UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
