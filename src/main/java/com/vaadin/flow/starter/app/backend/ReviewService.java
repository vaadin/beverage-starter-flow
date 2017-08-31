package com.vaadin.flow.starter.app.backend;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.*;

import org.apache.commons.beanutils.BeanUtils;


public class ReviewService {
    
    //create dummy data
    
    static String[] names = {"Coca-Cola","Fanta","Sprite","Budweiser","Heineken",
            "Guinness","Maxwell","Nescafe","Jacob's Creek","Barefoot"};
    
    static String[] categoryNames = {"Soft Drink","Water","Milk", "Beer","Coffee",
            "Wine","Others"};
    
    
    private static List<Review> reviewList = new ArrayList<Review>();
    
    public static List<Review> createDemoReviewService() {
        
        
        if (reviewList.size() == 0){
            Random r = new Random(0);
            Calendar cal = Calendar.getInstance();
            
            for(int i = 0; i < 20; i++){
                Review review = new Review();
                
                review.setName(names[r.nextInt(names.length)]);
                cal.set(1930 + r.nextInt(87), r.nextInt(11), r.nextInt(28));
                review.setTestDate(DateToLocalTime(cal.getTime()));
                review.setScore(r.nextInt(5));
                review.setReviewCategory(categoryNames[r.nextInt(categoryNames.length)]);
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
        List arrayList = new ArrayList();
        String reviewStringFilter = stringFilter.toLowerCase();
        
        for (Review review : reviews.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || review.toString().toLowerCase()
                                .contains(reviewStringFilter);
                if (passesFilter) {
                    arrayList.add(review.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(ReviewService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Review>() {

            @Override
            public int compare(Review o1, Review o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }

    public synchronized long count() {
        return reviews.size();
    }

    public synchronized void deleteReview(Review value) {
        reviews.remove(value.getId());
    }
    
    public synchronized void saveReview(Review entry) {
        
        if(entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (Review) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        reviews.put(entry.getId(),entry);
    }
}
