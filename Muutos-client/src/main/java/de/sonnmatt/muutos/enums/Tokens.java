/**
 * Tokens for browser path and details
 */
package de.sonnmatt.muutos.enums;

/**
 * @author MuenSasc
 *
 */
public enum Tokens {
	LOGIN("login"),
	LOGOFF("logoff"),
	APP("app"),
	TIMEOUT("timeout");

	private final String token;
	
	private Tokens(String token) {
		this.token = token;
	}
	
	@Override
	public String toString() {
		return this.token;
	}

}
