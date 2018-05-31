package de.sonnmatt.muutos;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;

public class Tools {

	public static String generateUniqueFieldID(Integer table, Integer field) {
		return NumberFormat.getFormat("00000").format(table) + NumberFormat.getFormat("00000").format(field);
	}

	public static Integer getTableID(String uniqueFieldID) {
		return Integer.valueOf(uniqueFieldID.substring(0, 4));
	}

	public static Integer getFielID(String uniqueFieldID) {
		return Integer.valueOf(uniqueFieldID.substring(5));
	}

	public static String format(final String format, final Object... args) {
		final RegExp regex = RegExp.compile("%[a-z]");
		final SplitResult split = regex.split(format);
		final StringBuffer msg = new StringBuffer();
		for (int pos = 0; pos < split.length() - 1; ++pos) {
			msg.append(split.get(pos));
			msg.append(args[pos].toString());
		}
		msg.append(split.get(split.length() - 1));
		return msg.toString();
	}
	
	public static String isNull(String value) {
		if (value==null)
			return "";
		return value;
	}

}
