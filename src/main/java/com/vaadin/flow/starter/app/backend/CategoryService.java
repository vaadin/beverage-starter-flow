package com.vaadin.flow.starter.app.backend;

import java.io.Serializable;
import java.util.*;

public class CategoryService implements Serializable {

    private static class SingletonHolder {
        private static final CategoryService INSTANCE = createDemoCategoryService();

        private static CategoryService createDemoCategoryService() {
            CategoryService categoryService = new CategoryService();
            Set<Category> categories = new LinkedHashSet<>(
                    ReviewService.BEVERAGES.values());

            categories.forEach(categoryService::saveCategory);

            return categoryService;
        }
    }

    static final Category MINERAL_WATER = new Category("Mineral Water");
    static final Category SOFT_DRINK = new Category("Soft Drink");
    static final Category COFFEE = new Category("Coffee");
    static final Category TEA = new Category("Tea");
    static final Category DAIRY = new Category("Dairy");
    static final Category CIDER = new Category("Cider");
    static final Category BEER = new Category("Beer");
    static final Category WINE = new Category("Wine");
    static final Category OTHER = new Category("Other");

    public static CategoryService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Map<Long, Category> categories = new HashMap<>();
    private long nextId = 0;

    public synchronized List<Category> findCategories(
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

    public synchronized long count() {
        return categories.size();
    }

    public synchronized void deleteCategory(Category value) {
        categories.remove(value.getCategoryId());
    }

    public synchronized void saveCategory(Category entry) {

        if (entry.getCategoryId() == null) {
            entry.setCategoryId(++nextId);
        }
        categories.put(entry.getCategoryId(), entry);
    }

}
