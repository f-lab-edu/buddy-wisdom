package cobook.buddywisdom.global.jwt;

import cobook.buddywisdom.global.security.domain.vo.AuthMember;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.global.security.domain.vo.RoleType;
import cobook.buddywisdom.global.security.CustomUserDetails;
import cobook.buddywisdom.member.mapper.MemberMapper;
import cobook.buddywisdom.member.exception.NotFoundMemberException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "role";
    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final MemberMapper memberMapper;

    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.accessToken-validity-in-seconds}") long accessTokenValidityInMilliseconds,
            @Value("${jwt.refreshToken-validity-in-seconds}") long refreshTokenValidityInMilliseconds,
            MemberMapper memberMapper) {
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
        this.memberMapper = memberMapper;
    }

    public TokenDto createToken(Authentication authentication) {

        log.debug("createToken = {}", authentication.getName());

        // 권한 목록 조회
        // 여러 권한을 가질 경우 콤마(,)로 구분
        // ex : ADMIN, MENTO
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        log.debug("authorities = {}", authorities);

        long now = (new Date()).getTime();
        Date accessTokenValidTime = new Date(now + this.accessTokenValidityInMilliseconds);
        Date refreshTokenValidTime = new Date(now + this.refreshTokenValidityInMilliseconds);

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        // 회원 조회 시, id 로 가져오기 위해 넣어준다.
        String accessToken = Jwts.builder()
                .setSubject(principal.getUsername())
                .claim("id", principal.getId())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenValidTime)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(principal.getUsername())
                .claim("id", principal.getId())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(refreshTokenValidTime)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.of(accessToken, refreshToken);
    }

    @Override
    public void afterPropertiesSet() {
        // secret key 를 Base64 로 디코딩
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 유효성 검사
    public boolean validateToken(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return true;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.debug("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            log.debug("Expired token.");
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported token.");
        } catch (IllegalArgumentException e) {
            log.debug("Invalid token.");
        }
        return false;
    }

    public Authentication getAuthentication(String jwt) {

        // Token 정보를 이용하여 Claims 를 생성
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableList());

        String id = String.valueOf(claims.get("id"));

        log.debug("claims id = {}", id);
        log.debug("claims username = {}", claims.getSubject());

        boolean activeYn = true;
        AuthMember user = memberMapper.findActiveMemberByEmail(claims.getSubject(), activeYn)
                .orElseThrow(() -> new NotFoundMemberException(ErrorMessage.NOT_FOUND_MEMBER));

        CustomUserDetails principal = CustomUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(RoleType.valueOf(user.getRole()))
                .build();

        return new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
    }
}
