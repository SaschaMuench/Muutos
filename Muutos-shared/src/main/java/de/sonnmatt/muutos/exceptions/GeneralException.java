/**
 * 
 */
package de.sonnmatt.muutos.exceptions;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author MuenSasc
 *
 */
public class GeneralException extends Exception implements Serializable, IsSerializable {

	private static final long serialVersionUID = -1249420142845937697L;
	private String message;
	private GeneralExceptionType type;

	public enum GeneralExceptionType implements Serializable, IsSerializable {
		unknownLanguage,
		unknownTenant
	}
	
	public GeneralException() {
		super();
	}

	public GeneralException(String message) {
		super(message);
		this.message = message;
	}
	
	public GeneralException(String message, GeneralExceptionType type) {
		super(message);
		this.message = message;
		this.type = type;
	}
	
	public String getMessage() {
		return this.message;
	}

	public GeneralExceptionType getType() {
		return this.type;
	}	
}