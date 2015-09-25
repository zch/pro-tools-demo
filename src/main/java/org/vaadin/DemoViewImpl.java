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
public class DemoViewImpl extends DemoView {

	private final Action PLOT_CHART_ACTION = new Action("Plot a chart");

	public DemoViewImpl() {
		super();
        fileTree.setContainerDataSource(new FilesystemContainer(new File(getClass().getResource("/xls").getPath()), true));
        fileTree.setItemCaptionPropertyId(FilesystemContainer.PROPERTY_NAME);
        fileTree.addValueChangeListener((e) -> open((File) fileTree.getValue()));
        
        spreadsheet.addActionHandler(new Action.Handler() {
            @Override
            public Action[] getActions(Object target, Object sender) {
                return new Action[] {PLOT_CHART_ACTION};
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (action == PLOT_CHART_ACTION) {
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
        chart.getConfiguration().getChart().setType(ChartType.COLUMN);
        spreadsheet.getCellSelectionManager().getCellRangeAddresses().forEach(this::addDataFromRangeAddress);
        chart.drawChart();
    }

    private void addDataFromRangeAddress(CellRangeAddress selection) {
        int numRows = selection.getLastRow() - selection.getFirstRow();
        int numCols = selection.getLastColumn() - selection.getFirstColumn();
        Configuration conf = chart.getConfiguration();
        if (numCols > numRows) {
            chart.getConfiguration().setTitle("Compare rows");
            for (int r = selection.getFirstRow(); r <= selection.getLastRow(); r++) {
                DataSeries series = new DataSeries();
                series.setName("Row " + r);
                Row row = spreadsheet.getActiveSheet().getRow(r);
                if (row != null) {
	                for (int c = selection.getFirstColumn(); c <= selection.getLastColumn(); c++) {
	                    Cell cell = row.getCell(c);
	                    if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
	                    		series.add(new DataSeriesItem(CellReference.convertNumToColString(c), cell.getNumericCellValue()));
	                    } else {
	                    		series.add(new DataSeriesItem(CellReference.convertNumToColString(c), null));
	                    }
	                }
                }
                conf.addSeries(series);
            }
        } else {
            chart.getConfiguration().setTitle("Compare columns");
            for (int c = selection.getFirstColumn(); c <= selection.getLastColumn(); c++) {
                DataSeries series = new DataSeries();
                series.setName("Col " + CellReference.convertNumToColString(c));
                for (int r = selection.getFirstRow(); r <= selection.getLastRow(); r++) {
                    Row row = spreadsheet.getActiveSheet().getRow(r);
                    if (row != null) {
	                    Cell cell = row.getCell(c);
	                    if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
	                        series.add(new DataSeriesItem(row.getRowNum()+1, cell.getNumericCellValue()));
	                    } else {
	                    		series.add(new DataSeriesItem(row.getRowNum()+1, null));
	                    }
                    }
                }
                conf.addSeries(series);
            }
        }
    }
}
