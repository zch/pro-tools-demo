package org.vaadin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.vaadin.data.Item;
import com.vaadin.ui.Notification;

public class UploadForm extends UploadFormDesign {

    File uploadedFile;

    public UploadForm() {
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
            Notification.show("File successfully uploaded");
            save.setEnabled(true);
        });

        save.addClickListener((e) -> {
            CompanyContainer c = MyUI.getCurrent().getCompanyContainer();
            Item i = c.addItem(uploadedFile);
            i.getItemProperty(CompanyContainer.PROPERTY_NAME).setValue(
                    companyName.getValue());
            c.setChildrenAllowed(uploadedFile, false);
        });

        cancel.addClickListener((e) -> {
            uploadedFile.delete();
            companyName.clear();
            description.clear();
        });
    }

}
