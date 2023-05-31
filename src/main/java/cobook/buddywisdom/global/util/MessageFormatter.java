package cobook.buddywisdom.global.util;


@FunctionalInterface
public interface MessageFormatter {
	String format(String template, String ...args);
}
