package cobook.buddywisdom.member.domain;

import java.time.LocalDateTime;

import cobook.buddywisdom.global.security.domain.vo.RoleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
	private Long id;
	private String name;
	private String nickname;
	private String email;
	private String password;
	private String phoneNumber;
	private RoleType role;
	private boolean activeYn;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
