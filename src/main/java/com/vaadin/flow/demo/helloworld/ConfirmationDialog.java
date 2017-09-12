package com.vaadin.flow.demo.helloworld;

import java.util.function.Consumer;

import com.vaadin.flow.html.Div;
import com.vaadin.flow.html.H2;
import com.vaadin.flow.html.Label;
import com.vaadin.flow.starter.app.backend.Category;
import com.vaadin.generated.paper.dialog.GeneratedPaperDialog;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

class ConfirmationDialog extends Composite<Div> {
    private final GeneratedPaperDialog dialog = new GeneratedPaperDialog();
    private final H2 titleField = new H2();
    private final Label message = new Label();
    private final Label message2 = new Label();
    private final Button confirmButton = new Button();
    private final Button cancelButton = new Button("Cancel");
    private Registration registration;

    public ConfirmationDialog() {
        confirmButton.getElement().setAttribute("dialog-confirm", true);
        confirmButton.setAutofocus(true);
        cancelButton.getElement().setAttribute("dialog-dismiss", true);

        HorizontalLayout buttonBar = new HorizontalLayout(confirmButton, cancelButton);
        buttonBar.setClassName("buttons");
        VerticalLayout layout = new VerticalLayout(titleField, message, message2, buttonBar);
        dialog.add(layout);
        dialog.setModal(true);
        getContent().add(dialog);
    }

    public void open(String title, String text1, String text2, String actionName,
            Category item, Consumer<Category> handler) {
        titleField.setText(title);
        message.setText(text1);
        message2.setText(text2);
        confirmButton.setText(actionName);
        if (registration != null) {
            registration.remove();
        }
        registration = confirmButton.addClickListener(e -> handler.accept(item));
        dialog.open();
    }
}
