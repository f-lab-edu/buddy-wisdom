package cobook.buddywisdom.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorMessage {

	NOT_FOUND_MENTEE_SCHEDULE(HttpStatus.NOT_FOUND, "등록된 멘티 스케줄이 존재하지 않습니다."),
	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "등록된 회원이 존재하지 않습니다."),
	INVALID_CREDENTIALS_EXCEPTION(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),

	;

	private HttpStatus status;
	private String message;

	ErrorMessage(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
