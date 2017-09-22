package com.vaadin.starter.beveragebuddy.ui;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.ui.Composite;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.combobox.ComboBox;
import com.vaadin.ui.datepicker.DatePicker;
import com.vaadin.ui.formlayout.FormLayout;
import com.vaadin.ui.html.Div;
import com.vaadin.ui.html.Label;
import com.vaadin.ui.layout.HorizontalLayout;
import com.vaadin.ui.layout.VerticalLayout;
import com.vaadin.ui.paper.dialog.GeneratedPaperDialog;
import com.vaadin.ui.textfield.TextField;

public class ReviewForm extends Composite<GeneratedPaperDialog> {

    private Binder<Review> binder = new Binder<>(Review.class);
    private Review reviewBean = new Review();
    private transient Consumer<Review> saveHandler;
    private transient Consumer<Review> deleteHandler;
    private transient CategoryService categoryService = CategoryService
            .getInstance();

    private FormLayout reviewFormLayout = new FormLayout();
    private HorizontalLayout buttonRow = new HorizontalLayout();
    private HorizontalLayout confirmDialogButtonBar = new HorizontalLayout();
    private VerticalLayout confirmDialogLayout = new VerticalLayout();

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");
    private Button yes = new Button("Yes");
    private Button no = new Button("No");
    private ComboBox<Category> categoryBox = new ComboBox<>();
    private ComboBox<String> scoreBox = new ComboBox<>();
    private DatePicker lastTasted = new DatePicker();
    private GeneratedPaperDialog confirmDialog = new GeneratedPaperDialog();
    private Label title = new Label();
    private PaperToast notification = new PaperToast();
    private TextField beverageName = new TextField();
    private TextField timesTasted = new TextField();

    public ReviewForm(Consumer<Review> saveHandler,
            Consumer<Review> deleteHandler) {
        this.saveHandler = saveHandler;
        this.deleteHandler = deleteHandler;

        createFormTitle();
        createNameField();
        createTimesField();
        createCategoryBox();
        createDatePicker();
        createScoreBox();
        createFormLayout();
        createButtonRow();
        createDeleteConfirmDialog();
        createNotification();
    }

    public void openReview(Optional<Review> optionalReview) {
        this.getContent().open();

        optionalReview.ifPresent(review -> {
            title.setText("Edit a review");
            this.reviewBean = review;
        });

        if (!optionalReview.isPresent()) {
            title.setText("Add a new review");
            reviewBean = new Review();
        }

        binder.readBean(reviewBean);
    }

    private void createNotification() {
        notification.addClassName("notification");
        this.getContent().add(notification);
    }

    private void createDeleteConfirmDialog() {
        confirmDialogButtonBar.add(yes, no);
        Label confirmation = new Label("Are you sure deleting this Review?");
        confirmDialogLayout.add(confirmation, confirmDialogButtonBar);
        confirmDialog.add(confirmDialogLayout);
        confirmDialog.setModal(true);
        this.getContent().add(confirmDialog);

        yes.addClickListener(event -> deleteConfirm());
        no.addClickListener(event -> {
            setButtonsDisabled(false);
            confirmDialog.close();
        });
    }

    private void createFormLayout() {
        reviewFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("50em", 2));
        reviewFormLayout.getStyle().set("padding", "0");
        Div div = new Div(reviewFormLayout);
        div.getStyle().set("padding", "10px");
        this.getContent().add(div);
        this.getContent().setModal(true);
    }

    private void createButtonRow() {
        buttonRow.add(save, cancel, delete);
        this.getContent().add(buttonRow);
        save.addClickListener(e -> this.saveClicked());
        cancel.addClickListener(e -> this.cancelClicked());
        delete.addClickListener(e -> this.deleteClicked());
    }

    private void createScoreBox() {
        scoreBox.setLabel("Mark a score");
        scoreBox.setWidth("15em");
        scoreBox.setAllowCustomValue(false);
        scoreBox.setItems("1", "2", "3", "4", "5");
        reviewFormLayout.add(scoreBox);

        binder.forField(scoreBox)
                .withConverter(new StringToIntegerConverter(0,
                        "The score should be a number"))
                .withValidator(score -> score >= 1 && score <= 5,
                        "The score should be between 1 and 5.")
                .bind(Review::getScore, Review::setScore);
    }

    private void createDatePicker() {
        lastTasted.setLabel("Choose the date");
        lastTasted.setMax(LocalDate.now());
        reviewFormLayout.add(lastTasted);

        binder.forField(lastTasted).bind(Review::getDate,
                Review::setDate);
    }

    private void createCategoryBox() {
        categoryBox.setLabel("Choose a category");
        categoryBox.setItemLabelPath("name");
        categoryBox.setItemValuePath("name");
        categoryBox.setAllowCustomValue(false);
        // TODO disable/hide the Clear button on the combobox
        categoryBox.setWidth("15em");
        categoryBox.setItems(categoryService.findCategories(""));
        reviewFormLayout.add(categoryBox);

        binder.forField(categoryBox)
                .withConverter(categoryService::findCategoryOrThrow,
                        Category::getName)
                .bind(Review::getCategory, Review::setCategory);
    }

    private void createTimesField() {
        timesTasted.setLabel("Times tasted");
        timesTasted.setPattern("[0-9]*");
        timesTasted.setPreventInvalidInput(true);
        reviewFormLayout.add(timesTasted);

        binder.forField(timesTasted)
                .withConverter(
                        new StringToIntegerConverter(0, "Must enter a number"))
                .withValidator(testTimes -> testTimes > 0,
                        "The taste times should be at least 1")
                .bind(Review::getCount, Review::setCount);
    }

    public void clear() {
        reviewBean.reset();
        binder.readBean(reviewBean);
    }

    private void createNameField() {
        beverageName.setLabel("Beverage name");
        reviewFormLayout.add(beverageName);

        binder.forField(beverageName).withValidator(name -> name.length() >= 3,
                "The name of the beverage should contain at least three characters.")
                .bind(Review::getName, Review::setName);
    }

    private void createFormTitle() {
        this.getContent().add(title);
    }

    private void deleteConfirm() {
        try {
            binder.writeBean(reviewBean);
            confirmDialog.close();
            this.getContent().close();
            setButtonsDisabled(false);
            this.deleteHandler.accept(reviewBean);
        } catch (ValidationException e) {
            notification.show(e.getMessage());
        }
    }

    private void cancelClicked() {
        this.getContent().close();
    }

    private void deleteClicked() {
        setButtonsDisabled(true);
        confirmDialog.open();
    }

    private void setButtonsDisabled(boolean disable) {
        buttonRow.getChildren()
                .forEach(child -> ((Button) child).setDisabled(disable));
    }

    private void saveClicked() {
        try {
            binder.writeBean(reviewBean);
            this.getContent().close();
            this.saveHandler.accept(reviewBean);
        } catch (ValidationException e) {
            BinderValidationStatus<Review> status = binder.validate();
            notification.show(status.getValidationErrors().stream()
                    .map(ValidationResult::getErrorMessage)
                    .collect(Collectors.joining("; ")));
        }
    }

}
