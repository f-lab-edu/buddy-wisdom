package cobook.buddywisdom.member.dto;

import cobook.buddywisdom.global.security.domain.vo.RoleType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateMemberDto {

    @Nullable
    private Long id;
    @NotNull
    private String name;
    private String nickname;
    @Email
    private String email;
    @NotNull
    private String password;
    private String phone_number;
    @NotNull
    private RoleType role;
    private int active_yn = 1;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // 비밀번호 암호화
    public static CreateMemberDto of(CreateMemberDto dto, String passwordEncoder) {
        return CreateMemberDto.builder()
                .id(dto.id)
                .name(dto.name)
                .nickname(dto.nickname)
                .email(dto.email)
                .password(passwordEncoder)
                .phone_number(dto.phone_number)
                .role(dto.role)
                .active_yn(dto.active_yn)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
    }

}
