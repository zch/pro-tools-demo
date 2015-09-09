package org.vaadin;

import java.io.File;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.event.Action;

@SuppressWarnings("serial")
public class DemoViewImpl extends DemoView {

	private final Action LINECHART_ACTION = new Action("Plot a line chart");

	public DemoViewImpl() {
		super();
        fileTree.setContainerDataSource(new FilesystemContainer(new File("/Users/jonatan/Documents/xlsx"), true));
        fileTree.setItemCaptionPropertyId(FilesystemContainer.PROPERTY_NAME);
        fileTree.addValueChangeListener((e) -> open((File) fileTree.getValue()));
        
        spreadsheet.addActionHandler(new Action.Handler() {
            @Override
            public Action[] getActions(Object target, Object sender) {
                return new Action[] {LINECHART_ACTION};
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (action == LINECHART_ACTION) {
                    plotLineChart();
                }
            }
        });
	}
	
    private void open(File file) {
        try {
            spreadsheet.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void plotLineChart() {
    		chart.setConfiguration(new Configuration());
        chart.getConfiguration().setTitle("");
        spreadsheet.getCellSelectionManager().getCellRangeAddresses().forEach(this::addDataFromRangeAddress);
        chart.drawChart();
    }

    private void addDataFromRangeAddress(CellRangeAddress selection) {
        int numRows = selection.getLastRow() - selection.getFirstRow();
        int numCols = selection.getLastColumn() - selection.getFirstColumn();
        Configuration conf = chart.getConfiguration();
        if (numCols > numRows) {
            for (int r = selection.getFirstRow(); r <= selection.getLastRow(); r++) {
                ListSeries series = new ListSeries();
                series.setName("Row " + r);
                Row row = spreadsheet.getActiveSheet().getRow(r);
                for (int c = selection.getFirstColumn(); c <= selection.getLastColumn(); c++) {
                    Cell cell = row.getCell(c);
                    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        series.addData(cell.getNumericCellValue());
                    }
                }
                conf.addSeries(series);
            }
        } else {
            for (int c = selection.getFirstColumn(); c <= selection.getLastColumn(); c++) {
                ListSeries series = new ListSeries();
                series.setName("Col " + CellReference.convertNumToColString(c));
                for (int r = selection.getFirstRow(); r <= selection.getLastRow(); r++) {
                    Row row = spreadsheet.getActiveSheet().getRow(r);
                    Cell cell = row.getCell(c);
                    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        series.addData(cell.getNumericCellValue());
                    }
                }
                conf.addSeries(series);
            }
        }
    }
}
