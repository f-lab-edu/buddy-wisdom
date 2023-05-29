package cobook.buddywisdom.global.vo;

public enum MessageTemplate {

	CREATE_SCHEDULE("%s님이 {0}로 코칭 일정을 신청했습니다."),
	REQUEST_CANCEL_SCHEDULE("%s님이 {0} 코칭 일정 취소 요청을 보냈습니다."),
	CONFIRM_CANCEL_SCHEDULE("%s님이 {0} 코칭 일정 취소 요청을 확인했습니다."),
	UPDATE_SCHEDULE("%s님이 {0}에서 {1}로 코칭 일정을 변경했습니다.")
	;

	private String template;

	MessageTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}
}
