package org.vaadin;

import java.io.File;

import com.vaadin.data.Container;

import com.vaadin.data.util.FilesystemContainer;

@SuppressWarnings("serial")
public class MainImpl extends MainViewDesign {

    private final ReportView reportView;
    private final Container companyContainer;

    public MainImpl() {
		super();
        reportView = new ReportView();
        content.addComponent(reportView);

        companyContainer = new CompanyContainer();
        fileTree.setContainerDataSource(companyContainer);
        fileTree.setItemCaptionPropertyId(CompanyContainer.PROPERTY_NAME);
        fileTree.addValueChangeListener((e) -> reportView.open((File) fileTree.getValue()));
	}
}
