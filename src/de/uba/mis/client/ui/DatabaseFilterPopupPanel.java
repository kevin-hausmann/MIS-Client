/**
 * 
 */
package de.uba.mis.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uba.mis.client.types.Database;

/**
 * Database filter dialog.
 * 
 * @author Kevin Hausmann
 */
public class DatabaseFilterPopupPanel extends DecoratedPopupPanel {
	
	/** The actual filter setting. */
	private Set<Database> databaseFilter = new HashSet<Database>();
	
	public DatabaseFilterPopupPanel() {
		super(false);
		
		createFilterWidget();
	}
	
	/**
	 * Checks whether current filter setting accepts given database.
	 * @param database The database to test.
	 * @return Whether the current filter setting allow this database to be displayed.
	 */
	public boolean accepts(Database database) {
		return databaseFilter.isEmpty() || databaseFilter.contains(database);
	}
	
	private void createFilterWidget() {
		VerticalPanel panel = new VerticalPanel();
		
		for (final Database database : Database.values()) {
			CheckBox box = new CheckBox(database.getLongDisplayName());
			
			box.setValue(true);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if (event.getValue()) databaseFilter.add(database);
					else databaseFilter.remove(database);
				}
			});
			
			panel.add(box);
			databaseFilter.add(database);
		}
		
		setWidget(panel);
	}
}
