/**
 * 
 */
package de.uba.mis.client.types;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

import de.uba.mis.client.types.jso.ChangeJSO;

/**
 * Wrapper class for a change javascript object.
 * Implements <code>Comparable</code> in order to allow clever sorting.
 * 
 * @author Kevin Hausmann
 */
public class Change implements Comparable<Change> {
	
	/** String constants for change types */
    public static String REPORT = "REPORT";
    public static String CALCULATION = "CALCULATION";
    public static String TREE = "TREE";
    public static String DESCRIPTOR = "DESCRIPTOR";
    public static String SERIES = "SERIES";
    public static String VALUE = "VALUE";
    public static String VIEW = "VIEW";
    public static String CRF = "CRF";
    
	/** Wrapped java script object */
	private ChangeJSO changeJSO;
	
	/** Date format server uses to encode datetimes */
	private final DateTimeFormat format = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");
	
	/**
	 * Creates new wrapper.
	 * @param changeJSO Change JavaScript object to wrap. Can not be <code>null</code>.
	 */
	public Change(ChangeJSO changeJSO) {
		assert(changeJSO != null);
		
		this.changeJSO = changeJSO;
	}

	public Date getDate()
	{
		try {
			return format.parse(changeJSO.getDatetime());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Database getDatabase() {
		return Database.getByID(changeJSO.getDatabase());
	}
	
	public String getUser() {
		String user = changeJSO.getUser();
		if (user == null || user.trim().length() == 0) return "unnamed user";
		
		// Hausmann, Kevin --> Kevin Hausmann
		String[] pieces = user.split(",");
		
		if (pieces.length == 1) return pieces[0].trim();
		else return pieces[1].trim() + " " + pieces[0].trim();
	}
	
	public String getType() {
		return changeJSO.getType();
	}
	
	/**
	 * @return The change's type as a nicely readable label string
	 */
	public String getTypeLabel() {
		if (getType().equals(REPORT)) return "Report";
		else if (getType().equals(VIEW)) return "View (filter)";
		else if (getType().equals(CALCULATION)) return "Calculation method";
		else if (getType().equals(CRF)) return "CRF variable";
		else if (getType().equals(TREE)) return "Tree";
		else if (getType().equals(SERIES)) return "Time series";
		else if (getType().equals(DESCRIPTOR)) return "Descriptor";
		else if (getType().startsWith(VALUE)) return "Value" + " " + getType().split(" ")[1];
		else return "Change";
	}
	
	public String getName() {
		return changeJSO.getName();
	}
	
	public String getID() {
		return changeJSO.getID();
	}
	
	@Override
	public String toString() {
		return changeJSO.getType() + ": " + changeJSO.getName() + " (" + changeJSO.getID() + ")";
	}
	
	@Override
	public int compareTo(Change anotherChange) {
		return -getDate().compareTo(anotherChange.getDate());
	}
}
