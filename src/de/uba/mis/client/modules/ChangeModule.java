/**
 * 
 */
package de.uba.mis.client.modules;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uba.mis.client.types.Change;
import de.uba.mis.client.types.Database;
import de.uba.mis.client.types.jso.ChangeJSO;
import de.uba.mis.client.ui.Styles;
import de.uba.mis.client.ui.tables.ChangeTable;

/**
 * This modules shows changes in MESAP databases.
 * @author Kevin Hausmann
 */
public class ChangeModule implements IModule {

	/** Date format server uses to encode datetimes */
	protected final DateTimeFormat format = DateTimeFormat.getFormat("EEE, dd.MM. 'at' HH:mm");
	
	/** Default value for the length of the changes list */
	public static final int DEFAULT_HOURS_BACK = 24;
	
	/** Maximum number of changes to show */
	public static final int MAX_NUMBER_OF_RESULTS = 500;
	
	/** List of non-value changes we are showing */
	private List<Change> changes = new ArrayList<Change>();;
	
	/** Lists of value changes we are showing */
	private Map<Database, List<Change>> valueChanges  = new HashMap<Database, List<Change>>();
	
	/** Main panel for this module */
	private final VerticalPanel modulePanel = new VerticalPanel();
		
	/** Table showing changes */
	private ChangeTable table = new ChangeTable();
	
	/* Module widgets */
	private final Label status = new Label();
	private final Button refresh = new Button("Refresh!");
	
	/** Server path providing change information */
	//private static final String JSON_URL = "http://172.22.2.96:5050/changes";
	private static final String JSON_URL = "http://localhost:5050/changes";

	/** Id to keep track of JSON requests */
	private int jsonRequestId = 0;
	
	@Override
	public String getName() {
		return "Changes";
	}

	@Override
	public String getDescription() {
		return "The change module display changes made to MESAP databases.";
	}

	@Override
	public void load(Panel container) {
		final HorizontalPanel helperPanel = new HorizontalPanel();
		
		status.setStyleName(Styles.CHANGE_MODULE_STATUS_LABEL);
		refresh.setStyleName(Styles.REFRESH_BUTTON);
		
		refresh.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				updateTable();
			}
		});
		
		helperPanel.add(status);
		helperPanel.add(refresh);
		
		modulePanel.setStyleName("modulePanel");
		modulePanel.add(helperPanel);
		modulePanel.add(table);
		container.add(modulePanel);
		
		updateTable();
	}

	@Override
	public void unload() {
		modulePanel.removeFromParent();
	}
	
	private void updateTable() {
		status.setText("Working...");
		status.setStyleName(Styles.CHANGE_MODULE_STATUS_LABEL_WORKING);
		refresh.setVisible(false);
		
		// Append the name of the callback function to the JSON URL.
		String url = URL.encode(JSON_URL) + "?hours=" + table.getHoursBack() +	"&values=true&callback=";
		
		// Send request to server by replacing RequestBuilder code with a call to a JSNI method.
		getJson(jsonRequestId++, url, this);
	}
	
	/**
	 * Make call to remote server.
	 */
	public native static void getJson(int requestId, String url, ChangeModule handler) /*-{
		var callback = "changeList" + requestId;

		// Create a script element.
		var script = document.createElement("script");
		script.setAttribute("src", url + callback);
		script.setAttribute("type", "text/javascript");
		// MIS Server will deliver data in UTF-8 encoding
		script.setAttribute("charset", "UTF-8");

		// Define the callback function on the window object.
		window[callback] = function(jsonObj) {
			handler.@de.uba.mis.client.modules.ChangeModule::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
			window[callback + "done"] = true;
		}

		// JSON download has 3-second timeout.
		setTimeout(function() {
			if (!window[callback + "done"]) {
				handler.@de.uba.mis.client.modules.ChangeModule::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
			}

			// Cleanup. Remove script and callback elements.
			document.body.removeChild(script);
 			delete window[callback];
			delete window[callback + "done"];
		}, 20000);

		// Attach the script element to the document body.
		document.body.appendChild(script);
	}-*/;

	/**
	 * Handle the response to the request for changes from a remote server.
	 */
	public void handleJsonResponse(JavaScriptObject jso) {
		// Request failed?
		if (jso == null) status.setText("Server does not respond!");
		// No? Then go update table
		else {
			// reads changes and seperates value/non-value items
			readChanges(jso);
						
			table.update(changes, valueChanges);
			
			// Update status label
			if (table.isTruncated()) status.setText("Synced, but result was truncated due to more then " + 
					MAX_NUMBER_OF_RESULTS + " changes ");
			else status.setText("Synced, " + (table.getRowCount() - 1) + " change(s) in " + 
					table.getNumberOfDatabases() + " database(s) ");
			
			Date since = new Date(new Date().getTime() - table.getHoursBack() * 1000 * 3600);
			status.setText(status.getText() + " since " + format.format(since) + ".");
		}
		
		// Stop indicating work
		status.setStyleName(Styles.CHANGE_MODULE_STATUS_LABEL);
		refresh.setVisible(true);
	}

	private void readChanges(JavaScriptObject javaScriptObject) {
		changes.clear();
		valueChanges.clear();
		
		// Get remote result
		JsArray<ChangeJSO> changesArray = asArrayOfChanges(javaScriptObject);
		for (int changeIndex = 0; changeIndex < changesArray.length(); changeIndex++) {
			Change change = new Change(changesArray.get(changeIndex));
			
			// Is value change? Yes...
			if (change.getType().startsWith(Change.VALUE)) {
				Database database = change.getDatabase();
				
				if (!valueChanges.containsKey(database)) 
					valueChanges.put(database, new ArrayList<Change>());
				
				valueChanges.get(database).add(change);
			// No...	
			} else changes.add(change);
		}
	}
	
	/**
	 * Convert the string of JSON into JavaScript objects of type Change.
	 */
	private final native JsArray<ChangeJSO> asArrayOfChanges(JavaScriptObject changes) /*-{return changes;}-*/;
}
