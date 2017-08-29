package com.vaadin.flow.starter.app.backend;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable, Cloneable {

    private long id;
    private int score;
    private String name = "";
    private Date testDate;
    private Category reviewCategory;
    private int testTimes; 
    
    //review ID in the app is the unique identification of every record
    public Long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    //get the test score and return
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    //get the test content and return
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    //get the ReviewCategory and return
    public Category getReviewCategory() {
        return reviewCategory;
    }
    
    public void setReviewCategory(Category reviewCategory) {
        this.reviewCategory = reviewCategory;
    }
    
    //get the testDate and return
    public Date getTestDate() {
        return testDate;
    }
    
    public void setTestDate(Date date) {
        this.testDate = date;
    }
    
    //get the testTimes and return
    public int getTestTimes() {
        return testTimes;
    }
    
    public void setTestTimes(int testTimes) {
        this.testTimes = testTimes;
    }
    
    @Override
    public Review clone() throws CloneNotSupportedException {
        return (Review) super.clone();
    }
    
    @Override
    public String toString() {
        return "Review{" + "id=" + id +", Score=" + score +", Name=" + name 
                + ", Category=" + reviewCategory + ", TestDate" + testDate 
                + ", TestTimes" + testTimes + '}';
    }
   
}
