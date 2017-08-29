package com.vaadin.flow.starter.app.backend;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;


public class ReviewService {
    
    //create dummy data
    
    static String[] names = {"Radisson Hotel","Hilton Hotel","Best Western",
            "Hyatt","InterContinental","Sheraton","Westin","Marriott","Starwood"};
    
    
    private static ReviewService instance;
    
    public static ReviewService createDemoReviewService() {
        
        if (instance == null){
            final ReviewService reviewService = new ReviewService();
            
            Random r = new Random(0);
            Calendar cal = Calendar.getInstance();
            
            for(int i = 0; i < 20; i++){
                Review review = new Review();
                
                review.setName(names[r.nextInt(names.length)]);
                cal.set(1930 + r.nextInt(87), r.nextInt(11), r.nextInt(28));
                review.setTestDate(cal.getTime());
                review.setScore(r.nextInt(5));
                //review.setReviewCategory(category);
                review.setReviewCategory(Category.values()[r.nextInt(Category.values().length)]);
                review.setTestTimes(r.nextInt(100));
                
                reviewService.saveReview(review);                
            }
            
            instance = reviewService;
        }
        
        return instance;
    }
    
    private HashMap<Long, Review> reviews = new HashMap<>(); 
    private long nextId = 0;
    
    public synchronized List<Review> findReview(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        for (Review review : reviews.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || review.toString().toLowerCase()
                                .contains(stringFilter.toLowerCase());
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
