package org.vaadin;

import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.ui.Tree;

import java.io.File;

public class FileTree extends Tree {

    private final FilesystemContainer dataSource;

    public FileTree() {
        super();
        dataSource = new FilesystemContainer(new File("/Users/jonatan/Documents/xlsx"), true);
        setContainerDataSource(dataSource);
        setItemCaptionPropertyId(FilesystemContainer.PROPERTY_NAME);
    }
}
