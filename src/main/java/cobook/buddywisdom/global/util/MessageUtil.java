package cobook.buddywisdom.global.util;

import static cobook.buddywisdom.global.vo.MessageTemplate.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class MessageUtil {

	public String convertToString(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

		return localDateTime.format(formatter);
	}
}
