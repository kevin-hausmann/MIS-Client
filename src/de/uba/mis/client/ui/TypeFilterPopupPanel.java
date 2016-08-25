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

import de.uba.mis.client.types.Change;

/**
 * Type filter panel.
 * 
 * @author Kevin Hausmann
 */
public class TypeFilterPopupPanel extends DecoratedPopupPanel {

	
	/** The actual filter setting */
	private Set<String> typeFilter = new HashSet<String>();
	
	public TypeFilterPopupPanel() {
		super(false);
		
		createFilterWidget();
	}
	
	/**
	 * Checks whether current filter setting accepts given change.
	 * @param change The change to test.
	 * @return Whether the current filter setting allow this change to be displayed.
	 */
	public boolean accepts(Change change) {
		String type = change.getType();
		
		if (type.startsWith(Change.VALUE)) return typeFilter.contains(Change.VALUE);
		else return typeFilter.contains(type);
	}
	
	/**
	 * Check whether value changes should be included
	 * @return true if included
	 */
	public boolean showValues() {
		return typeFilter.contains(Change.VALUE);
	}
		
	private void createFilterWidget() {
		VerticalPanel panel = new VerticalPanel();
		
		panel.add(buildCheckBox(Change.REPORT, "Report"));
		panel.add(buildCheckBox(Change.CALCULATION, "Calculation method"));
		panel.add(buildCheckBox(Change.SERIES, "Time series"));
		panel.add(buildCheckBox(Change.VALUE, "Value"));
		panel.add(buildCheckBox(Change.VIEW, "View (filter)"));
		panel.add(buildCheckBox(Change.DESCRIPTOR, "Descriptor"));
		panel.add(buildCheckBox(Change.TREE, "Tree"));
		panel.add(buildCheckBox(Change.CRF, "CRF variable"));
				
		setWidget(panel);
	}

	private CheckBox buildCheckBox(final String type, String label) {
		final CheckBox box = new CheckBox(label);
		box.setValue(true);
		typeFilter.add(type);
		
		box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (box.getValue()) typeFilter.add(type);
				else typeFilter.remove(type);
			}
		});
		
		return box;
	}
}
