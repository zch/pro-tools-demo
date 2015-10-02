package org.vaadin;

import java.io.File;

import com.vaadin.ui.Tree;

@SuppressWarnings("serial")
public class MainImpl extends MainViewDesign {

    private final ReportView reportView;

    public MainImpl() {
        super();
        reportView = new ReportView();

        fileTree.setContainerDataSource(new CompanyContainer());
        fileTree.setItemCaptionPropertyId(CompanyContainer.PROPERTY_NAME);
        fileTree.addValueChangeListener((e) -> {
            File file = (File) fileTree.getValue();
            if (file == null) {
            	return;
            }
            String companyName = (String) fileTree.getItem(file)
                    .getItemProperty(CompanyContainer.PROPERTY_NAME).getValue();
            reportView.open(companyName, file);
            content.removeAllComponents();
            content.addComponent(reportView);
        });

        upload.addClickListener((e) -> {
            content.removeAllComponents();
            content.addComponent(new UploadForm());
        });
    }
}
