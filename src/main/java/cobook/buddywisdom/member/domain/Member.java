package cobook.buddywisdom.member.domain;


import cobook.buddywisdom.global.domain.vo.RoleType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;


// TODO : 회원가입 시, 사용할 Member 객체 (로그인에서는 사용 X, 상세조회 사용 O)
@Builder(toBuilder = true)
public record Member (
    @Id
    @Nullable
    Long id,
    @NotNull
    String name,
    String nickname,
    @NotNull
    String email,
    @NotNull
    String password,
    String phone_number,
    @NotNull
    RoleType role,
    int active_yn,
    LocalDateTime created_at,
    LocalDateTime updated_at
) {}
