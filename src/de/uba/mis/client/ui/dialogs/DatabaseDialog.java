/**
 * 
 */
package de.uba.mis.client.ui.dialogs;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;

import de.uba.mis.client.types.Database;
import de.uba.mis.client.types.User;

/**
 * @author Kevin Hausmann
 * Wraps a dialog showing databases user is logged into.
 */
public class DatabaseDialog extends DialogBox {

	/**
	 * Creates dialog.
	 * @param user User to display databases for.
	 */
	public DatabaseDialog(User user) {
		super(true, false);

		setAnimationEnabled(true);
		setGlassEnabled(true);

		setText("Databases");
		
		String list = "";
		for (Database database : user.getLoggedIntoDatabases())
			list += "- " + (database == null ? "Unknown database" : database.getLongDisplayName()) + "<br>";
		
		add(new HTML(list));
	}
}
