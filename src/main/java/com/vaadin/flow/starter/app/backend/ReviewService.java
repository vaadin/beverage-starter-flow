package com.vaadin.flow.starter.app.backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

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
            List<Map.Entry<String, String>> beverages = new ArrayList<>(BEVERAGES.entrySet());

            for (int i = 0; i < reviewCount; i++) {
                Review review = new Review();
                Map.Entry<String, String> beverage = beverages.get(r.nextInt(BEVERAGES.size()));
                Category category = CategoryService.getInstance().findCategoryOrThrow(
                        beverage.getValue());

                review.setName(beverage.getKey());
                LocalDate testDay = LocalDate.of(1930 + r.nextInt(88),
                        1 + r.nextInt(12), 1 + r.nextInt(28));
                review.setTestDate(testDay);
                review.setScore(1 + r.nextInt(5));
                review.setReviewCategory(category);
                review.setTestTimes(1 + r.nextInt(15));
                reviewService.saveReview(review);
            }

            return reviewService;
        }

    }

    static final Map<String, String> BEVERAGES = new LinkedHashMap<>();

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
        Category category = dto.getReviewCategory();

        if (category != null) {
            category = CategoryService.getInstance().findCategoryById(
                    category.getCategoryId())
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
        entity.setReviewCategory(category);
    }
}
