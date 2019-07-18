package com.vaadin.starter.connect;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;

@Route("main")
public class MainView extends Span {
    Button b;

    public MainView() {
        setText("FLOW APP");
        getStyle().set("background", "pink");
        getStyle().set("padding", "10px");
        getStyle().set("position", "absolute");
        addClickListener(e -> {
            System.err.println("Hi");
            getUI().get().getPage().executeJs("alert('Hi from Flow')");
        });
        
        add(new Button("HEY"));
    }

}