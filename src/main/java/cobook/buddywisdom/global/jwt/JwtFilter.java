package cobook.buddywisdom.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String jwt = resolveToken(servletRequest);
        String requestURI = servletRequest.getRequestURI();

        // 유효성 검사
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

            log.info("jwt = {}", jwt);

            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Authentication user '{}' saved in Security Context, uri: {}", authentication.getName(), requestURI);
        } else {
            log.debug("JwtFilter Invalid Token, uri: {}", requestURI);
        }

        chain.doFilter(request, response);
    }

    // request header 에서 token 정보를 꺼내온다
    private String resolveToken(HttpServletRequest request) {
        log.info("AUTHORIZATION_HEADER = {}", request.getHeader(AUTHORIZATION_HEADER));
        return request.getHeader(AUTHORIZATION_HEADER);
    }
}
