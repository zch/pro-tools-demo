package org.vaadin;

import java.io.File;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.event.Action;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ReportView extends VerticalLayout {
    private final Action PLOT_CHART_ACTION = new Action("Plot a chart");

    private Label header;
    private Button infoLink;

    private Spreadsheet spreadsheet;
    private Chart chart;

    public ReportView() {
        // Make the report view take the full screen
        setSizeFull();

        // Create and add the header with company name and info link
        addComponent(buildHeader());

        // Create and add the Spreadsheet
        spreadsheet = new Spreadsheet();
        addComponent(spreadsheet);
        // Make the spreadsheet take all available space
        setExpandRatio(spreadsheet, 1);

        // Create and add the Chart
        chart = new Chart();
        chart.getConfiguration().setTitle("");
        chart.setSizeFull();

        // Add an Action Handler to the spreadsheet. This adds the "Plot a chart" option to the context menu.
        spreadsheet.addActionHandler(new Action.Handler() {
            @Override
            public Action[] getActions(Object target, Object sender) {
                return new Action[]{PLOT_CHART_ACTION};
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (action == PLOT_CHART_ACTION) {
                    plotChart();
                    addComponent(chart);
                    setExpandRatio(chart, 1);
                }
            }
        });
    }

    /**
     * Builds a layout containing the company name in a large font
     * and a link to a form allowing editing of the company info.
     *
     * @return a layout containing the elements of the header
     */
    private HorizontalLayout buildHeader() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setMargin(true);

        // Create and add the company name label
        header = new Label();
        header.addStyleName(ValoTheme.LABEL_HUGE);
        header.setWidth(null);
        headerLayout.addComponent(header);

        // Create and add the company info link
        infoLink = new Button("Company info");
        infoLink.addStyleName(ValoTheme.BUTTON_LINK);
        infoLink.addClickListener(e -> openInfo());
        headerLayout.addComponent(infoLink);

        return headerLayout;
    }

    /**
     * Navigates to the company info form
     */
    public void openInfo() {
        ((ComponentContainer) getParent()).replaceComponent(this, new CompanyInfo(header.getValue()));
    }

    /**
     * Open an XLSX file to show in the spreadsheet for a specified company.
     *
     * @param name The company name
     * @param file The XLSX file containing the financial information for said company
     */
    public void open(String name, File file) {
        try {
            header.setValue(name);
            spreadsheet.read(file);
            removeComponent(chart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configures the chart component to show a column
     * chart of the selected data in the spreadsheet.
     */
    private void plotChart() {
        // Set the type of chart to a column chart
        Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.COLUMN);
        chart.setConfiguration(configuration);

        // Extract the values of all the selected cells and plot them in a chart
        spreadsheet.getCellSelectionManager().getCellRangeAddresses().forEach(this::addDataFromRangeAddress);

        // Force the chart to redraw
        chart.drawChart();
    }

    /**
     * Extracts values from cells in a CellRangeAddress and creates data series
     * for these values and finally adds the data series to the chart.
     *
     * @param selection the CellRangeAddress to extract values from
     */
    private void addDataFromRangeAddress(CellRangeAddress selection) {
        int numRows = selection.getLastRow() - selection.getFirstRow();
        int numCols = selection.getLastColumn() - selection.getFirstColumn();
        Configuration conf = chart.getConfiguration();

        if (numCols > numRows) {
            // We are comparing rows
            chart.getConfiguration().setTitle("Compare rows");
            // Loop through each row and add the data from each cell to a new data series object.
            for (int r = selection.getFirstRow(); r <= selection.getLastRow(); r++) {
                DataSeries series = new DataSeries();
                series.setName("Row " + r);
                Row row = spreadsheet.getActiveSheet().getRow(r);
                if (row != null) {
                    for (int c = selection.getFirstColumn(); c <= selection.getLastColumn(); c++) {
                        Cell cell = row.getCell(c);
                        // If the cell is a numeric value, add the numeric value, otherwise add null instead
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            series.add(new DataSeriesItem(CellReference.convertNumToColString(c),
                                    cell.getNumericCellValue()));
                        } else {
                            series.add(new DataSeriesItem(CellReference.convertNumToColString(c), null));
                        }
                    }
                }
                // Add the data series to the chart
                conf.addSeries(series);
            }
        } else {
            // We are comparing columns
            chart.getConfiguration().setTitle("Compare columns");
            // Loop through each column and add the data from each cell to a new data series object.
            for (int c = selection.getFirstColumn(); c <= selection.getLastColumn(); c++) {
                DataSeries series = new DataSeries();
                series.setName("Col " + CellReference.convertNumToColString(c));
                for (int r = selection.getFirstRow(); r <= selection.getLastRow(); r++) {
                    Row row = spreadsheet.getActiveSheet().getRow(r);
                    if (row != null) {
                        Cell cell = row.getCell(c);
                        // If the cell is a numeric value, add the numeric value, otherwise add null instead
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            series.add(new DataSeriesItem(row.getRowNum() + 1, cell.getNumericCellValue()));
                        } else {
                            series.add(new DataSeriesItem(row.getRowNum() + 1, null));
                        }
                    }
                }
                // Add the data series to the chart
                conf.addSeries(series);
            }
        }
    }

}
