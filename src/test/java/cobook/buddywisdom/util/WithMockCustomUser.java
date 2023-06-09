package cobook.buddywisdom.util;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


// 테스트 코드에서만 사용
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    long id() default 1L;
    String username() default "admin@mock.test";
    String password() default "password";
    String role() default "ADMIN";
}