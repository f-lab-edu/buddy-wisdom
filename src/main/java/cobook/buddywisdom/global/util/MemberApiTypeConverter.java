package cobook.buddywisdom.global.util;

import org.springframework.core.convert.converter.Converter;

import cobook.buddywisdom.global.vo.MemberApiType;

public class MemberApiTypeConverter implements Converter<String, MemberApiType> {
	@Override
	public MemberApiType convert(String memberApiType) {
		return MemberApiType.valueOf(memberApiType.toUpperCase());
	}
}
