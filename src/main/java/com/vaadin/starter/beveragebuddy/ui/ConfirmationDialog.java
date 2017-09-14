package com.vaadin.starter.beveragebuddy.ui;

import java.util.function.Consumer;

import com.vaadin.flow.html.H2;
import com.vaadin.flow.html.Label;
import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.generated.paper.dialog.GeneratedPaperDialog;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

class ConfirmationDialog extends Composite<GeneratedPaperDialog> {
    private final H2 titleField = new H2();
    private final Label messageLabel = new Label();
    private final Label extraMessageLabel = new Label();
    private final Button confirmButton = new Button();
    private final Button cancelButton = new Button("Cancel");
    private Registration registration;

    public ConfirmationDialog() {
        confirmButton.getElement().setAttribute("dialog-confirm", true);
        confirmButton.setAutofocus(true);
        cancelButton.getElement().setAttribute("dialog-dismiss", true);

        HorizontalLayout buttonBar = new HorizontalLayout(confirmButton, cancelButton);
        buttonBar.setClassName("buttons");
        VerticalLayout layout = new VerticalLayout(titleField, messageLabel,
                extraMessageLabel, buttonBar);
        getContent().setModal(true);
        getContent().add(layout);
    }

    public void open(String title, String message, String additionalMessage,
            String actionName, Category item, Consumer<Category> handler) {
        titleField.setText(title);
        messageLabel.setText(message);
        extraMessageLabel.setText(additionalMessage);
        confirmButton.setText(actionName);
        if (registration != null) {
            registration.remove();
        }
        registration = confirmButton.addClickListener(e -> handler.accept(item));
        getContent().open();
    }
}
