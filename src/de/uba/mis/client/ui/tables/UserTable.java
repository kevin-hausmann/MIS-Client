/**
 * 
 */
package de.uba.mis.client.ui.tables;

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import de.uba.mis.client.types.User;
import de.uba.mis.client.ui.Styles;
import de.uba.mis.client.ui.dialogs.DatabaseDialog;

/**
 * @author Kevin Hausmann
 * Table to list users
 */
public class UserTable extends ATable {

	/** How many users are logged in? */
	private int onlineUsers = 0;
	
	/* Column names */
	private static final String ONLINE_LAST_SEEN_COL_LABEL = "Online/Last seen";
	private static final String NAME_COL_LABEL = "Name";
	
	/** Date format server uses to encode datetimes */
	private final DateTimeFormat format = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm");
		
	/**
	 * Create the user table.
	 */
	public UserTable() {
		setStyleName(Styles.USER_TABLE);
		setText(0, 0, NAME_COL_LABEL);
		setText(0, 1, ONLINE_LAST_SEEN_COL_LABEL);
		setAllCellStyle(0, Styles.CELL);
		setRowStyle(0, Styles.TABLE_HEADER);
	}

	/**
	 * @return Number of online users.
	 */
	public int getOnlineUsers() {
		return onlineUsers;
	}
	
	/**
	 * Generate the actual table
	 * @param users Users to show.
	 */
	public void update(List<User> users) {
		// Reset fields
		onlineUsers = 0;
		
		removeAllButFirstRow();
		
		Collections.sort(users);
				
		for (final User user : users) {
			int row = getRowCount();
			if (user.isLoggedIn()) onlineUsers++;
			
			setText(row, 0, user.getName());
			
			if (user.isLoggedIn()) setWidget(row, 1, CreateUserButton(user));
			else setText(row, 1, format.format(user.getLastSeen()));
			
			setAllCellStyle(row, Styles.CELL);
						
			if (user.isLoggedInBeforeToday()) setRowStyle(row, Styles.STALE_LOGIN);
			else if (user.isLoggedIn()) setRowStyle(row, Styles.LOGGED_IN);
			else if (getRowCount() % 2 == 1) setRowStyle(row, Styles.EVERY_SECOND_ROW);
		}
	}
	
	private Widget CreateUserButton(final User user) {
		return new Button("Since " + format.format(user.getLoggedInSince()) + " (" +
				user.getLoggedIntoDatabases().size() + ")", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DatabaseDialog dialog = new DatabaseDialog(user);
				
				dialog.center();
				dialog.show();
			}
		});
	}
}
