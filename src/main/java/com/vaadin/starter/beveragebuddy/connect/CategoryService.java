package com.vaadin.starter.beveragebuddy.connect;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.server.connect.VaadinService;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;

@VaadinService
@AnonymousAllowed
public class CategoryService {

    static class CategoryDTO extends Category {

        int beverages;

        public CategoryDTO() {
        }

        public int getBeverages() {
            return beverages;
        }

        public void setBeverages(int beverages) {
            this.beverages = beverages;
        }

        public CategoryDTO(Category entity) {
            super(entity);

            List<Review> reviewsInCategory = ReviewService.getInstance()
                    .findReviews(getName());
            beverages = reviewsInCategory.stream().mapToInt(Review::getCount)
                    .sum();
        }
    }

    public List<CategoryDTO> list(String filter) {
        return com.vaadin.starter.beveragebuddy.backend.CategoryService
                .getInstance().findCategories(filter).stream()
                .map(CategoryDTO::new).collect(Collectors.toList());
    }

    public void save(CategoryDTO category) {
        com.vaadin.starter.beveragebuddy.backend.CategoryService.getInstance()
                .saveCategory(category);
    }
}
