package com.matching.util;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    long id() default 8L;
    String nickname() default "test_nickname";
    String[] roles() default {"USER"};
    String role() default "USER";
    String[] authorities() default {};
    String password() default "password";

    String username() default "8";
}
