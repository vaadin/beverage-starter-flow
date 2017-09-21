package com.vaadin.starter.beveragebuddy.ui;

import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.DomEvent;

@DomEvent("iron-announce")
public class IronAnnounceEvent extends ComponentEvent<PaperToast> {
    public IronAnnounceEvent(PaperToast source, boolean fromClient) {
        super(source, fromClient);
    }
}
