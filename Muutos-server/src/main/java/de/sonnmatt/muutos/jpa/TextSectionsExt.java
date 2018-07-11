package de.sonnmatt.muutos.jpa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.sonnmatt.muutos.enums.TextSections;

public final class TextSectionsExt extends TextSections implements Serializable, IsSerializable {

	private static final long serialVersionUID = -4081889816228157854L;

	public static List<String> values() {
		List<String> values = new ArrayList<String>();
		Field[] fields = TextSectionsExt.class.getDeclaredFields();
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers()) && String.class.equals(f.getType())) {
				values.add(f.getName());
			}
		}
		return values;
	}
}
