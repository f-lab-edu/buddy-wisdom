package cobook.buddywisdom.member.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private String phoneNumber;
    @NotNull
    private String role;

    private boolean activeYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 비밀번호 암호화
    public static CreateMemberDto of(CreateMemberDto dto, PasswordEncoder passwordEncoder) {
        return CreateMemberDto.builder()
                .id(dto.id)
                .name(dto.name)
                .nickname(dto.nickname)
                .email(dto.email)
                .password(passwordEncoder.encode(dto.password))
                .phoneNumber(dto.phoneNumber)
                .role(dto.role)
                .activeYn(dto.activeYn)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
