package org.vaadin;

import java.io.File;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.event.Action;

@SuppressWarnings("serial")
public class MainImpl extends MainViewDesign {

    private final ReportView reportView;

    public MainImpl() {
		super();
        reportView = new ReportView();
        content.addComponent(reportView);
        fileTree.setContainerDataSource(new FilesystemContainer(new File(getClass().getResource("/xls").getPath()), true));
        fileTree.setItemCaptionPropertyId(FilesystemContainer.PROPERTY_NAME);
        fileTree.addValueChangeListener((e) -> reportView.open((File) fileTree.getValue()));
	}
}
