/**
 * 
 */
package de.uba.mis.client.types;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;

import de.uba.mis.client.types.jso.UserJSO;

/**
 * Wrapper class for a user javascript object.
 * Implements <code>Comparable</code> in order to allow clever sorting.
 * 
 * @author Kevin Hausmann
 */
public class User implements Comparable<User> {

	/** Javascript user object wrapped */
	private UserJSO userJSO;
	
	/** Date format server uses to encode datetimes */
	private final DateTimeFormat format = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");
	
	/** String server uses to flag offline users */
	private final static String OFFLINE = "offline";
	
	/**
	 * Creates a new wrapper.
	 * @param user User Javascript Object to wrap. Can not be <code>null</code>.
	 */
	public User(UserJSO user) {
		assert(user != null);
		
		this.userJSO = user;
	}
	
	/**
	 * @return The user's name.
	 */
	public String getName() {
		String name = userJSO.getName();
		if (name == null || name.trim().length() == 0) return "unnamed user";
		
		// Hausmann, Kevin --> Kevin Hausmann
		String[] pieces = name.split(",");
		
		if (pieces.length == 1) return pieces[0].trim();
		else return pieces[1].trim() + " " + pieces[0].trim();
	}

	/**
	 * @return The user's login status.
	 */
	public boolean isLoggedIn() {
		return !userJSO.loggedIn().equals(OFFLINE);
	}
	
	/**
	 * @return Whether the login time was yesterday or earlier (indicates stale login)
	 */
	public boolean isLoggedInBeforeToday() {
		if (!isLoggedIn()) return false;
		
		Date loginDate = getLoggedInSince();
		if (loginDate == null) return false;
		
		return CalendarUtil.getDaysBetween(loginDate, new Date()) > 0;
	}
	
	/**
	 * @return Timepoint of login for this user, <code>null</code> if user is offline.
	 */
	public Date getLoggedInSince() {
		if (!isLoggedIn()) return null;
		else
			try {
				return format.parse(userJSO.loggedIn());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			}
	}
	
	/**
	 * @return Timepoint of last user log off, <code>null</code> if user has never been online.
	 */
	public Date getLastSeen() {
		try {
			return format.parse(userJSO.lastSeen());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return Set of database names the user is currently logged into.
	 */
	public Set<Database> getLoggedIntoDatabases() {
		Set<Database> databases = new HashSet<Database>();
		
		for (int index = 0; index < userJSO.loggedIntoDatabases().length(); index++)
			databases.add(Database.getByID(userJSO.loggedIntoDatabases().get(index)));
		
		return databases;
	}
	
	@Override
	public int compareTo(User otherUser) {
		if (this == otherUser) return 0;
		
		if (!isLoggedIn() && !otherUser.isLoggedIn()) return -getLastSeen().compareTo(otherUser.getLastSeen());
		else if (isLoggedIn() && !otherUser.isLoggedIn()) return -1;
		else if (!isLoggedIn() && otherUser.isLoggedIn()) return 1;
		else return getName().compareTo(otherUser.getName());
	}
}
