package com.vaadin.starter.beveragebuddy.ui;

import java.util.List;

import com.vaadin.router.Route;
import com.vaadin.router.Title;
import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.html.Label;
import com.vaadin.ui.icon.Icon;
import com.vaadin.ui.icon.VaadinIcons;
import com.vaadin.ui.layout.HorizontalLayout;
import com.vaadin.ui.layout.VerticalLayout;
import com.vaadin.ui.textfield.TextField;

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 */
@Route(value = "categories", layout = MainLayout.class)
@Title("Categories List")
public final class CategoriesList extends VerticalLayout {

    private final transient CategoryService categoryService = CategoryService
            .getInstance();
    private final transient ReviewService reviewService = ReviewService
            .getInstance();

    private final TextField filter = new TextField("", "Search");
    private final VerticalLayout categoryLayout = new VerticalLayout();

    private final CategoryEditorDialog form = new CategoryEditorDialog(
            this::saveCategory, this::deleteCategory);

    private final PaperToast notification = new PaperToast();

    public CategoriesList() {
        initView();

        addSearchBar();
        add(categoryLayout);

        updateView();
    }

    private void initView() {
        addClassName("categories-list");

        notification.addClassName("notification");
        add(notification, form);
    }

    private void updateView() {
        categoryLayout.removeAll();
        List<Category> categories = categoryService
                .findCategories(filter.getValue());
        for (Category category : categories) {
            List<Review> reviewsInCategory = reviewService
                    .findReviews(category.getName());
            int reviewCount = reviewsInCategory.stream()
                    .mapToInt(Review::getCount).sum();
            addRow(category, reviewCount);
        }
    }

    private void addSearchBar() {
        HorizontalLayout layout = new HorizontalLayout();
        HorizontalLayout searchField = new HorizontalLayout();
        searchField.add(new Icon(VaadinIcons.SEARCH), filter);
        filter.addValueChangeListener(e -> updateView());
        Button newButton = new Button("New Category",
                new Icon(VaadinIcons.PLUS));
        newButton.addClickListener(e -> form.open(new Category(),
                AbstractEditorDialog.Operation.ADD));
        layout.add(searchField, newButton);

        layout.setWidth("100%");
        layout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        add(layout);
    }

    private void addRow(Category category, int reviewCount) {
        HorizontalLayout layout = new HorizontalLayout();
        Label name = new Label(category.getName());
        Label counter = new Label(String.valueOf(reviewCount));

        Button editButton = new Button("Edit", new Icon(VaadinIcons.PENCIL));
        editButton.addClickListener(
                e -> form.open(category, AbstractEditorDialog.Operation.EDIT));
        layout.add(name, counter, editButton);
        layout.setWidth("100%");
        layout.getStyle().set("border", "1px solid #9E9E9E");
        layout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        categoryLayout.add(layout);
    }

    private void saveCategory(Category category,
            AbstractEditorDialog.Operation operation) {
        categoryService.saveCategory(category);

        notification.show(
                "Category successfully " + operation.getNameInText() + "ed.");
        updateView();
    }

    private void deleteCategory(Category category) {
        List<Review> reviewsInCategory = reviewService
                .findReviews(category.getName());

        reviewsInCategory.forEach(review -> {
            review.setCategory(null);
            reviewService.saveReview(review);
        });
        categoryService.deleteCategory(category);

        notification.show("Category successfully deleted.");
        updateView();
    }
}
