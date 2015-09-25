package org.vaadin;

import com.vaadin.data.util.HierarchicalContainer;

import java.io.File;

public class CompanyContainer extends HierarchicalContainer {

    public static final String PROPERTY_NAME = "name";

    public CompanyContainer() {
        addContainerProperty(PROPERTY_NAME, String.class, null);

        addCompany("Aviv", "Aviv.xlsx");
        addCompany("Microsoft", "Microsoft.xls");
        addCompany("P & G", "PG.xls");
        addCompany("Preston Smalley", "Preston Smalley.xls");
    }

    public void addCompany(String companyName, String fileName) {
        File file = getFile(fileName);
        addItem(file).getItemProperty(PROPERTY_NAME).setValue(companyName);
        setChildrenAllowed(file, false);
    }

    private File getFile(String fileName) {
        return new File(getClass().getResource("/xls/" + fileName).getPath());
    }
}
