/**
 * 
 */
package de.uba.mis.client.modules;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uba.mis.client.types.User;
import de.uba.mis.client.types.jso.UserJSO;
import de.uba.mis.client.ui.Styles;
import de.uba.mis.client.ui.tables.UserTable;

/**
 * The user module list all Mesap users and allows some
 * overview on who's online at the moment. 
 * @author Kevin Hausmann
 */
public class UserModule implements IModule {

	/** The users shown */
	private ArrayList<User> users = new ArrayList<User>();
	
	/** Main panel for this module */
	private final VerticalPanel modulePanel = new VerticalPanel();
	
	/* Module widgets */
	private final Label status = new Label();
	private final TextBox refresh = new TextBox();
	private final Label minutes = new Label();
	
	/** The refresh timer */
	private Timer refreshTimer;
	
	/** The preset refresh interval (in minutes) */
	private final static int DEFAULT_REFRESH_INTERVAL = 3;
	
	/** Table showing users and their status */
	private UserTable table = new UserTable();

	/** Server path providing user information */
	//private static final String JSON_URL = "http://172.22.2.96:5050/users";
	private static final String JSON_URL = "http://localhost:5050/users";
	
	/** Id to keep track of JSON requests */
	private int jsonRequestId = 0;
	
	@Override
	public String getName() {
		return "Users";
	}
	
	@Override
	public String getDescription() {
		return "The user module list all Mesap users and allows some overview on who's online at the moment.";
	}

	@Override
	public void load(Panel container) {
		final HorizontalPanel helperPanel = new HorizontalPanel();
		
		status.setStyleName(Styles.STATUS_LABEL);
		
		refresh.setText(String.valueOf(DEFAULT_REFRESH_INTERVAL));
		refresh.setTitle("Set refresh interval (minutes)");
		refresh.setStyleName(Styles.REFRESH_TEXT_BOX);
		refresh.addChangeHandler(new ChangeHandler(){
			
			@Override
			public void onChange(ChangeEvent event) {
				setRefreshTimer();
			}	
		});
		
		minutes.setStyleName(Styles.MINUTES_LABEL);
		
		helperPanel.add(status);
		helperPanel.add(refresh);
		helperPanel.add(minutes);
		
		modulePanel.setStyleName(Styles.MODULE_PANEL);
		modulePanel.add(helperPanel);
		modulePanel.add(table);
		container.add(modulePanel);
		
		refreshTimer = new Timer() {
			
			@Override
			public void run() {
				updateTable();
			}
		};
		
		setRefreshTimer();
		updateTable();
	}

	@Override
	public void unload() {
		refreshTimer.cancel();
		modulePanel.removeFromParent();
	}
	
	private void setRefreshTimer() {
		try {
			int interval = Integer.parseInt(refresh.getText());
			if (interval < 1) throw new NumberFormatException("Interval should be >= 1");
			
			refreshTimer.scheduleRepeating(interval * 1000 * 60);
			status.setText("Refresh interval set");
			
			if (interval == 1) minutes.setText("minute");
			else minutes.setText("minutes");
		} catch (NumberFormatException e) {
			refreshTimer.cancel();
			status.setText("Bad number, timer cancelled");
		}
	}
	
	private void updateTable() {
		status.setText("Working...");
		status.setStyleName(Styles.STATUS_LABEL_WORKING);
			
		// Append the name of the callback function to the JSON URL.
		String url = URL.encode(JSON_URL) + "?callback=";

		// Send request to server by replacing RequestBuilder code with a call to a JSNI method.
		getJson(jsonRequestId++, url, this);
	}
	
	/**
	 * Make call to remote server.
	 */
	public native static void getJson(int requestId, String url, UserModule handler) /*-{
		var callback = "userList" + requestId;

		// Create a script element.
		var script = document.createElement("script");
		script.setAttribute("src", url + callback);
		script.setAttribute("type", "text/javascript");
		// MIS Server will deliver data in UTF-8 encoding
		script.setAttribute("charset", "UTF-8");

		// Define the callback function on the window object.
		window[callback] = function(jsonObj) {
			handler.@de.uba.mis.client.modules.UserModule::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(jsonObj);
			window[callback + "done"] = true;
		}

		// JSON download has 5-second timeout.
		setTimeout(function() {
			if (!window[callback + "done"]) {
				handler.@de.uba.mis.client.modules.UserModule::handleJsonResponse(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
			}

			// Cleanup. Remove script and callback elements.
			document.body.removeChild(script);
 			delete window[callback];
			delete window[callback + "done"];
		}, 5000);

		// Attach the script element to the document body.
		document.body.appendChild(script);
	}-*/;

	/**
	 * Handle the response to the request for users from a remote server.
	 */
	public void handleJsonResponse(JavaScriptObject jso) {
		// Request failed?
		if (jso == null) status.setText("Server does not respond!");
		// No, lets create the table
		else {
			readUsers(jso);
		
			table.update(users);
			
			status.setText("Synced, " + table.getOnlineUsers() + " of " + users.size() + " users online.");
		}
		
		status.setStyleName(Styles.STATUS_LABEL);
	}
	
	private void readUsers(JavaScriptObject javaScriptObject) {
		users.clear();
		
		JsArray<UserJSO> usersJSO = asArrayOfUsers(javaScriptObject);
		for (int userIndex = 0; userIndex < usersJSO.length(); userIndex++) 
			users.add(new User(usersJSO.get(userIndex)));
	}

	/**
	 * Convert the string of JSON into JavaScript objects of type User.
	 */
	private final native JsArray<UserJSO> asArrayOfUsers(JavaScriptObject users) /*-{return users;}-*/;
}
