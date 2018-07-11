package de.sonnmatt.muutos.DTOs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.sonnmatt.muutos.Tools;

/**
 * @author MuenSasc
 *
 */
public class TextResourcesDTO implements Serializable, IsSerializable {

	private static final long serialVersionUID = 7655582596499259001L;
	private static final String DELIMITER = "__";
	private HashMap<String, DoubleString> data;
	private Set<String> missingData;
	
	public TextResourcesDTO() {
		data = new HashMap<String,DoubleString>();
		missingData = new HashSet<String>();
	}
	
	public void put(String key, String value) {
		DoubleString tString = new DoubleString(key, value);
		data.put(key.toLowerCase(), tString);
	}

	public void putAll(TextResourcesDTO textResourcesDTO) {
		data.putAll(textResourcesDTO.getAll());
	}

	private HashMap<String, DoubleString> getAll() {
		return data;
	}

	public void set(String key, String value) {
		DoubleString tString = new DoubleString(key, value);
		data.put(key.toLowerCase(), tString);
	}

	public int size() {
		return data.size();
	}

	public String get(String key) {
		if (data == null)
			return DELIMITER + key + DELIMITER;

		DoubleString tString = data.get(key.toLowerCase());
		if (tString == null) {
			missingData.add(key);
			return DELIMITER + key + DELIMITER;
		}
			
		String	i18string = tString.get2();
		if ((i18string == null) || (i18string.isEmpty()))
			i18string = "";
		return i18string;
	}

	public String get(String key, Object...args) {
		return Tools.format(get(key), args);
	}

	public Set<String> getMissingData() {
		return missingData;
	}
	
	public Set<String> keySet() {
		Set<String> keys = new HashSet<>();
		data.entrySet().forEach(es -> keys.add(es.getValue().get1()));
		return keys;
	}
	
	public Set<String> ciKeySet() {
		return data.keySet();
	}

}