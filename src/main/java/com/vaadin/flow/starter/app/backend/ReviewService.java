package com.vaadin.flow.starter.app.backend;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;

public class ReviewService {

    // create dummy data

    static final String[] REVIEW_NAMES = { "Coca-Cola", "Fanta", "Sprite",
            "Budweiser", "Heineken", "Guinness", "Maxwell", "Nescafe",
            "Jacob's Creek", "Barefoot" };

    static final String[] CATEGORY_NAMES = { "Soft Drink", "Water", "Milk",
            "Beer", "Coffee", "Wine", "Others" };

    private static List<Review> reviewList = new ArrayList<Review>();

    public static List<Review> createDemoReviewService() {

        if (reviewList.size() == 0) {
            Random r = new Random(0);
            Calendar cal = Calendar.getInstance();

            for (int i = 0; i < 20; i++) {
                Review review = new Review();

                review.setName(REVIEW_NAMES[r.nextInt(REVIEW_NAMES.length)]);
                cal.set(1930 + r.nextInt(87), r.nextInt(11), r.nextInt(28));
                review.setTestDate(DateToLocalTime(cal.getTime()));
                review.setScore(r.nextInt(5));
                review.setReviewCategory(
                        CATEGORY_NAMES[r.nextInt(CATEGORY_NAMES.length)]);
                review.setTestTimes(r.nextInt(100));
                reviewList.add(review);
            }

        }

        return reviewList;
    }

    private Map<Long, Review> reviews = new HashMap<>();
    private long nextId = 0;

    /**
     * Converts the Java.util.Date to Java.time.LocalTime
     */
    public static synchronized LocalDate DateToLocalTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();

        return localDate;
    }

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
        try {
            entry = (Review) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        reviews.put(entry.getId(), entry);
    }
}
