package org.vaadin;

import java.io.File;

import com.vaadin.data.Container;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MainImpl extends MainViewDesign {

    private final ReportView reportView;

    public MainImpl() {
        super();
        reportView = new ReportView();

        CompanyContainer companyContainer = MyUI.getCurrent()
                .getCompanyContainer();
        fileTree.setContainerDataSource(companyContainer);
        fileTree.setItemCaptionPropertyId(CompanyContainer.PROPERTY_NAME);
        fileTree.addValueChangeListener((e) -> {
            File file = (File) fileTree.getValue();
            String companyName = (String) companyContainer.getItem(file)
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
