/**
 * 
 */
package de.sonnmatt.muutos;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.http.client.URL;
import de.sonnmatt.muutos.enums.Params;
import de.sonnmatt.muutos.enums.Tokens;

/**
 * @author MuenSasc
 *
 */
public class URLHandler {

	private String					urlToken	= "";
	private HashMap<String, String>	urlParams	= new HashMap<String, String>();

	private Logger logger = Logger.getLogger("Muutos");

	/**
	 * Constructor
	 */
	public URLHandler() {
	}

	/**
	 * Constructor
	 * 
	 * @param urlHash
	 *            the urlHash to set and parsed
	 */
	public URLHandler(String urlHash) {
		setUrlHash(urlHash);
	}

	/**
	 * @param key
	 *            the parameter to set the value for
	 * @param value
	 *            the value to be set
	 * @return the value for the parameter or null if the key is not registered
	 */
	public void addParameter(Params key, String value) {
		urlParams.put(key.toString(), value);
	}

	/**
	 * @param key
	 *            the parameter to get the value for
	 * @return the value for the parameter or null if the key is not registered
	 */
	public String getParameter(Params key) {
		return urlParams.get(key.toString());
	}

	/**
	 * @return the URL parameter map
	 */
	public HashMap<String, String> getUrlParammeterMap() {
		return urlParams;
	}

	/**
	 * @param urlParams
	 *            the URL parameter map to be set
	 */
	public void setUrlParameterMap(HashMap<String, String> urlParams) {
		this.urlParams = urlParams;
	}

	/**
	 * @return the complete url without #
	 */
	public String getUrl() {
		String urlHash = urlToken + "?";
		if (urlParams.size() > 0) {
			for (Map.Entry<String, String> entry : urlParams.entrySet()) {
				urlHash += entry.getKey() + "=" + URL.encode(entry.getValue()) + "&";
			}
			urlHash = urlHash.substring(0, urlHash.length() - 1);
		}
		return urlHash;
	}

	/**
	 * @return the complete urlHash
	 */
	public String getUrlHash() {
		String urlHash = "#" + urlToken + "?";
		if (urlParams.size() > 0) {
			for (Map.Entry<String, String> entry : urlParams.entrySet()) {
				urlHash += entry.getKey() + "=" + URL.encode(entry.getValue()) + "&";
			}
			urlHash = urlHash.substring(0, urlHash.length() - 1);
		}
		return urlHash;
	}

	/**
	 * @param urlHash
	 *            the urlHash to set and parsed
	 */
	public void setUrlHash(String urlHash) {
		this.urlParams.clear();
		this.urlToken = "";

		logger.log(Level.FINER, this.getClass().getSimpleName() + " setUrlHash(): " + urlHash);
		if (urlHash != null && urlHash.length() > 2) {
			String qs = "";
			int paramStart = 0;
			if (urlHash.startsWith("#")) {
				paramStart = urlHash.indexOf("?") + 1;
				urlToken = paramStart == 0 ? urlHash.substring(1, urlHash.length()) : urlHash.substring(1, paramStart - 1);
				logger.log(Level.FINER, this.getClass().getSimpleName() + " setUrlHash() urlToken: " + urlToken);
			} else {
				if (urlHash.indexOf("?") > 1) {
					paramStart = urlHash.indexOf("?") + 1;
					urlToken = urlHash.substring(1, paramStart - 1);
				} else {
					paramStart = 1;
				}
			}

			qs = urlHash.substring(paramStart);

			for (String kvPair : qs.split("&")) {
				String[] kv = kvPair.split("=", 2);

				String key = kv[0];
				if (key.isEmpty()) {
					continue;
				}

				String val = kv.length > 1 ? kv[1] : "";
				try {
					val = URL.decodeQueryString(val);
				} catch (JavaScriptException e) {
					GWT.log("Cannot decode a URL query string parameter=" + key + " value=" + val, e);
				}
				this.urlParams.put(key, val);
			}
		}
	}

	/**
	 * @return the urlToken
	 */
	public Tokens getUrlToken() {
		logger.log(Level.FINER, this.getClass().getSimpleName() + " getUrlToken() : ENTRY");
		return Tokens.valueOf(urlToken.toUpperCase());
	}

	/**
	 * @param urlToken
	 *            the urlToken to set
	 */
	public void setUrlToken(Tokens urlToken) {
		this.urlToken = urlToken.toString();
	}

}
