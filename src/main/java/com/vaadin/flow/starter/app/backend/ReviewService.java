package com.vaadin.flow.starter.app.backend;

import java.time.LocalDate;
import java.util.*;

public class ReviewService {

    static final Map<String, String> BEVERAGES = new LinkedHashMap<>();

    static {
        BEVERAGES.put("Evian", "Mineral Water");
        BEVERAGES.put("Voss", "Mineral Water");
        BEVERAGES.put("Veen", "Mineral Water");
        BEVERAGES.put("San Pellegrino", "Mineral Water");
        BEVERAGES.put("Perrier", "Mineral Water");

        BEVERAGES.put("Coca-Cola", "Soft Drink");
        BEVERAGES.put("Fanta", "Soft Drink");
        BEVERAGES.put("Sprite", "Soft Drink");

        BEVERAGES.put("Maxwell Ready-to-Drink Coffee", "Coffee");
        BEVERAGES.put("Nescafé Gold", "Coffee");
        BEVERAGES.put("Starbucks East Timor Tatamailau", "Coffee");

        BEVERAGES.put("Prince Of Peace Organic White Tea", "Tea");
        BEVERAGES.put("Pai Mu Tan White Peony Tea", "Tea");
        BEVERAGES.put("Tazo Zen Green Tea", "Tea");
        BEVERAGES.put("Dilmah Sencha Green Tea", "Tea");
        BEVERAGES.put("Twinings Earl Grey", "Tea");
        BEVERAGES.put("Twinings Lady Grey", "Tea");
        BEVERAGES.put("Classic Indian Chai", "Tea");

        BEVERAGES.put("Salt Lassi", "Dairy");
        BEVERAGES.put("Mango Lassi", "Dairy");
        BEVERAGES.put("Airag", "Dairy");

        BEVERAGES.put("Crowmoor Extra Dry Apple", "Cider");
        BEVERAGES.put("Golden Cap Perry", "Cider");
        BEVERAGES.put("Somersby Blueberry", "Cider");
        BEVERAGES.put("Kopparbergs Naked Apple Cider", "Cider");
        BEVERAGES.put("Kopparbergs Raspberry", "Cider");
        BEVERAGES.put("Kingstone Press Wild Berry Flavoured Cider", "Cider");
        BEVERAGES.put("Crumpton Oaks Apple Cider", "Cider");
        BEVERAGES.put("Frosty Jack's", "Cider");
        BEVERAGES.put("Ciderboys Mad Bark", "Cider");
        BEVERAGES.put("Angry Orchard Stone Dry", "Cider");
        BEVERAGES.put("Walden Hollow", "Cider");
        BEVERAGES.put("Fox Barrel Wit Pear", "Cider");

        BEVERAGES.put("Budweiser", "Beer");
        BEVERAGES.put("Heineken", "Beer");
        BEVERAGES.put("Holsten Pilsener", "Beer");
        BEVERAGES.put("Krombacher", "Beer");
        BEVERAGES.put("Erdinger Weissbier", "Beer");
        BEVERAGES.put("Weihenstephaner Hefeweissbier", "Beer");
        BEVERAGES.put("Ayinger Kellerbier", "Beer");
        BEVERAGES.put("Guinness Draught", "Beer");
        BEVERAGES.put("Kilkenny Irish Cream Ale", "Beer");
        BEVERAGES.put("Hoegaarden White", "Beer");
        BEVERAGES.put("Barbar", "Beer");
        BEVERAGES.put("Corsendonk Agnus Dei", "Beer");
        BEVERAGES.put("Leffe Blonde", "Beer");
        BEVERAGES.put("Chimay Tripel", "Beer");
        BEVERAGES.put("Duvel", "Beer");
        BEVERAGES.put("Pilsner Urquell", "Beer");
        BEVERAGES.put("Kozel", "Beer");
        BEVERAGES.put("Staropramen", "Beer");
        BEVERAGES.put("Lapin Kulta IVA", "Beer");
        BEVERAGES.put("Kukko Pils III", "Beer");
        BEVERAGES.put("Finlandia Sahti", "Beer");

        BEVERAGES.put("Jacob's Creek Classic Shiraz", "Wine");
        BEVERAGES.put("Chateau d’Yquem Sauternes", "Wine");
        BEVERAGES.put("Oremus Tokaji Aszú 5 Puttonyos", "Wine");

        BEVERAGES.put("Pan Galactic Gargle Blaster", "Other");
        BEVERAGES.put("Mead", "Other");
        BEVERAGES.put("Soma", "Other");
    }

    private static final ReviewService INSTANCE = createDemoReviewService();

    public static ReviewService getInstance() {
        return INSTANCE;
    }

    private static ReviewService createDemoReviewService() {
        final ReviewService reviewService = new ReviewService();
        Random r = new Random();
        int reviewCount = 20 + r.nextInt(30);
        List<Map.Entry<String, String>> beverages = new ArrayList<>(BEVERAGES.entrySet());

        for (int i = 0; i < reviewCount; i++) {
            Review review = new Review();
            Map.Entry<String, String> beverage = beverages.get(r.nextInt(BEVERAGES.size()));

            review.setName(beverage.getKey());
            LocalDate testDay = LocalDate.of(1930 + r.nextInt(88),
                    1 + r.nextInt(12), 1 + r.nextInt(28));
            review.setTestDate(testDay);
            review.setScore(1 + r.nextInt(5));
            review.setReviewCategory(beverage.getValue());
            review.setTestTimes(1 + r.nextInt(15));
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
