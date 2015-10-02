package org.vaadin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.vaadin.data.Item;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

public class UploadForm extends VerticalLayout {

    File uploadedFile;
    
    private Upload upload = new Upload();;
    private TextField companyName = new TextField("Company name");
    private TextField additionalInformation = new TextField("Additional information");;
    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    public UploadForm() {
    	setSpacing(true);
    	setMargin(true);
    	
    	// add Upload component
    	addComponent(upload);	
    	
    	// add TextFields next to each other
    	HorizontalLayout hz = new HorizontalLayout();
    	hz.setSpacing(true);
    	addComponent(hz);
    	hz.addComponent(companyName);
    	hz.addComponent(additionalInformation);
    	
    	// add Buttons next to each other
    	hz = new HorizontalLayout();
    	hz.setSpacing(true);
    	addComponent(hz);
    	setComponentAlignment(hz, Alignment.BOTTOM_CENTER);
    	hz.addComponent(cancel);
    	save.setEnabled(false);
    	hz.addComponent(save);
    	
    	// attach logic to Upload and Buttons
    	
    	cancel.addClickListener((e) -> {
            if (uploadedFile != null) {
                uploadedFile.delete();
            }
            ((AbstractLayout)getParent()).replaceComponent(this, new UploadForm());
        });
    	
        save.addClickListener((e) -> {            
            Tree tree = MyUI.getCurrent().getCompanyTree();
            
            Item i = tree.addItem(uploadedFile);
            i.getItemProperty(CompanyContainer.PROPERTY_NAME).setValue(
                    companyName.getValue());
            tree.setChildrenAllowed(uploadedFile, false);
            tree.select(uploadedFile);
            
            Notification.show("Saved", Type.HUMANIZED_MESSAGE);
        });

    	
        upload.setReceiver((filename, mimeType) -> {
            // Create upload stream
            FileOutputStream fos = null; // Stream to write to
            try {
                // Open the file for writing.
                uploadedFile = File.createTempFile("temp", filename);
                fos = new FileOutputStream(uploadedFile);
            } catch (final IOException e) {
                Notification.show("Could not open file<br/>", e.getMessage(),
                        Notification.Type.ERROR_MESSAGE);
                return null;
            }
            return fos; // Return the output stream to write to
        });

        upload.addSucceededListener((e) -> {
            if (uploadedFile.length() > 0) {
                Notification.show("File successfully uploaded");
                save.setEnabled(true);
                replaceComponent(upload, new Label(uploadedFile.getName()));
            }
        });
        
    }

}
