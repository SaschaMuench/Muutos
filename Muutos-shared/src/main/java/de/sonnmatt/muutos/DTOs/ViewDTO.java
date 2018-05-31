package de.sonnmatt.muutos.DTOs;

import java.io.Serializable;
//import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.sonnmatt.muutos.enums.TranslationSections;

public class ViewDTO implements Serializable, IsSerializable {

	private static final long serialVersionUID = 158110508718377779L;
//	private static final String DELIMITER = "__";
//	private HashMap<String, Boolean> data;
//	private HashMap<String, Boolean> missingData;

	public ViewDTO() {
//		data = new HashMap<String, Boolean>();
	}
	
	public void set(String key, Boolean value, Boolean defaultValue) {
//		if (value.equals(null)) {
//			data.put(section + DELIMITER + key, defaultValue);
//		} else {
//			data.put(section + DELIMITER + key, value);
//		}
//		data.put(section + DELIMITER + key + "__DEFAULT", defaultValue);
	}

	public void set(String key, Boolean value) {
//		data.put(section.toString() + DELIMITER + key, value);
	}

	public Boolean get(String key) {
//		Boolean i18Bool = null;
//		
//		if (data.equals(null))
//			return true;
//		try {
//			i18Bool = data.get(section + DELIMITER + key);
//		} catch (Exception e) {
//			i18Bool = true;
//			missingData.put(section + DELIMITER + key, true);
//		}
//		if (i18Bool == null) i18Bool = data.get(section + DELIMITER + key + "__DEFAULT");
//		if (i18Bool == null) i18Bool = true;
//		return i18Bool;
		return true;
	}
}
