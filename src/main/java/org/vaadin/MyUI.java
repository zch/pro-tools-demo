package org.vaadin;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;

@Theme("mytheme")
@Widgetset("org.vaadin.MyAppWidgetset")
@Viewport("width=device-width,initial-scale=1")
public class MyUI extends UI {

    private MainImpl mainContent;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainContent = new MainImpl();
        setContent(mainContent);
    }

    public static MyUI getCurrent() {
        return (MyUI) UI.getCurrent();
    }

    public Tree getCompanyTree() {
        return mainContent.fileTree;
    }
    
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
