/**
 * 
 */
package de.uba.mis.client.ui.tables;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uba.mis.client.modules.ChangeModule;
import de.uba.mis.client.types.Change;
import de.uba.mis.client.types.Database;
import de.uba.mis.client.ui.DatabaseFilterPopupPanel;
import de.uba.mis.client.ui.Styles;
import de.uba.mis.client.ui.TypeFilterPopupPanel;
import de.uba.mis.client.ui.dialogs.ValuesChangedDialog;

/**
 * @author Kevin Hausmann
 * Table to show changes made in databases.
 */
public class ChangeTable extends ATable {

	/** Whether current result reached limit */
	private boolean truncated = false;
	
	/** Number of databases affect by current set of changes */
	private int numberOfDatabases = 0;
	
	/** Textbox to put hours back into */
	private final TextBox hours = new TextBox();
	
	/** Filter for database */
	private final Button databaseFilterButton = new Button("Database");
	private final DatabaseFilterPopupPanel databaseFilterPanel = new DatabaseFilterPopupPanel();
	
	/** Filter for type */
	private final Button typeFilterButton = new Button("Object");
	private final TypeFilterPopupPanel typeFilterPanel = new TypeFilterPopupPanel();
	
	/** Values changes popup dialog */
	private final ValuesChangedDialog valuesChangedDialog = new ValuesChangedDialog();
	
	/**
	 * Create initial change table.
	 * Use update() to put data.
	 */
	public ChangeTable() {
		setStyleName(Styles.CHANGE_TABLE);
		setWidget(0, 0, hours);
		setWidget(0, 1, databaseFilterButton);
		setWidget(0, 2, typeFilterButton);
		setText(0, 3, "User");
		
		hours.setText(String.valueOf(ChangeModule.DEFAULT_HOURS_BACK));
		hours.setTitle("Length of list (as hours back from now)");
		hours.setStyleName(Styles.HOURS_TEXT_BOX);
				
		databaseFilterButton.setTitle("Filter databases");
		databaseFilterButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (!databaseFilterPanel.isShowing()) databaseFilterPanel.showRelativeTo(databaseFilterButton);
				else databaseFilterPanel.hide();
			}
		});
		
		typeFilterButton.setTitle("Filter types");
		typeFilterButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (!typeFilterPanel.isShowing()) typeFilterPanel.showRelativeTo(typeFilterButton);
				else typeFilterPanel.hide();
			}
		});
		
		setAllCellStyle(0, Styles.CELL);
		setRowStyle(0, Styles.TABLE_HEADER);		
	}
	
	/**
	 * Update change table to show given changes
	 * @param changes Generic changes to show
	 * @param valueChanges Value changes to show
	 */
	public void update(List<Change> changes, Map<Database, List<Change>> valueChanges) {
		// reset fields
		truncated = false;
		numberOfDatabases = 0;
		
		removeAllButFirstRow();
		
		// See whether we have to cut list
		if (changes.size() > ChangeModule.MAX_NUMBER_OF_RESULTS) {
			changes = changes.subList(0, ChangeModule.MAX_NUMBER_OF_RESULTS - 1);
			truncated = true;
		}
		
		Collections.sort(changes);
		
		// Helper for status field
		Set<Database> databases = new HashSet<Database>();
		
		for (Database database : valueChanges.keySet()) {
			// Apply filters
			if (!databaseFilterPanel.accepts(database)) continue;
			if (!typeFilterPanel.showValues()) continue;
			
			int row = getRowCount();
			
			databases.add(database);
			
			Label changed = new Label(valueChanges.get(database).size() + " value(s) changed");
			changed.setStyleName(Styles.TYPE_PREFIX + "value");
			
			setText(row, 0, " ");
			setText(row, 1, database.getDisplayName());
			setWidget(row, 2, changed);
			setWidget(row, 3, createValueButton(database, valueChanges.get(database)));
						
			setAllCellStyle(row, Styles.CELL);
			setRowStyle(row, Styles.VALUES_CHANGED);
		}
		
		for (Change change : changes) {
			// Apply filters
			if (!databaseFilterPanel.accepts(change.getDatabase())) continue;
			if (!typeFilterPanel.accepts(change)) continue;
			
			int row = getRowCount();
			databases.add(change.getDatabase());
			
			setText(row, 0, format.format(change.getDate()));
			setText(row, 1, change.getDatabase().getDisplayName());
			setWidget(row, 2, createChangeWidget(change));
			setText(row, 3, change.getUser());
						
			setAllCellStyle(row, Styles.CELL);
			
			if (getRowCount() % 2 == 1) setRowStyle(row, Styles.EVERY_SECOND_ROW);
		}
		
		numberOfDatabases = databases.size();
	}

	/**
	 * @return Number of hours back from now for changes to display
	 */
	public int getHoursBack() {
		int result;
		
		try {
			result = Integer.valueOf(hours.getText());
		} catch (NumberFormatException e) {
			hours.setText(String.valueOf(ChangeModule.DEFAULT_HOURS_BACK));
			return ChangeModule.DEFAULT_HOURS_BACK;
		}
		
		if (result < 1) {
			result = 1;
			hours.setText(String.valueOf(result));
		}
		
		return result;
	}
	
	/**
	 * @return Whether result table was truncated after limit
	 */
	public boolean isTruncated() {
		return truncated;
	}
	
	/**
	 * @return The number of databases affected by changes
	 */
	public int getNumberOfDatabases() {
		return numberOfDatabases;
	}
	
	private Widget createValueButton(final Database database, final List<Change> changes) {
		return new Button("Show details...", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				valuesChangedDialog.update(changes);
				
				valuesChangedDialog.center();
				valuesChangedDialog.show();
			}
		});
	}
}
