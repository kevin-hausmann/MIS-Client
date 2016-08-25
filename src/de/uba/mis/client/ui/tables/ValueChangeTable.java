/**
 * 
 */
package de.uba.mis.client.ui.tables;

import java.util.Collections;
import java.util.List;

import de.uba.mis.client.types.Change;
import de.uba.mis.client.ui.Styles;

/**
 * @author Kevin Hausmann
 * Table showign value changes
 */
public class ValueChangeTable extends ATable {

	/**
	 * Create the table.
	 */
	public ValueChangeTable() {
		setStyleName(Styles.CHANGE_TABLE);
		setText(0, 0, "Datetime");
		setText(0, 1, "Object");
		setText(0, 2, "User");
		setAllCellStyle(0, Styles.CELL);
		setRowStyle(0, Styles.TABLE_HEADER);
	}
	
	/**
	 * Update table contents
	 * @param changes Value changes to show
	 */
	public void update(List<Change> changes) {
		removeAllButFirstRow();
		
		Collections.sort(changes);

		for (Change change : changes) {
			int row = getRowCount();

			setText(row, 0, format.format(change.getDate()));
			setWidget(row, 1, createChangeWidget(change));
			setText(row, 2, change.getUser());

			setAllCellStyle(row, Styles.CELL);
			
			if (getRowCount() % 2 == 1) setRowStyle(row, Styles.EVERY_SECOND_ROW);
		}
	}
}
