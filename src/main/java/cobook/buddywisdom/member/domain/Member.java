package cobook.buddywisdom.member.domain;


import cobook.buddywisdom.global.security.domain.vo.RoleType;
import cobook.buddywisdom.coach.dto.CreateMemberDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record Member(
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
) {
    public static Member of(CreateMemberDto dto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .id(dto.getId())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone_number(dto.getPhone_number())
                .role(dto.getRole())
                .active_yn(1)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
    }
}
