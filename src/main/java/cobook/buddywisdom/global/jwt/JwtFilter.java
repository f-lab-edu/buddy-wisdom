package cobook.buddywisdom.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


@Slf4j
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response
            , FilterChain chain) throws IOException, ServletException {

        HttpServletRequest requestHeader = (HttpServletRequest) request;

        requestHeader.getHeader(AUTHORIZATION_HEADER);
        String jwt = ((HttpServletRequest) request).getHeader("X-Auth-Token");

        // 유효성 검사
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            log.debug("jwt = {}", jwt);
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Authentication user '{}' saved in Security Context", authentication.getName());
        } else {
            log.debug("JwtFilter Invalid Token, uri: {}");
        }

        chain.doFilter(request, response);
    }

}
