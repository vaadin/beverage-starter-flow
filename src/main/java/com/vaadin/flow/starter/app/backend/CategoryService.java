package com.vaadin.flow.starter.app.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryService {

    // create dummy Category

    public static final String[] CATEGORY_NAME = { "Soft Drink", "Water",
            "Milk", "Beer", "Wine", "Others" };

    private static final CategoryService INSTANCE = createDemoCategoryService();

    public static CategoryService getDemoCategoryService() {
        return INSTANCE;
    }

    private static CategoryService createDemoCategoryService() {

        final CategoryService categoryService = new CategoryService();

        for (int i = 0; i < CATEGORY_NAME.length; i++) {
            Category category = new Category();

            category.setCategoryName(CATEGORY_NAME[i]);

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
                    || category.toString().toLowerCase()
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
