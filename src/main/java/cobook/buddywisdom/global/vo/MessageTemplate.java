package cobook.buddywisdom.global.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageTemplate {

	public static final String COMBINED_NAME = "{0}({1})";
	public static final String DATETIME_FORMAT = "yyyy년 MM월 dd일 HH:mm";
	public static final String CREATE_SCHEDULE = "%s님이 {0}로 코칭 일정을 신청했습니다.";
	public static final String REQUEST_CANCEL_SCHEDULE = "%s님이 {0} 코칭 일정 취소 요청을 보냈습니다.";
	public static final String CONFIRM_CANCEL_SCHEDULE = "%s님이 {0} 코칭 일정 취소 요청을 확인했습니다.";
	public static final String UPDATE_SCHEDULE = "%s님이 {0}에서 {1}로 코칭 일정을 변경했습니다.";
}
