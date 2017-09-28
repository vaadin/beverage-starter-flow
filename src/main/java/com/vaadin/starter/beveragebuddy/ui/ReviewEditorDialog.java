package com.vaadin.starter.beveragebuddy.ui;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.ui.combobox.ComboBox;
import com.vaadin.ui.datepicker.DatePicker;
import com.vaadin.ui.textfield.TextField;

/**
 * A dialog for editing {@link Review} objects.
 */
public class ReviewEditorDialog extends AbstractEditorDialog<Review> {

    private transient CategoryService categoryService = CategoryService
            .getInstance();

    private ComboBox<Category> categoryBox = new ComboBox<>();
    private ComboBox<String> scoreBox = new ComboBox<>();
    private DatePicker lastTasted = new DatePicker();
    private TextField beverageName = new TextField();
    private TextField timesTasted = new TextField();

    public ReviewEditorDialog(BiConsumer<Review, Operation> saveHandler,
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
        scoreBox.setAllowCustomValue(false);
        scoreBox.setItems("1", "2", "3", "4", "5");
        getFormLayout().add(scoreBox);

        getBinder().forField(scoreBox)
                .withConverter(new StringToIntegerConverter(0,
                        "The score should be a number."))
                .withValidator(new IntegerRangeValidator(
                        "The tasting count must be between 1 and 5.", 1, 5))
                .bind(Review::getScore, Review::setScore);
    }

    private void createDatePicker() {
        lastTasted.setLabel("Choose the date");
        lastTasted.setMax(LocalDate.now());
        lastTasted.setMin(LocalDate.of(1, 1, 1));
        lastTasted.setValue(LocalDate.now());
        getFormLayout().add(lastTasted);

        getBinder().forField(lastTasted)
                .withValidator(Objects::nonNull,
                        "The date should be in MM/dd/yyyy format.")
                .withValidator(new DateRangeValidator(
                        "The date should be neither Before Christ nor in the future.",
                        LocalDate.of(1, 1, 1), LocalDate.now()))
                .bind(Review::getDate, Review::setDate);

    }

    private void createCategoryBox() {
        categoryBox.setLabel("Choose a category");
        categoryBox.setItemLabelGenerator(Category::getName);
        categoryBox.setAllowCustomValue(false);
        categoryBox.setItems(categoryService.findCategories(""));
        getFormLayout().add(categoryBox);

        getBinder().forField(categoryBox).bind(Review::getCategory,
                Review::setCategory);
    }

    private void createTimesField() {
        timesTasted.setLabel("Times tasted");
        timesTasted.setPattern("[0-9]*");
        timesTasted.setPreventInvalidInput(true);
        getFormLayout().add(timesTasted);

        getBinder().forField(timesTasted)
                .withConverter(
                        new StringToIntegerConverter(0, "Must enter a number."))
                .withValidator(new IntegerRangeValidator(
                        "The tasting count must be between 1 and 99.", 1, 99))
                .bind(Review::getCount, Review::setCount);
    }

    private void createNameField() {
        beverageName.setLabel("Beverage name");
        getFormLayout().add(beverageName);

        getBinder().forField(beverageName)
                .withConverter(String::trim, String::trim)
                .withValidator(new StringLengthValidator(
                        "Beverage name must contain at least 3 printable characters",
                        3, null))
                .bind(Review::getName, Review::setName);
    }

    @Override
    protected void confirmDelete() {
        openConfirmationDialog(
                "Delete beverage \"" + getCurrentItem().getName() + "\"?", "",
                "");
    }

}
