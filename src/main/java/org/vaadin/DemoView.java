package org.vaadin;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { … }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class DemoView extends HorizontalSplitPanel {
	protected Tree fileTree;
	protected Spreadsheet spreadsheet;
	protected Chart chart;

	public DemoView() {
		Design.read(this);
	}
}