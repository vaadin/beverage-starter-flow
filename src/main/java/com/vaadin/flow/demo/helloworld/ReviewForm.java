package com.vaadin.flow.demo.helloworld;

import java.time.LocalDate;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.flow.html.Label;
import com.vaadin.flow.starter.app.backend.Category;
import com.vaadin.flow.starter.app.backend.CategoryService;
import com.vaadin.flow.starter.app.backend.Review;
import com.vaadin.flow.starter.app.backend.ReviewService;
import com.vaadin.generated.paper.dialog.GeneratedPaperDialog;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DatePicker;
import com.vaadin.ui.FlexLayout.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ReviewForm extends GeneratedPaperDialog {

    private ReviewService reviewService = ReviewService.getInstance();
    private Binder<Review> binder = new Binder<>(Review.class);
    private Review reviewBean = new Review();
    private CategoryService categoryService = CategoryService.getInstance();
    private TextField beverageName = new TextField();
    private TextField timesTasted = new TextField();
    private ComboBox<String> categoryBox = new ComboBox<>();
    private DatePicker lastTasted = new DatePicker();
    private ComboBox<String> scoreBox = new ComboBox<>();
    private ReviewsView reviewsView;
    private PaperToast notification = new PaperToast();
    private GeneratedPaperDialog confirmDialog = new GeneratedPaperDialog();
    Button save = new Button("Save");
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete");

    public ReviewForm(ReviewsView reviewsView) {
        this.reviewsView = reviewsView;
        bindFields();
        VerticalLayout reviewFormLayout = new VerticalLayout();
        addFormTitle(reviewFormLayout);
        addComboDatePicker(reviewFormLayout);
        scoreBox.setWidth("20%");
        scoreBox.setLabel("mark a score");
        scoreBox.setItems("1", "2", "3", "4", "5");
        reviewFormLayout.add(scoreBox);
        addButtonRow(reviewFormLayout);
        setModal(true);
        add(reviewFormLayout);
        deleteConfirmDesign();
        add(confirmDialog);
    }

    private void addComboDatePicker(VerticalLayout reviewFormLayout) {
        HorizontalLayout row2 = new HorizontalLayout();
        categoryBox.setLabel("chose a category");
        categoryBox.setItems(categoryService.findCategory("").stream()
                .map(Category::getCategoryName).collect(Collectors.toList())
                .toArray(new String[0]));
        lastTasted.setLabel("choose the date");
        row2.add(categoryBox, lastTasted);
        row2.setSpacing(true);
        reviewFormLayout.add(row2);
    }

    private void addFormTitle(VerticalLayout reviewFormLayout) {
        Label label = new Label("Edit Beverage Notes");
        HorizontalLayout row1 = new HorizontalLayout();
        row1.setWidth("90%");
        row1.setSpacing(true);
        beverageName.setLabel("Beverage Name");
        beverageName.setPlaceholder("add a beverage name");
        timesTasted.setLabel("Times Tasted");
        timesTasted.setPlaceholder("add a number ");
        timesTasted.setPattern("[0-9]*").setPreventInvalidInput(true);
        row1.add(beverageName, timesTasted);
        reviewFormLayout.add(label, row1);
    }

    private void addButtonRow(VerticalLayout reviewFormLayout) {
        HorizontalLayout row4 = new HorizontalLayout();
        row4.setWidth("80%");
        row4.setSpacing(true);
        row4.setHeight("150px");
        row4.setDefaultComponentAlignment(Alignment.END);

        save.addClickListener(e -> this.saveClicked());
        cancel.addClickListener(e -> this.cancelClicked());
        delete.addClickListener(e -> this.deleteClicked());
        row4.add(save, cancel, delete, notification);
        reviewFormLayout.add(row4);
    }

    private void deleteConfirmDesign() {
        VerticalLayout confirmDeleteDesign = new VerticalLayout();
        HorizontalLayout buttonBar = new HorizontalLayout();
        Button yes = new Button("Yes");
        Button no = new Button("No");
        buttonBar.add(yes, no);
        Label confirmation = new Label("Are you sure deleteing this Review?");
        confirmDeleteDesign.add(confirmation, buttonBar);
        confirmDialog.add(confirmDeleteDesign);
        confirmDialog.setModal(true);

        yes.addClickListener(event -> deleteConfirm());
        no.addClickListener(event -> {
            buttonEnable();
            confirmDialog.close();
            this.close();
        });
    }

    private void deleteConfirm() {
        try {
            binder.writeBean(reviewBean);
            reviewService.deleteReview(reviewBean);
            reviewsView.updateList();
            confirmDialog.close();
            this.close();
            reviewsView.showMessage();
            buttonEnable();
        } catch (ValidationException e) {
            notification.show(
                    "Please double check the information." + e.getMessage());
        }
    }

    private void bindFields() {
        binder.forField(beverageName).withValidator(name -> name.length() >= 3,
                "The name of the beverage should be at least three characters.")
                .bind(Review::getName, Review::setName);
        binder.forField(timesTasted)
                .withConverter(
                        new StringToIntegerConverter(0, "Must enter a number"))
                .withValidator(testTimes -> testTimes > 0,
                        "The taste times should be at least 1")
                .bind(Review::getTestTimes, Review::setTestTimes);
        binder.forField(categoryBox).bind(Review::getReviewCategory,
                Review::setReviewCategory);
        binder.forField(lastTasted).bind(Review::getTestDate,
                Review::setTestDate);
        binder.forField(scoreBox)
                .withConverter(new StringToIntegerConverter(0,
                        "The Score is a number"))
                .withValidator(score -> score >= 1 && score <= 5,
                        "The score should be between 1 and 5.")
                .bind(Review::getScore, Review::setScore);
    }

    public void clear() {
        reviewBean.setTestDate(LocalDate.now());
        reviewBean.setScore(1);
        reviewBean.setTestTimes(0);
        binder.readBean(reviewBean);
    }

    public void bindReview(Review review) {
        this.reviewBean = review;
        binder.readBean(reviewBean);
    }

    private void cancelClicked() {
        this.close();
    }

    private void deleteClicked() {
        save.setDisabled(true);
        delete.setDisabled(true);
        cancel.setDisabled(true);
        confirmDialog.open();
        confirmDialog.setModal(true);
    }

    private void buttonEnable() {
        save.setDisabled(false);
        delete.setDisabled(false);
        cancel.setDisabled(false);
    }

    private void saveClicked() {
        try {
            binder.writeBean(reviewBean);
            reviewService.saveReview(reviewBean);
            reviewsView.updateList();
            this.close();
            reviewsView.showMessage();
        } catch (ValidationException e) {
            notification.show(
                    "Please double check the information." + e.getMessage());
        }
    }

}
