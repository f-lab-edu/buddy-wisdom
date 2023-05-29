package cobook.buddywisdom.global.security;

import cobook.buddywisdom.auth.mapper.AuthMapper;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.member.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthMapper authMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return CustomUserDetails.of(authMapper.findByEmail(username)
                .orElseThrow(() -> new NotFoundMemberException(ErrorMessage.NOT_FOUND_MEMBER)));
    }
}
