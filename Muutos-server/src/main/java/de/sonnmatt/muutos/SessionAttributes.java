package de.sonnmatt.muutos;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class SessionAttributes {
	public static final String SESSATTR_TENANT_ID = "TenantId";
	public static final String SESSATTR_LANGUAGE = "Language";
	public static final String SESSATTR_PARAMS = "Params";
	public static final String SESSATTR_SESSION_ID = "SessionId";
	public static final String SESSATTR_LOGIN = "Login";
	public static final String SESSATTR_USER = "User";
	public static final String SESSATTR_USER_ID = "UserId";

	public static List<String> values() {
		List<String> values = new ArrayList<String>();
		Field[] fields = SessionAttributes.class.getDeclaredFields();
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers()) && String.class.equals(f.getType())) {
				values.add(f.getName());
			}
		}
		return values;
	}
}
