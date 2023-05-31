package cobook.buddywisdom.global.vo;

public enum MemberApiType {
	MENTEES("mentees"), COACHES("coaches");

	private String value;

	MemberApiType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
