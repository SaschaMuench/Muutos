/**
 * 
 */
package de.sonnmatt.muutos.DTOs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.sonnmatt.muutos.Tools;
import de.sonnmatt.muutos.enums.UserFields;
import de.sonnmatt.muutos.enums.UserPermission;

/**
 * @author MuenSasc
 *
 */
public class UserDTO implements Serializable, IsSerializable {
	private static final long serialVersionUID = -2676558577427340857L;
	
	private HashMap<String, String> data;
	private HashMap<String, Boolean> permission;

	private void generalConstructor() {
		data = new HashMap<String, String>();
		permission = new HashMap<String, Boolean>();
	}
	
	public UserDTO() {
		generalConstructor();
	}

	public UserDTO(Map<String, String> properties) {
		generalConstructor();
		for (String key : properties.keySet()) {
			set(key, properties.get(key));
		}
	}

	public String get(Integer key) {
		System.out.println("key: " + key + " : " + data.get(key.toString()));
		return data.get(key.toString());
	}
	
	public String get(String key) {
		return data.get(key);
	}

	public String get(UserFields key) {
		return Tools.isNull(data.get(key.toString()));
	}

	public String getFullName() {
		return get(UserFields.FirstName) + " " + get(UserFields.LastName);
	}

	public void set(String key, String value) {
		data.put(key, value);
	}

	public void set(UserFields key, String value) {
		data.put(key.toString(), value);
	}

	public void set(UserFields key, boolean value) {
		this.set(key, String.valueOf(value));
	}

	public Boolean getPermission(UserPermission key) {
		return permission.get(key.toString());
	}
	

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj.getClass() != this.getClass())
			return false;
		UserDTO user = (UserDTO) obj;
		return user.get(UserFields.Login).equalsIgnoreCase(this.get(UserFields.Login));
	}

	public int compareTo(ICellTableData o) {
		return (o == null || o.getColumnData(0) == null) ? -1 : -o.getColumnData(0).compareTo(get(UserFields.Login));
	}

	public String getColumnData(Integer column) {
		switch (column) {
		case 0:
			return get(UserFields.UID);
		case 1:
			return get(UserFields.FirstName) + " " + get(UserFields.LastName);
		case 2:
			return get(UserFields.Street) + " " + get(UserFields.ZIP) + " " + get(UserFields.City);
		case 3:
			return get(UserFields.Phone);
		case 4:
			return get(UserFields.Mobile);
		case 5:
			return get(UserFields.Login);
		case 6:
			return get(UserFields.Active);
		case 7:
			return get(UserFields.LoggedIn);

		default:
			break;
		}
		return null;
	}

	public Integer getIntegerColumnData(Integer column) {
		switch (column) {
		case 6:
			return Integer.valueOf(get(UserFields.Active));
		case 7:
			return Integer.valueOf(get(UserFields.LoggedIn));
		default:
			break;
		}
		return null;
	}

	/*public Class<?> getColumnFormat(Integer column) {
		switch (column) {
		case 0:
			return Integer.class;
		case 7:
			return Integer.class;

		default:
			return String.class;
		}
	}*/

	/*public Class<?> getColumnFormat(String column) {
		switch (column) {
		case "UID":
			return Integer.class;
		case "7":
			return Integer.class;

		default:
			return String.class;
		}
	}*/

	public String getSessionId() {
		return get(UserFields.SessionID);
	}

}
