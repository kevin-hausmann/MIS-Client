/**
 * 
 */
package de.uba.mis.client.types.jso;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Javascript overlay type for a change. Uses native methods to get field values.
 * Is wrapped by real Java class definition.
 * 
 * @author Kevin Hausmann
 */
public class ChangeJSO extends JavaScriptObject {

	// Overlay types always have protected, zero argument constructors.
	protected ChangeJSO() {}
	
	/**
	 * @return The name of the database this change was made in.
	 * Might be an identifier rather then the actual name.
	 */
	public final native String getDatabase() /*-{ return this.database; }-*/;
	
	/**
	 * @return The type of object changes by this change.
	 */
	public final native String getType() /*-{ return this.type; }-*/;
	
	/**
	 * @return The changed object's name.
	 */
	public final native String getName() /*-{ return this.name; }-*/;
	
	/**
	 * @return The changed object's id.
	 */
	public final native String getID() /*-{ return this.id; }-*/;
	
	/**
	 * @return The user who changed this object.
	 */
	public final native String getUser() /*-{ return this.user; }-*/;
	
	/**
	 * @return The timepoint of change
	 */
	public final native String getDatetime() /*-{ return this.datetime; }-*/;
}
