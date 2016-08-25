package de.uba.mis.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import de.uba.mis.client.modules.ChangeModule;
import de.uba.mis.client.modules.IModule;
import de.uba.mis.client.modules.UserModule;

/**
 * The main page definition of the MESAP Information System (MIS).
 * Entry point classes define <code>onModuleLoad()</code>.
 * @author Kevin Hausmann
 */
public class MesapInformationSystem implements EntryPoint {

	/** The main panel widget to hold modules */
	private final HorizontalPanel mainPanel = new HorizontalPanel();
	
	/** Set of modules */
	private List<IModule> modules = new ArrayList<IModule>();
	
	/**
	 * This is the entry point method.
	 * Creates and adds modules to main panel.
	 */
	@Override
	public void onModuleLoad() {
		// Init the main window
		Window.setTitle("MESAP Information System");
		RootPanel.get("title").add(new Label("MESAP Information System"));
		mainPanel.setStyleName("mainPanel");
		
		// Create modules
		modules.add(new UserModule());
		modules.add(new ChangeModule());
		
		// Add module on/off checkboxes to top panel
		for (final IModule module : modules)
		{
			module.load(mainPanel);
			
			final CheckBox moduleCheck = new CheckBox(module.getName());
			moduleCheck.setTitle(module.getDescription());
			moduleCheck.setValue(true);
			moduleCheck.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if (moduleCheck.getValue()) module.load(mainPanel);
					else module.unload();
				}
			});
			
			RootPanel.get("modules").add(moduleCheck);
		}				
		
		// Add main panel (holding the modules) to the main window
		RootPanel.get("main").add(mainPanel);
	}
}
