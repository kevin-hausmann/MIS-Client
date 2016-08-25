/**
 * 
 */
package de.uba.mis.client.ui.dialogs;

import java.util.List;

import com.google.gwt.user.client.ui.DialogBox;

import de.uba.mis.client.types.Change;
import de.uba.mis.client.ui.tables.ValueChangeTable;

/**
 * @author Kevin Hausmann
 * Wraps a dialog showing changes in some database
 */
public class ValuesChangedDialog extends DialogBox {

	/** Table showing the actual contents */
	ValueChangeTable table = new ValueChangeTable();
	
	/**
	 * Creates dialog.
	 */
	public ValuesChangedDialog() {
		super(true, false);

		setAnimationEnabled(true);
		setGlassEnabled(true);

		setText("Values changed");
		add(table);
	}

	/**
	 * Create dialog content.
	 * @param changes Value changes to put into content table.
	*/
	public void update(List<Change> changes) {
		table.update(changes);
	}
}
