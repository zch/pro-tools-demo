package org.vaadin;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
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
public class MainViewDesign extends HorizontalLayout {
	protected HorizontalLayout menuTitle;
	protected Label menuTitleLabel;
	protected Button menuToggle;
	protected CssLayout menuItems;
	protected Button userButton;
	protected Tree fileTree;
	protected Button upload;
	protected VerticalLayout content;

	public MainViewDesign() {
		Design.read(this);
	}
}
