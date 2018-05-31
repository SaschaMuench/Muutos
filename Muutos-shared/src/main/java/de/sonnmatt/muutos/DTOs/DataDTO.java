/**
 * 
 */
package de.sonnmatt.muutos.DTOs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author MuenSasc
 *
 */
public class DataDTO implements Serializable, IsSerializable {

	public enum dataTypes implements Serializable, IsSerializable {
		Translation,
		View;
		
		@Override
		public String toString() {
			return super.toString();
		}
	}
	
	private static final long serialVersionUID = 6082712199756154718L;
	private HashMap<dataTypes, Object> data;

	private void generalConstructor() {
		data = new HashMap<dataTypes, Object>();
	}
	
	public DataDTO() {
		generalConstructor();
	}

	public DataDTO(Map<dataTypes, Object> properties) {
		generalConstructor();
		for (dataTypes key : properties.keySet()) {
			set(key, properties.get(key));
		}
	}

	public Object get(dataTypes key) {
		return data.get(key);
	}

	public void set(dataTypes key, Object value) {
		data.put(key, value);
	}

}
