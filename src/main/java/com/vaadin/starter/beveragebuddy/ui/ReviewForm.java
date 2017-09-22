package com.vaadin.starter.beveragebuddy.ui;

import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.ui.combobox.ComboBox;
import com.vaadin.ui.datepicker.DatePicker;
import com.vaadin.ui.textfield.TextField;

public class ReviewForm extends ItemEditorForm<Review> {

    private transient CategoryService categoryService = CategoryService
            .getInstance();

    private ComboBox<Category> categoryBox = new ComboBox<>();
    private ComboBox<String> scoreBox = new ComboBox<>();
    private DatePicker lastTasted = new DatePicker();
    private TextField beverageName = new TextField();
    private TextField timesTasted = new TextField();

    public ReviewForm(BiConsumer<Review, Operation> saveHandler,
            Consumer<Review> deleteHandler) {
        super("Review", saveHandler, deleteHandler);

        createNameField();
        createTimesField();
        createCategoryBox();
        createDatePicker();
        createScoreBox();
    }

    private void createScoreBox() {
        scoreBox.setLabel("Mark a score");
        scoreBox.setWidth("15em");
        scoreBox.setAllowCustomValue(false);
        scoreBox.setItems("1", "2", "3", "4", "5");
        getFormLayout().add(scoreBox);

        getBinder().forField(scoreBox)
                .withConverter(new StringToIntegerConverter(0,
                        "The score should be a number."))
                .withValidator(score -> score >= 1 && score <= 5,
                        "The score should be between 1 and 5.")
                .bind(Review::getScore, Review::setScore);
    }

    private void createDatePicker() {
        lastTasted.setLabel("Choose the date");
        lastTasted.setMax(LocalDate.now());
        lastTasted.setValue(LocalDate.now());
        getFormLayout().add(lastTasted);

        getBinder().forField(lastTasted)
                .withValidator(date -> date != null,
                        "The date should be in dd/MM/yyyy format.")
                .bind(Review::getDate, Review::setDate);
    }

    private void createCategoryBox() {
        categoryBox.setLabel("Choose a category");
        categoryBox.setItemLabelPath("name");
        categoryBox.setItemValuePath("name");
        categoryBox.setAllowCustomValue(false);
        // TODO disable/hide the Clear button on the combobox
        categoryBox.setWidth("15em");
        categoryBox.setItems(categoryService.findCategories(""));
        getFormLayout().add(categoryBox);

        getBinder().forField(categoryBox)
                .withConverter(categoryService::findCategoryOrThrow,
                        Category::getName)
                .bind(Review::getCategory, Review::setCategory);
    }

    private void createTimesField() {
        timesTasted.setLabel("Times tasted");
        timesTasted.setPattern("[0-9]*");
        timesTasted.setPreventInvalidInput(true);
        getFormLayout().add(timesTasted);

        getBinder().forField(timesTasted)
                .withConverter(
                        new StringToIntegerConverter(0, "Must enter a number."))
                .withValidator(testTimes -> testTimes > 0,
                        "The taste times should be at least 1")
                .bind(Review::getCount, Review::setCount);
    }

    private void createNameField() {
        beverageName.setLabel("Beverage name");
        getFormLayout().add(beverageName);

        getBinder().forField(beverageName)
                .withConverter(String::trim, String::trim)
                .withValidator(name -> name.length() >= 3,
                        "Beverage name must contain at least 3 printable characters")
                .bind(Review::getName, Review::setName);
    }

    @Override protected void confirmDelete() {
        openConfirmationDialog("Delete beverage \""
                        + getCurrentItem().getName() + "\"?",
                "", "");
    }

}
