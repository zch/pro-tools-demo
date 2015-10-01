package org.vaadin;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window;

public class CompanyInfo extends CompanyInfoDesign {

	public CompanyInfo(String name) {
		this.name.setValue(name);
		
		saveButton.addClickListener(e -> {
			Notification.show("Saved", Notification.Type.TRAY_NOTIFICATION);
			close();
		});

		cancelButton.addClickListener(e -> {
			close();
		});

		deleteButton.addClickListener(e -> {
			Notification.show("Delete not permitted", "Please contact the administrator", Type.ERROR_MESSAGE);
		});

	}

	private void close() {
		if (getParent() instanceof Window) {
			((Window) getParent()).close();
		}
	}

}
