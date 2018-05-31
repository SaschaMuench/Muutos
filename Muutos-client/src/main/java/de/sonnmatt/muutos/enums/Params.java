/**
 * 
 */
package de.sonnmatt.muutos.enums;

/**
 * @author MuenSasc
 *
 */
public enum Params {
	COMPANY("company"),
	USER("user"),
	FORM("form");
	
	private String param;
	
	private Params(String param) {
		this.param = param;
	}
	
	@Override
	public String toString() {
		return this.param;
	}
}
