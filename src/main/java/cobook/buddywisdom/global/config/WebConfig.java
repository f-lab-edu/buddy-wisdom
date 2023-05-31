package cobook.buddywisdom.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cobook.buddywisdom.global.util.MemberApiTypeConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new MemberApiTypeConverter());
	}
}
