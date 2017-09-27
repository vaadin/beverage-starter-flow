package com.vaadin.starter.beveragebuddy.ui;

import java.io.Serializable;
import java.util.function.Consumer;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Composite;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.html.H2;
import com.vaadin.ui.html.Label;
import com.vaadin.ui.layout.HorizontalLayout;
import com.vaadin.ui.layout.VerticalLayout;
import com.vaadin.ui.paper.dialog.GeneratedPaperDialog;

/**
 * A generic dialog for confirming or cancelling an action.
 * @param <T>   The type of the action's subject
 */
@HtmlImport("frontend://bower_components/paper-dialog/paper-dialog.html")
class ConfirmationDialog<T extends Serializable>
        extends Composite<GeneratedPaperDialog> {

    private final H2 titleField = new H2();
    private final Label messageLabel = new Label();
    private final Label extraMessageLabel = new Label();
    private final Button confirmButton = new Button();
    private final Button cancelButton = new Button("Cancel");
    private Registration registrationForConfirm;
    private Registration registrationForCancel;

    /**
     * Constructor.
     */
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

    /**
     * Opens the confirmation dialog.
     *
     * The dialog will display the given title and message(s), then call
     * <code>confirmHandler()</code> if the Confirm button is clicked,
     * or <code>cancelHandler()</code> if the Cancel button is clicked.
     *
     * @param title             The title text
     * @param message           Detail message (optional, may be empty)
     * @param additionalMessage Additional message (optional, may be empty)
     * @param actionName        The action name to be shown on the
     *                          Confirm button
     * @param item              The subject of the action
     * @param confirmHandler    The confirmation handler function
     * @param cancelHandler     The cancellation handler function
     */
    public void open(String title, String message, String additionalMessage,
            String actionName, T item, Consumer<T> confirmHandler,
            Runnable cancelHandler) {
        titleField.setText(title);
        messageLabel.setText(message);
        extraMessageLabel.setText(additionalMessage);
        confirmButton.setText(actionName);

        if (registrationForConfirm != null) {
            registrationForConfirm.remove();
        }
        registrationForConfirm = confirmButton.addClickListener(
                e -> confirmHandler.accept(item));
        if (registrationForCancel != null) {
            registrationForCancel.remove();
        }
        registrationForCancel = cancelButton.addClickListener(
                e -> cancelHandler.run());

        getContent().open();
    }
}
