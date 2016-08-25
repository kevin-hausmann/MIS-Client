/**
 * 
 */
package de.uba.mis.client.ui.tables;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.uba.mis.client.types.Change;
import de.uba.mis.client.ui.Styles;

/**
 * @author Kevin Hausmann
 * Abstract table, defines some confy methods.
 */
public abstract class ATable extends FlexTable {

	/** Date format server uses to encode datetimes */
	protected final DateTimeFormat format = DateTimeFormat.getFormat("dd.MM. HH:mm");
	
	/**
	 * Set CSS style for given row
	 * @param row The row
	 * @param style The style name
	 */
	protected void setRowStyle(int row, String style) {
		getRowFormatter().setStyleName(row, style);
	}
	
	/**
	 * Set CSS style for given cell
	 * @param row Row cell is in
	 * @param cell Cell number
	 * @param style The style name
	 */
	protected void setCellStyle(int row, int cell, String style) {
		getCellFormatter().setStyleName(row, cell, style);
	}
	
	/**
	 * Set CSS style for all cells in given row
	 * @param row Row cell are in
	 * @param style The style name
	 */
	protected void setAllCellStyle (int row, String style) {
		for (int cell = 0; cell < getCellCount(row); cell++) 
			setCellStyle(row, cell, style);
	}
	
	/**
	 * Remove all but first row from table
	 */
	protected void removeAllButFirstRow() {
		int rows = getRowCount();
		for (int row = 1; row < rows; row++)
			removeRow(1);
	}
	
	/**
	 * Wraps creation of change table cell widget showing the actual
	 * object information for the changed artifact.
	 * @param change Change object to display information of
	 * @return Widget to be used as the change table cell
	 */
	protected Widget createChangeWidget(Change change) {
		Label label = new Label();
		
		// Style
		label.setStyleName(Styles.TYPE_PREFIX + change.getType().toLowerCase());
		
		// Text part 1
		if (change.getType().startsWith(Change.VALUE))
			label.setText("Value" + " " + change.getType().split(" ")[1] + ": ");
		else label.setText(change.getTypeLabel() + ": ");
		
		// Text part 2, use name of changed object if not empty
		if (change.getName() != null && change.getName().length() != 0) 
			label.setText(label.getText() + change.getName());
		else label.setText(label.getText() + change.getID());
		
		// Cut text if needed
		if (label.getText().length() > 50) 
			label.setText(label.getText().substring(0, 45).trim() + "...");
		
		// Tooltip
		label.setTitle(change.getName() + " (" + change.getID() + ")");
		
		return label; 
	}
}
