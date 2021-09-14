package com.schess.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Push
@Route(value = "", layout = MainLayout.class)
public class MainView extends VerticalLayout {
    public MainView(@Autowired MessageBean bean) {
        final Button button = new Button("Click me", e -> {
            Notification.show(bean.getMessage());
            final UI ui = UI.getCurrent(); //
            ExecutorService executor = Executors.newSingleThreadExecutor(); //
            executor.submit(() -> {
                doHeavyStuff(); //
                ui.access(() -> { //
                    Notification.show("Calculation done"); //
                });
            });
        });
        add(button);
        // simple link to the logout endpoint provided by Spring Security
        Element logoutLink = ElementFactory.createAnchor("logout", "Logout");
        getElement().appendChild(logoutLink);
    }
}
