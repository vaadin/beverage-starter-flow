package com.vaadin.flow.starter.app.backend;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class ReviewService implements Serializable {

    static final Map<String, Category> BEVERAGES = new LinkedHashMap<>();

    static {
        Stream.of("Evian", "Voss", "Veen", "San Pellegrino", "Perrier")
                .forEach(name -> BEVERAGES.put(name, CategoryService.MINERAL_WATER));

        Stream.of("Coca-Cola", "Fanta", "Sprite")
                .forEach(name -> BEVERAGES.put(name, CategoryService.SOFT_DRINK));

        Stream.of("Maxwell Ready-to-Drink Coffee", "Nescafé Gold", "Starbucks East Timor Tatamailau")
                .forEach(name -> BEVERAGES.put(name, CategoryService.COFFEE));

        Stream.of("Prince Of Peace Organic White Tea", "Pai Mu Tan White Peony Tea",
                "Tazo Zen Green Tea", "Dilmah Sencha Green Tea",
                "Twinings Earl Grey", "Twinings Lady Grey", "Classic Indian Chai")
                .forEach(name -> BEVERAGES.put(name, CategoryService.TEA));

        Stream.of("Cow's Milk", "Goat's Milk", "Unicorn's Milk", "Salt Lassi", "Mango Lassi", "Airag")
                .forEach(name -> BEVERAGES.put(name, CategoryService.DAIRY));

        Stream.of("Crowmoor Extra Dry Apple", "Golden Cap Perry", "Somersby Blueberry",
                "Kopparbergs Naked Apple Cider", "Kopparbergs Raspberry",
                "Kingstone Press Wild Berry Flavoured Cider", "Crumpton Oaks Apple", "Frosty Jack's",
                "Ciderboys Mad Bark", "Angry Orchard Stone Dry", "Walden Hollow", "Fox Barrel Wit Pear")
                .forEach(name -> BEVERAGES.put(name, CategoryService.CIDER));

        Stream.of("Budweiser", "Miller",
                "Heineken", "Holsten Pilsener", "Krombacher", "Weihenstephaner Hefeweissbier", "Ayinger Kellerbier",
                "Guinness Draught", "Kilkenny Irish Cream Ale",
                "Hoegaarden White", "Barbar", "Corsendonk Agnus Dei", "Leffe Blonde", "Chimay Tripel", "Duvel",
                "Pilsner Urquell", "Kozel", "Staropramen",
                "Lapin Kulta IVA", "Kukko Pils III", "Finlandia Sahti")
                .forEach(name -> BEVERAGES.put(name, CategoryService.BEER));

        Stream.of("Jacob's Creek Classic Shiraz", "Chateau d’Yquem Sauternes", "Oremus Tokaji Aszú 5 Puttonyos")
                .forEach(name -> BEVERAGES.put(name, CategoryService.WINE));

        Stream.of("Pan Galactic Gargle Blaster", "Mead", "Soma")
                .forEach(name -> BEVERAGES.put(name, CategoryService.OTHER));
    }

    private static final ReviewService INSTANCE = createDemoReviewService();

    public static ReviewService getInstance() {
        return INSTANCE;
    }

    private static ReviewService createDemoReviewService() {
        final ReviewService reviewService = new ReviewService();
        Random r = new Random();
        int reviewCount = 20 + r.nextInt(30);
        List<Map.Entry<String, Category>> beverages = new ArrayList<>(BEVERAGES.entrySet());

        for (int i = 0; i < reviewCount; i++) {
            Review review = new Review();
            Map.Entry<String, Category> beverage = beverages.get(r.nextInt(BEVERAGES.size()));

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

    public synchronized List<Review> findReviews(String stringFilter) {
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
            entry.setId(++nextId);
        }
        reviews.put(entry.getId(), entry);
    }
}
