package de.uba.mis.client.types.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * Javascript overlay type for a user. Uses native methods to get field values.
 * Is wrapped by real Java class definition.
 * 
 * @author Kevin Hausmann
 */
public class UserJSO extends JavaScriptObject {

	// Overlay types always have protected, zero argument constructors.
	protected UserJSO() {}

	/**
	 * @return The name of the user.
	 */
	public final native String getName() /*-{ return this.name; }-*/;

	/**
	 * @return The login status of this user: either a <code>DateTime</code> for the timepoint of login
	 * or a special string for flagging the offline status.
	 */
	public final native String loggedIn() /*-{ return this.loggedIn; }-*/;
	
	/**
	 * @return An array of database names the user is currently logged into. 
	 */
	public final native JsArrayString loggedIntoDatabases() /*-{ return this.databases; }-*/;
	
	/**
	 * @return A <code>DateTime</code> for the last log off for this user.
	 */
	public final native String lastSeen() /*-{ return this.lastSeenOnline; }-*/;	
}
