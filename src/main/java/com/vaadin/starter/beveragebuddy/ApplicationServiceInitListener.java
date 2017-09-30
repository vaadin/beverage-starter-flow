package com.vaadin.starter.beveragebuddy;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinServiceInitListener;

public class ApplicationServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addBootstrapListener(response -> {
            Document document = response.getDocument();

            Element head = document.head();

            head.appendChild(createMeta(document, "viewport",
                    "width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes"));
            head.appendChild(createMeta(document,
                    "apple-mobile-web-app-capable", "yes"));
            head.appendChild(createMeta(document,
                    "apple-mobile-web-app-status-bar-style", "black"));
        });
    }

    private Element createMeta(Document document, String name, String content) {
        Element meta = document.createElement("meta");
        meta.attr("name", name);
        meta.attr("content", content);
        return meta;
    }

}