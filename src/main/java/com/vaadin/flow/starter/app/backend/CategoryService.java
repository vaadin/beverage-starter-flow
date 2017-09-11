package com.vaadin.flow.starter.app.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CategoryService {

    /**
     * Helper class to initialize the singleton Service in a thread-safe way
     * and to keep the initialization ordering clear between the two services.
     * See also:
     * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private static class SingletonHolder {
        static final CategoryService INSTANCE = createDemoCategoryService();

        private static CategoryService createDemoCategoryService() {
            CategoryService categoryService = new CategoryService();
            Set<String> categoryNames = new LinkedHashSet<>(
                    ReviewService.BEVERAGES.values());

            categoryNames.forEach(name -> categoryService.saveCategory(new Category(name)));

            return categoryService;
        }
    }

    static final String MINERAL_WATER = "Mineral Water";
    static final String SOFT_DRINK = "Soft Drink";
    static final String COFFEE = "Coffee";
    static final String TEA = "Tea";
    static final String DAIRY = "Dairy";
    static final String CIDER = "Cider";
    static final String BEER = "Beer";
    static final String WINE = "Wine";
    static final String OTHER = "Other";

    public static CategoryService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Map<Long, Category> categories = new HashMap<>();
    private long nextId = 0;

    private CategoryService() {
    }

    public synchronized List<Category> findCategories(
            String stringCategoryFilter) {
        List<Category> categoryFindList = new ArrayList<>();
        String stringCategoryFilterLoCase = stringCategoryFilter.toLowerCase();

        for (Category category : categories.values()) {

            boolean passesFilter = (stringCategoryFilter == null
                    || stringCategoryFilter.isEmpty())
                    || category.getCategoryName().toLowerCase()
                            .contains(stringCategoryFilterLoCase);
            if (passesFilter) {
                categoryFindList.add(new Category(category));
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

    public synchronized Optional<Category> findCategoryByName(String name) {
        List<Category> categoriesMatching = findCategories(name);

        if (categoriesMatching.isEmpty()) {
            return Optional.empty();
        }
        if (categoriesMatching.size() > 1) {
            throw new IllegalStateException("Category " + name + " is ambiguous");
        }
        return Optional.of(categoriesMatching.get(0));
    }

    public Category findCategoryOrThrow(String name) {
        return findCategoryByName(name)
                .orElseThrow(() -> new IllegalStateException("Category " + name + " does not exist"));
    }

    public Optional<Category> findCategoryById(Long id) {
        Category category = categories.get(id);
        return Optional.ofNullable(category);
    }

    public synchronized long count() {
        return categories.size();
    }

    public synchronized void deleteCategory(Category value) {
        categories.remove(value.getCategoryId());
    }

    public synchronized void saveCategory(Category dto) {
        Category entity = categories.get(dto.getCategoryId());

        if (entity == null) {
            entity = new Category(dto);
            if (dto.getCategoryId() == null) {
                entity.setCategoryId(++nextId);
            }
            categories.put(entity.getCategoryId(), entity);
        } else {
            entity.setCategoryName(dto.getCategoryName());
        }
    }

}
