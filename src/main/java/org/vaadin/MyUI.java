package org.vaadin;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.io.File;

@Theme("mytheme")
@Widgetset("org.vaadin.MyAppWidgetset")
public class MyUI extends UI {

    private CompanyContainer companyContainer;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        companyContainer = new CompanyContainer();
        setContent(new MainImpl());
    }

    public static MyUI getCurrent() {
        return (MyUI) UI.getCurrent();
    }

    public CompanyContainer getCompanyContainer() {
        return companyContainer;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
