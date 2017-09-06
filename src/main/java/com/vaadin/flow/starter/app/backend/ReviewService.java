package com.vaadin.flow.starter.app.backend;

import java.time.LocalDate;
import java.util.*;

public class ReviewService {

    private static final String MINERAL_WATER = "Mineral Water";
    private static final String SOFT_DRINK = "Soft Drink";
    private static final String COFFEE = "Coffee";
    private static final String TEA = "Tea";
    private static final String DAIRY = "Dairy";
    private static final String CIDER = "Cider";
    private static final String BEER = "Beer";
    private static final String WINE = "Wine";
    private static final String OTHER = "Other";

    static final Map<String, String> BEVERAGES = new LinkedHashMap<>();

    static {
        BEVERAGES.put("Evian", MINERAL_WATER);
        BEVERAGES.put("Voss", MINERAL_WATER);
        BEVERAGES.put("Veen", MINERAL_WATER);
        BEVERAGES.put("San Pellegrino", MINERAL_WATER);
        BEVERAGES.put("Perrier", MINERAL_WATER);

        BEVERAGES.put("Coca-Cola", SOFT_DRINK);
        BEVERAGES.put("Fanta", SOFT_DRINK);
        BEVERAGES.put("Sprite", SOFT_DRINK);

        BEVERAGES.put("Maxwell Ready-to-Drink Coffee", COFFEE);
        BEVERAGES.put("Nescafé Gold", COFFEE);
        BEVERAGES.put("Starbucks East Timor Tatamailau", COFFEE);

        BEVERAGES.put("Prince Of Peace Organic White Tea", TEA);
        BEVERAGES.put("Pai Mu Tan White Peony Tea", TEA);
        BEVERAGES.put("Tazo Zen Green Tea", TEA);
        BEVERAGES.put("Dilmah Sencha Green Tea", TEA);
        BEVERAGES.put("Twinings Earl Grey", TEA);
        BEVERAGES.put("Twinings Lady Grey", TEA);
        BEVERAGES.put("Classic Indian Chai", TEA);

        BEVERAGES.put("Cow's Milk", DAIRY);
        BEVERAGES.put("Goat's Milk", DAIRY);
        BEVERAGES.put("Unicorn's Milk", DAIRY);
        BEVERAGES.put("Salt Lassi", DAIRY);
        BEVERAGES.put("Mango Lassi", DAIRY);
        BEVERAGES.put("Airag", DAIRY);

        BEVERAGES.put("Crowmoor Extra Dry Apple", CIDER);
        BEVERAGES.put("Golden Cap Perry", CIDER);
        BEVERAGES.put("Somersby Blueberry", CIDER);
        BEVERAGES.put("Kopparbergs Naked Apple Cider", CIDER);
        BEVERAGES.put("Kopparbergs Raspberry", CIDER);
        BEVERAGES.put("Kingstone Press Wild Berry Flavoured Cider", CIDER);
        BEVERAGES.put("Crumpton Oaks Apple Cider", CIDER);
        BEVERAGES.put("Frosty Jack's", CIDER);
        BEVERAGES.put("Ciderboys Mad Bark", CIDER);
        BEVERAGES.put("Angry Orchard Stone Dry", CIDER);
        BEVERAGES.put("Walden Hollow", CIDER);
        BEVERAGES.put("Fox Barrel Wit Pear", CIDER);

        BEVERAGES.put("Budweiser", BEER);
        BEVERAGES.put("Heineken", BEER);
        BEVERAGES.put("Holsten Pilsener", BEER);
        BEVERAGES.put("Krombacher", BEER);
        BEVERAGES.put("Erdinger Weissbier", BEER);
        BEVERAGES.put("Weihenstephaner Hefeweissbier", BEER);
        BEVERAGES.put("Ayinger Kellerbier", BEER);
        BEVERAGES.put("Guinness Draught", BEER);
        BEVERAGES.put("Kilkenny Irish Cream Ale", BEER);
        BEVERAGES.put("Hoegaarden White", BEER);
        BEVERAGES.put("Barbar", BEER);
        BEVERAGES.put("Corsendonk Agnus Dei", BEER);
        BEVERAGES.put("Leffe Blonde", BEER);
        BEVERAGES.put("Chimay Tripel", BEER);
        BEVERAGES.put("Duvel", BEER);
        BEVERAGES.put("Pilsner Urquell", BEER);
        BEVERAGES.put("Kozel", BEER);
        BEVERAGES.put("Staropramen", BEER);
        BEVERAGES.put("Lapin Kulta IVA", BEER);
        BEVERAGES.put("Kukko Pils III", BEER);
        BEVERAGES.put("Finlandia Sahti", BEER);

        BEVERAGES.put("Jacob's Creek Classic Shiraz", WINE);
        BEVERAGES.put("Chateau d’Yquem Sauternes", WINE);
        BEVERAGES.put("Oremus Tokaji Aszú 5 Puttonyos", WINE);

        BEVERAGES.put("Pan Galactic Gargle Blaster", OTHER);
        BEVERAGES.put("Mead", OTHER);
        BEVERAGES.put("Soma", OTHER);
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
