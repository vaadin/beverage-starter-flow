package com.vaadin.starter.beveragebuddy.ui;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.ValidationResult;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Composite;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.formlayout.FormLayout;
import com.vaadin.ui.html.Div;
import com.vaadin.ui.html.H2;
import com.vaadin.ui.layout.HorizontalLayout;
import com.vaadin.ui.paper.dialog.GeneratedPaperDialog;


/**
 * Abstract base class for forms adding, editing or deleting items.
 * @param <T>   the type of the item to be added, edited or deleted
 */
@HtmlImport("frontend://bower_components/paper-dialog/paper-dialog.html")
public abstract class ItemEditorForm<T extends Serializable>
        extends Composite<GeneratedPaperDialog> {

    /**
     * The operations supported by this dialog.
     * Delete is enabled when editing an already existing item.
     */
    public enum Operation {
        ADD("Add New", "add", true),
        EDIT("Edit", "edit", false);

        private final String nameInTitle;
        private final String nameInText;
        private final boolean deleteDisabled;

        Operation(String nameInTitle, String nameInText, boolean deleteDisabled) {
            this.nameInTitle = nameInTitle;
            this.nameInText = nameInText;
            this.deleteDisabled = deleteDisabled;
        }

        public String getNameInTitle() {
            return nameInTitle;
        }

        public String getNameInText() {
            return nameInText;
        }

        public boolean isDeleteDisabled() {
            return deleteDisabled;
        }
    }

    private final H2 titleField = new H2();
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final Button deleteButton = new Button("Delete");
    private Registration registration;

    private final FormLayout formLayout = new FormLayout();
    private final HorizontalLayout buttonBar
            = new HorizontalLayout(saveButton, cancelButton, deleteButton);

    private Binder<T> binder = new Binder<>();
    private T currentItem;

    private final ConfirmationDialog<T> confirmationDialog
            = new ConfirmationDialog<>();
    private final PaperToast notification = new PaperToast();

    private final String itemType;
    private final BiConsumer<T, Operation> itemSaver;
    private final Consumer<T> itemDeleter;

    /**
     * Constructs a new instance.
     * @param itemType      The readable name of the item type
     * @param itemSaver     Callback to save the edited item
     * @param itemDeleter   Callback to delete the edited item
     */
    protected ItemEditorForm(String itemType, BiConsumer<T, Operation> itemSaver,
            Consumer<T> itemDeleter) {
        this.itemType = itemType;
        this.itemSaver = itemSaver;
        this.itemDeleter = itemDeleter;

        initTitle();
        initFormLayout();
        initButtonBar();
        initNotification();
        getContent().setModal(true);

    }

    private void initTitle() {
        getContent().add(titleField);
    }

    private void initFormLayout() {
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("50em", 2));
        formLayout.getStyle().set("padding", "0");
        Div div = new Div(formLayout);
        div.getStyle().set("padding", "10px");
        getContent().add(div);
    }

    private void initButtonBar() {
        saveButton.getElement().setAttribute("autofocus", true);
        cancelButton.getElement().setAttribute("dialog-dismiss", true);
        deleteButton.addClickListener(e -> deleteClicked());
        buttonBar.setClassName("buttons");
        getContent().add(buttonBar);
    }

    private void initNotification() {
        getContent().add(notification);
        notification.addClassName("notification");
    }

    /**
     * Gets the form layout, where additional components can be added
     * for displaying or editing the item's properties.
     * @return  the form layout
     */
    protected final FormLayout getFormLayout() {
        return formLayout;
    }

    /**
     * Gets the binder.
     * @return  the binder
     */
    protected final Binder<T> getBinder() {
        return binder;
    }

    /**
     * Gets the item currently being edited.
     * @return  the item currently being edited
     */
    protected final T getCurrentItem() {
        return currentItem;
    }

    /**
     * Opens the given item for editing in the dialog.
     * @param item      The item to edit; it may be an existing or
     *                  a newly created instance
     * @param operation The operation being performed on the item
     */
    public final void open(T item, Operation operation) {
        currentItem = item;
        titleField.setText(operation.getNameInTitle() + " " + itemType);
        if (registration != null) {
            registration.remove();
        }
        registration = saveButton.addClickListener(e -> saveClicked(operation));
        binder.readBean(currentItem);

        setButtonsDisabled(false);
        deleteButton.setDisabled(operation.isDeleteDisabled());
        getContent().open();
    }

    private void saveClicked(Operation operation) {
        boolean isValid = binder.writeBeanIfValid(currentItem);

        if (isValid) {
            itemSaver.accept(currentItem, operation);
            getContent().close();
        } else {
            BinderValidationStatus<T> status = binder.validate();
            notification.show(status.getValidationErrors().stream()
                    .map(ValidationResult::getErrorMessage)
                    .collect(Collectors.joining("; ")));
        }
    }

    private void deleteClicked() {
        if (confirmationDialog.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(confirmationDialog));
        }
        setButtonsDisabled(true);
        confirmDelete();
    }

    protected abstract void confirmDelete();

    protected final void openConfirmationDialog(String title, String message,
            String additionalMessage) {
        confirmationDialog.open(title, message, additionalMessage, "Delete",
                getCurrentItem(), this::deleteConfirmed,
                () -> setButtonsDisabled(false));
    }

    private void deleteConfirmed(T item) {
        itemDeleter.accept(item);
        setButtonsDisabled(false);
        getContent().close();
    }

    private void setButtonsDisabled(boolean disable) {
        buttonBar.getChildren().forEach(
                child -> ((Button) child).setDisabled(disable));
    }
}
