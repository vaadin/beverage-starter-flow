package com.vaadin.flow.demo.freestarter.backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ReviewService {

    /**
     * Helper class to initialize the singleton Service in a thread-safe way
     * and to keep the initialization ordering clear between the two services.
     * See also:
     * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private static class SingletonHolder {
        static final ReviewService INSTANCE = createDemoReviewService();

        private static ReviewService createDemoReviewService() {
            final ReviewService reviewService = new ReviewService();
            Random r = new Random();
            int reviewCount = 20 + r.nextInt(30);
            List<Map.Entry<String, String>> beverages =
                    new ArrayList<>(StaticData.BEVERAGES.entrySet());

            for (int i = 0; i < reviewCount; i++) {
                Review review = new Review();
                Map.Entry<String, String> beverage =
                        beverages.get(r.nextInt(StaticData.BEVERAGES.size()));
                Category category = CategoryService.getInstance().findCategoryOrThrow(
                        beverage.getValue());

                review.setName(beverage.getKey());
                LocalDate testDay = LocalDate.of(1930 + r.nextInt(88),
                        1 + r.nextInt(12), 1 + r.nextInt(28));
                review.setTestDate(testDay);
                review.setScore(1 + r.nextInt(5));
                review.setCategory(category);
                review.setTestTimes(1 + r.nextInt(15));
                reviewService.saveReview(review);
            }

            return reviewService;
        }

    }

    private Map<Long, Review> reviews = new HashMap<>();
    private long nextId = 0;

    private ReviewService() {
    }

    public static ReviewService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized List<Review> findReviews(String stringFilter) {
        List<Review> reviewFindList = new ArrayList<>();
        String reviewStringFilter = stringFilter.toLowerCase();

        for (Review review : reviews.values()) {
            boolean passesFilter = (stringFilter == null
                    || stringFilter.isEmpty())
                    || review.toString().toLowerCase().contains(reviewStringFilter);
            if (passesFilter) {
                // Make a copy to keep entities and DTOs separated
                reviewFindList.add(new Review(review));
            }
        }

        Collections.sort(reviewFindList, new Comparator<Review>() {
            @Override
            public int compare(Review o1, Review o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return reviewFindList;
    }

    public synchronized long count() {
        return reviews.size();
    }

    public synchronized void deleteReview(Review value) {
        reviews.remove(value.getId());
    }

    public synchronized void saveReview(Review dto) {
        Review entity = reviews.get(dto.getId());
        Category category = dto.getCategory();

        if (category != null) {
            category = CategoryService.getInstance().findCategoryById(
                    category.getId())
                    .orElse(null);
        }
        if (entity == null) {
            // Make a copy to keep entities and DTOs separated
            entity = new Review(dto);
            if (dto.getId() == null) {
                entity.setId(++nextId);
            }
            reviews.put(entity.getId(), entity);
        } else {
            entity.setScore(dto.getScore());
            entity.setName(dto.getName());
            entity.setTestDate(dto.getTestDate());
            entity.setTestTimes(dto.getTestTimes());
        }
        entity.setCategory(category);
    }
}
