package com.vaadin.flow.starter.app.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;

public class CategoryService {

    // create dummy Category

    public static String[] categoryNames = { "Soft Drink", "Water", "Milk",
            "Beer", "Wine", "Others" };

    private static CategoryService instance;

    public static CategoryService createDemoCategoryService() {

        if (instance == null) {
            final CategoryService categoryService = new CategoryService();

            for (int i = 0; i < categoryNames.length; i++) {
                Category category = new Category();

                category.setCategoryName(categoryNames[i]);

                categoryService.saveCategory(category);
            }

            instance = categoryService;
        }

        return instance;
    }

    private Map<Long, Category> categories = new HashMap<>();
    private long nextId = 0;

    public synchronized List<Category> findCategory(
            String stringCategoryFilter) {
        List arrayList = new ArrayList();
        String stringCategoryFilterLoCase = stringCategoryFilter.toLowerCase();

        for (Category category : categories.values()) {
            try {
                boolean passesFilter = (stringCategoryFilter == null
                        || stringCategoryFilter.isEmpty())
                        || category.toString().toLowerCase()
                                .contains(stringCategoryFilterLoCase);
                if (passesFilter) {
                    arrayList.add(category.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(CategoryService.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        Collections.sort(arrayList, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return (int) (o2.getCategoryId() - o1.getCategoryId());
            }
        });

        return arrayList;
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
        try {
            entry = (Category) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        categories.put(entry.getCategoryId(), entry);
    }

}
