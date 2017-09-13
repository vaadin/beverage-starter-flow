package com.vaadin.flow.demo.freestarter.backend;

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
                    StaticData.BEVERAGES.values());

            categoryNames.forEach(name -> categoryService.saveCategory(new Category(name)));

            return categoryService;
        }
    }

    private Map<Long, Category> categories = new HashMap<>();
    private long nextId = 0;

    private CategoryService() {
    }

    public static CategoryService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized List<Category> findCategories(
            String stringCategoryFilter) {
        List<Category> categoryFindList = new ArrayList<>();
        String stringCategoryFilterLoCase = stringCategoryFilter.toLowerCase();

        for (Category category : categories.values()) {

            boolean passesFilter = (stringCategoryFilter == null
                    || stringCategoryFilter.isEmpty())
                    || category.getName().toLowerCase()
                            .contains(stringCategoryFilterLoCase);
            if (passesFilter) {
                // Make a copy to keep entities and DTOs separated
                categoryFindList.add(new Category(category));
            }
        }

        Collections.sort(categoryFindList, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return (int) (o2.getId() - o1.getId());
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
        categories.remove(value.getId());
    }

    public synchronized void saveCategory(Category dto) {
        Category entity = categories.get(dto.getId());

        if (entity == null) {
            // Make a copy to keep entities and DTOs separated
            entity = new Category(dto);
            if (dto.getId() == null) {
                entity.setId(++nextId);
            }
            categories.put(entity.getId(), entity);
        } else {
            entity.setName(dto.getName());
        }
    }

}
