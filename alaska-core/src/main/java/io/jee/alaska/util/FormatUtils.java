package io.jee.alaska.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtils {

	public static String formatDate(String format, Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String formatDateNow(String format) {
		return formatDate(format, new Date());
	}

	public static String formatDateNow() {
		return formatDate("yyyy-MM-dd HH:mm:ss", new Date());
	}

	public static String formatDate(Date date) {
		return formatDate("yyyy-MM-dd HH:mm:ss", date);
	}

	public static Date formatDate(String format, String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			Date dateData = formatter.parse(date);
			return dateData;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static Date formatDate(String date) {
		return formatDate("yyyy-MM-dd HH:mm:ss", date);
	}
}
