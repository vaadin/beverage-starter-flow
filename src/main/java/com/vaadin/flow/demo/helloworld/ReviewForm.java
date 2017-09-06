package com.vaadin.flow.demo.helloworld;

import java.time.LocalDate;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
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
    private CategoryService categoryService = CategoryService.getInstance();
    private TextField beverageName = new TextField();
    private TextField timesTasted = new TextField();
    private ComboBox<String> categoryBox = new ComboBox<>();
    private DatePicker lastTasted = new DatePicker();
    private ComboBox<String> scoreBox = new ComboBox<>();
    private ReviewsView reviewsView;
    private PaperToast notification = new PaperToast();

    public ReviewForm(ReviewsView reviewsView) {
        this.reviewsView = reviewsView;
        bindFields();
        VerticalLayout reviewFormLayout = new VerticalLayout();
        addFormTitle(reviewFormLayout);
        addComboDatePicker(reviewFormLayout);
        scoreBox.setWidth("20%");
        scoreBox.setLabel("mark a score");
        scoreBox.setItems("0", "1", "2", "3", "4", "5");
        reviewFormLayout.add(scoreBox);
        addButtonRow(reviewFormLayout);
        setModal(true);
        add(reviewFormLayout);
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
        Button save = new Button("Save");
        Button cancel = new Button("Cancel");
        Button delete = new Button("Delete");
        save.addClickListener(e -> saveClicked());
        cancel.addClickListener(e -> cancelClicked());
        delete.addClickListener(null);
        delete.setDisabled(true);
        row4.add(save, cancel, delete, notification);
        reviewFormLayout.add(row4);
    }

    private void bindFields() {
        binder.forField(beverageName).withValidator(name -> name.length() >= 3,
                "The name of the beverage should be at least three characters.")
                .bind(Review::getName, Review::setName);
        binder.forField(timesTasted)
                .withConverter(
                        new StringToIntegerConverter(0, "Must enter a number"))
                .bind(Review::getTestTimes, Review::setTestTimes);
        binder.forField(categoryBox).bind(Review::getReviewCategory,
                Review::setReviewCategory);
        binder.forField(lastTasted).bind(Review::getTestDate,
                Review::setTestDate);
        binder.forField(scoreBox)
                .withConverter(new StringToIntegerConverter(0,
                        "The Score is a number"))
                .bind(Review::getScore, Review::setScore);
    }

    public void clear() {
        Review review = new Review();
        review.setTestDate(LocalDate.now());
        review.setScore(0);
        review.setTestTimes(0);
        binder.setBean(review);
    }

    private void cancelClicked() {
        close();
    }

    private void saveClicked() {
        if (binder.isValid()) {
            reviewService.saveReview(binder.getBean());
            reviewsView.updateList();
            close();
            reviewsView.showMessage();
        } else {
            notification.show("Please double check the information.");
        }
    }

}
