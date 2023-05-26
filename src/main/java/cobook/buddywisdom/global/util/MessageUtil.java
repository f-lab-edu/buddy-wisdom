package cobook.buddywisdom.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class MessageUtil {

	public String convertToString(LocalDateTime scheduleDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

		return scheduleDateTime.format(formatter);
	}
}
