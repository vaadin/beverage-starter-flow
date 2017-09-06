package com.vaadin.flow.starter.app.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CategoryService implements Serializable {

    private static final CategoryService INSTANCE = createDemoCategoryService();

    public static CategoryService getInstance() {
        return INSTANCE;
    }

    private static CategoryService createDemoCategoryService() {

        CategoryService categoryService = new CategoryService();
        Set<String> categories = new LinkedHashSet<>(
                ReviewService.BEVERAGES.values());

        for (String categoryName : categories) {
            Category category = new Category();

            category.setCategoryName(categoryName);

            categoryService.saveCategory(category);
        }

        return categoryService;
    }

    private Map<Long, Category> categories = new HashMap<>();
    private long nextId = 0;

    public synchronized List<Category> findCategory(
            String stringCategoryFilter) {
        List categoryFindList = new ArrayList();
        String stringCategoryFilterLoCase = stringCategoryFilter.toLowerCase();

        for (Category category : categories.values()) {

            boolean passesFilter = (stringCategoryFilter == null
                    || stringCategoryFilter.isEmpty())
                    || category.getCategoryName().toLowerCase()
                            .contains(stringCategoryFilterLoCase);
            if (passesFilter) {
                categoryFindList.add(category);
            }
        }

        Collections.sort(categoryFindList, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return (int) (o2.getCategoryId() - o1.getCategoryId());
            }
        });

        return categoryFindList;
    }

    public synchronized long count() {
        return categories.size();
    }

    public synchronized void deleteCategory(Category value) {
        categories.remove(value.getCategoryId());
    }

    public synchronized void saveCategory(Category entry) {

        if (entry.getCategoryId() == null) {
            entry.setCategoryId(nextId++);
        }
        categories.put(entry.getCategoryId(), entry);
    }

}
