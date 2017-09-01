package com.vaadin.flow.starter.app.backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ReviewService {

    // create dummy data

    static final String[] REVIEW_NAMES = { "Coca-Cola", "Fanta", "Sprite",
            "Budweiser", "Heineken", "Guinness", "Maxwell", "Nescafe",
            "Jacob's Creek", "Barefoot" };

    static final String[] CATEGORY_NAMES = { "Soft Drink", "Water", "Milk",
            "Beer", "Coffee", "Wine", "Others" };

    private static final ReviewService INSTANCE = createDemoReviewService();

    public static ReviewService getDemoReviewService() {
        return INSTANCE;
    }

    private static ReviewService createDemoReviewService() {
        final ReviewService reviewService = new ReviewService();
        Random r = new Random(0);

        for (int i = 0; i < 20; i++) {
            Review review = new Review();

            review.setName(REVIEW_NAMES[r.nextInt(REVIEW_NAMES.length)]);
            LocalDate testDay = LocalDate.of(1930 + r.nextInt(87),
                    1 + r.nextInt(11), 1 + r.nextInt(27));
            review.setTestDate(testDay);
            review.setScore(r.nextInt(5));
            review.setReviewCategory(
                    CATEGORY_NAMES[r.nextInt(CATEGORY_NAMES.length)]);
            review.setTestTimes(r.nextInt(100));
            reviewService.saveReview(review);
        }

        return reviewService;
    }

    private Map<Long, Review> reviews = new HashMap<>();
    private long nextId = 0;

    public synchronized List<Review> findReview(String stringFilter) {
        List reviewFindList = new ArrayList();
        String reviewStringFilter = stringFilter.toLowerCase();

        for (Review review : reviews.values()) {

            boolean passesFilter = (stringFilter == null
                    || stringFilter.isEmpty())
                    || review.toString().toLowerCase()
                            .contains(reviewStringFilter);
            if (passesFilter) {
                reviewFindList.add(review);
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

    public synchronized void saveReview(Review entry) {

        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        reviews.put(entry.getId(), entry);
    }
}
