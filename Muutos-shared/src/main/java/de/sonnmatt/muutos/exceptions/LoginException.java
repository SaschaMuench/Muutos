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
public class LoginException extends Exception implements Serializable, IsSerializable {

	private static final long serialVersionUID = -1806235056560061607L;
	private String message;
	private LoginExceptionType type;

	public enum LoginExceptionType implements Serializable, IsSerializable {
		userUnknown,
		passwordWrong,
		loginLocked,
		tooManyUsers,
		generalError
	}
	
	public LoginException() {
		super();
	}

	public LoginException(String message) {
		super(message);
		this.message = message;
	}
	
	public LoginException(String message, LoginExceptionType type) {
		super(message);
		this.message = message;
		this.type = type;
	}
	
	public String getMessage() {
		return this.message;
	}

	public LoginExceptionType getType() {
		return this.type;
	}	
}