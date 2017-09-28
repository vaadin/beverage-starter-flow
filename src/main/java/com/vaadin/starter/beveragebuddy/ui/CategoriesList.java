package com.vaadin.starter.beveragebuddy.ui;

import java.util.List;

import com.vaadin.router.Route;
import com.vaadin.router.Title;
import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasValue;
import com.vaadin.ui.grid.Grid;
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

    private final TextField filter = new TextField("", "Search");
    private final Grid<Category> grid = new Grid<>();

    private final CategoryEditorDialog form = new CategoryEditorDialog(
            this::saveCategory, this::deleteCategory);

    private final PaperToast notification = new PaperToast();

    public CategoriesList() {
        initView();

        addSearchBar();
        addGrid();

        updateView();
    }

    private void initView() {
        addClassName("categories-list");

        notification.addClassName("notification");
        add(notification, form);
    }

    private void addSearchBar() {
        HorizontalLayout layout = new HorizontalLayout();
        HorizontalLayout searchField = new HorizontalLayout();
        searchField.add(new Icon(VaadinIcons.SEARCH), filter);
        filter.addValueChangeListener(e -> updateView());
        filter.addClassName("filter-field");
        Button newButton = new Button("New Category",
                new Icon(VaadinIcons.PLUS));
        newButton.addClickListener(e -> form.open(new Category(),
                AbstractEditorDialog.Operation.ADD));
        layout.add(searchField, newButton);

        layout.addClassName("full-width");
        layout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        add(layout);
    }

    private void addGrid() {
        grid.addColumn("CATEGORY", Category::getName);
        grid.addColumn("BEVERAGES", this::getReviewCount);
        // Grid does not yet implement HasStyle
        grid.getElement().getClassList().add("full-width");
        grid.asSingleSelect().addValueChangeListener(this::selectionChanged);
        add(grid);
    }

    private void selectionChanged(
            HasValue.ValueChangeEvent<Grid<Category>, Category> event) {
        Category selectedItem = event.getValue();

        if (selectedItem != null) {
            form.open(selectedItem, AbstractEditorDialog.Operation.EDIT);
            grid.getSelectionModel().deselect(selectedItem);
        }
    }

    private String getReviewCount(Category category) {
        List<Review> reviewsInCategory =
                ReviewService.getInstance().findReviews(category.getName());
        int sum = reviewsInCategory.stream().mapToInt(Review::getCount).sum();
        return Integer.toString(sum);
    }

    private void updateView() {
        List<Category> categories =
                CategoryService.getInstance().findCategories(filter.getValue());
        grid.setItems(categories);
    }

    private void saveCategory(Category category, AbstractEditorDialog.Operation operation) {
        CategoryService.getInstance().saveCategory(category);

        notification.show(
                "Category successfully " + operation.getNameInText() + "ed.");
        updateView();
    }

    private void deleteCategory(Category category) {
        List<Review> reviewsInCategory =
                ReviewService.getInstance().findReviews(category.getName());

        reviewsInCategory.forEach(review -> {
            review.setCategory(null);
            ReviewService.getInstance().saveReview(review);
        });
        CategoryService.getInstance().deleteCategory(category);

        notification.show("Category successfully deleted.");
        updateView();
    }
}
