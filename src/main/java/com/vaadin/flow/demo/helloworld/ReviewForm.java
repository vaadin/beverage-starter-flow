package com.vaadin.flow.demo.helloworld;

import java.time.LocalDate;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.flow.html.Label;
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

    private TextField beverageName = new TextField();
    private TextField timesTasted = new TextField();
    private ComboBox<String> categoryBox = new ComboBox<>();
    private DatePicker lastTasted = new DatePicker();
    private ComboBox<String> scoreBox = new ComboBox<>();
    private ReviewsView reviewsView;

    public ReviewForm(ReviewsView reviewsView) {
        this.reviewsView = reviewsView;
        binder.forField(beverageName).withValidator(name -> name.length() >= 3,
                "The name of the beverage should be at least three characters.")
                .bind(Review::getName, Review::setName);
        binder.forField(timesTasted)
                .withConverter(
                        new StringToIntegerConverter("Must enter a number"))
                .bind(Review::getTestTimes, Review::setTestTimes);
        binder.forField(categoryBox).bind(Review::getReviewCategory,
                Review::setReviewCategory);
        binder.forField(lastTasted).bind(Review::getTestDate,
                Review::setTestDate);
        binder.forField(scoreBox)
                .withConverter(
                        new StringToIntegerConverter("The Score is a number"))
                .bind(Review::getScore, Review::setScore);

        VerticalLayout reviewFormLayout = new VerticalLayout();
        // Title of this dialog is "Edit Beverage Notes"
        Label label = new Label("Edit Beverage Notes");

        // two textfields: one for beverage name and one for tasted time
        HorizontalLayout row1 = new HorizontalLayout();
        row1.setWidth("90%");
        row1.setSpacing(true);
        beverageName.setLabel("Beverage Name");
        beverageName.setPlaceholder("add a beverage name");

        timesTasted.setLabel("Times Tasted");
        timesTasted.setPlaceholder("add a number ");
        timesTasted.setValue("0");
        timesTasted.setPattern("[0-9]*").setPreventInvalidInput(true);

        row1.add(beverageName, timesTasted);
        reviewFormLayout.add(label, row1);

        // one combobox for choosing category and datepicker for date
        HorizontalLayout row2 = new HorizontalLayout();
        categoryBox.setLabel("chose a category");
        categoryBox.setItems(CategoryService.CATEGORY_NAME);

        lastTasted.setLabel("choose the date");
        lastTasted.setValue(LocalDate.now());

        row2.add(categoryBox, lastTasted);
        row2.setSpacing(true);
        reviewFormLayout.add(row2);

        // combobox for mark a score.
        scoreBox.setWidth("20%");
        scoreBox.setLabel("mark a score");
        scoreBox.setItems("0", "1", "2", "3", "4", "5");
        scoreBox.setValue("0");

        reviewFormLayout.add(scoreBox);

        // three buttons for save, cancel and delete
        HorizontalLayout row4 = new HorizontalLayout();
        row4.setWidth("80%");
        row4.setSpacing(true);
        row4.setHeight("150px");
        row4.setDefaultComponentAlignment(Alignment.END);

        Button save = new Button("Save");
        save.addClickListener(e -> {
            saveClicked();

        });

        Button cancel = new Button("Cancel");
        cancel.addClickListener(e -> cancelClicked());
        Button delete = new Button("Delete");
        delete.setDisabled(true);
        delete.addClickListener(null);
        row4.add(save, cancel, delete);

        reviewFormLayout.add(row4);

        // If modal is true, this implies no-cancel-on-outside-click,
        // no-cancel-on-esc-key and with-backdrop.
        setModal(true);
        add(reviewFormLayout);
    }

    public void clear() {
        binder.setBean(new Review());
    }

    private void cancelClicked() {
        close();
    }

    private void saveClicked() {
        if (binder.isValid()) {
            reviewService.saveReview(binder.getBean());
            reviewsView.updateList();
            close();
        } else {
            close();
        }
    }

}
