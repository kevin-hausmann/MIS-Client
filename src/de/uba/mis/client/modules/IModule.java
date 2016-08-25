/**
 * 
 */
package de.uba.mis.client.modules;

import com.google.gwt.user.client.ui.Panel;

/**
 * @author hausmann
 *
 */
public interface IModule {

	/**
	 * @return Name to represent this module
	 */
	public String getName();
	
	/**
	 * @return A description for this module
	 */
	public String getDescription();
	
	/**
	 * Lifecycle method loading the module.
	 * @param container Widget to add module to
	 */
	public void load(Panel container);
	
	/**
	 * Lifecycle method called to destroy this module.
	 */
	public void unload();
}
