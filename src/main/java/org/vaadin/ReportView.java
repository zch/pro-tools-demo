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
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ReportView extends VerticalLayout {
    private final Action PLOT_CHART_ACTION = new Action("Plot a chart");

    private Label header;
    private Button infoLink;

    private Spreadsheet spreadsheet;
    private Chart chart;

    public ReportView() {
        setSizeFull();
        CssLayout headerLayout = new CssLayout();
        headerLayout.setWidth("100%");
        header = new Label();
        header.addStyleName(ValoTheme.LABEL_HUGE);
        header.setWidth(null);
        infoLink = new Button("Company info");
        infoLink.addStyleName(ValoTheme.BUTTON_LINK);
        infoLink.addClickListener(e->openInfo());
        headerLayout.addComponent(header);
        headerLayout.addComponent(infoLink);

        spreadsheet = new Spreadsheet();
        chart = new Chart();
        chart.getConfiguration().setTitle("");
        chart.setSizeFull();
        addComponent(headerLayout);
        addComponent(spreadsheet);
        addComponent(chart);
        setExpandRatio(spreadsheet, 1);
        setExpandRatio(chart, 1);

        spreadsheet.addActionHandler(new Action.Handler() {
            @Override
            public Action[] getActions(Object target, Object sender) {
                return new Action[] { PLOT_CHART_ACTION };
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (action == PLOT_CHART_ACTION) {
                    plotLineChart();
                }
            }
        });
    }

    public void openInfo() {
        Window w = new Window();
        w.setDraggable(false);
        w.setResizable(false);
        w.setSizeFull();
        w.setModal(true);
        w.setContent(new CompanyInfo(header.getValue()));
        UI.getCurrent().addWindow(w);
    }
    
    public void open(String name, File file) {
        try {
            header.setValue(name);
            spreadsheet.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void plotLineChart() {
        Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.COLUMN);
        chart.setConfiguration(configuration);
        spreadsheet.getCellSelectionManager().getCellRangeAddresses()
                .forEach(this::addDataFromRangeAddress);
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
                    for (int c = selection.getFirstColumn(); c <= selection
                            .getLastColumn(); c++) {
                        Cell cell = row.getCell(c);
                        if (cell != null
                                && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            series.add(new DataSeriesItem(CellReference
                                    .convertNumToColString(c), cell
                                    .getNumericCellValue()));
                        } else {
                            series.add(new DataSeriesItem(CellReference
                                    .convertNumToColString(c), null));
                        }
                    }
                }
                conf.addSeries(series);
            }
        } else {
            chart.getConfiguration().setTitle("Compare columns");
            for (int c = selection.getFirstColumn(); c <= selection
                    .getLastColumn(); c++) {
                DataSeries series = new DataSeries();
                series.setName("Col " + CellReference.convertNumToColString(c));
                for (int r = selection.getFirstRow(); r <= selection
                        .getLastRow(); r++) {
                    Row row = spreadsheet.getActiveSheet().getRow(r);
                    if (row != null) {
                        Cell cell = row.getCell(c);
                        if (cell != null
                                && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            series.add(new DataSeriesItem(row.getRowNum() + 1,
                                    cell.getNumericCellValue()));
                        } else {
                            series.add(new DataSeriesItem(row.getRowNum() + 1,
                                    null));
                        }
                    }
                }
                conf.addSeries(series);
            }
        }
    }

}
