package cobook.buddywisdom.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorMessage {

	// MEMBER
	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "등록된 회원이 존재하지 않습니다."),
	INVALID_CREDENTIALS_EXCEPTION(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
	DUPLICATED_MEMBER_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다."),
	FAILED_CREATE_MEMBER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입에 실패하였습니다."),

	// MENTEE
	NOT_FOUND_MENTEE_SCHEDULE(HttpStatus.NOT_FOUND, "등록된 멘티 스케줄이 존재하지 않습니다."),
	DUPLICATED_MENTEE_SCHEDULE(HttpStatus.BAD_REQUEST, "이미 해당 코칭 일정으로 등록된 스케줄이 존재합니다."),
	NOT_ALLOWED_UPDATE_SCHEDULE(HttpStatus.BAD_REQUEST, "변경 가능 시간이 지났으므로 취소 요청을 진행해 주세요."),

	// COACH
	NOT_FOUND_COACH_SCHEDULE(HttpStatus.NOT_FOUND, "이미 신청이 마감되었거나 존재하지 않는 스케줄입니다."),

	// RELATIONSHIP
	NOT_FOUND_COACHING_RELATIONSHIP(HttpStatus.NOT_FOUND, "매칭된 코칭 팀이 존재하지 않습니다."),

	// CANCEL_REQUEST
	NOT_FOUND_CANCEL_REQUEST(HttpStatus.NOT_FOUND, "등록된 일정 취소 요청이 존재하지 않습니다."),
	CONFIRMED_CANCEL_REQUEST(HttpStatus.BAD_REQUEST, "이미 확인이 완료된 취소 요청입니다."),

	// FEED
	NOT_FOUND_FEED(HttpStatus.NOT_FOUND, "해당하는 피드가 존재하지 않습니다."),
	ALREADY_CHECKED_FEED(HttpStatus.BAD_REQUEST, "이미 확인이 완료되었습니다."),

	// FEEDBACK
	NOT_FOUND_FEEDBACK(HttpStatus.NOT_FOUND, "등록된 피드백이 존재하지 않습니다.")

	;

	private HttpStatus status;
	private String message;

	ErrorMessage(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
