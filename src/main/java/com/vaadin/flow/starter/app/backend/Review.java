package com.vaadin.flow.starter.app.backend;

import java.io.Serializable;
import java.time.LocalDate;

public class Review implements Serializable {

    private Long id;
    private int score;
    private String name = "";
    private LocalDate testDate;
    private String reviewCategory;
    private int testTimes;

    public Review(int score, String name, LocalDate testDate,
            String reviewCategory, int testTimes) {
        this.score = score;
        this.name = name;
        this.testDate = testDate;
        this.reviewCategory = reviewCategory;
        this.testTimes = testTimes;
    }

    public Review() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the value of score
     *
     * @return the value of score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set the value of score
     *
     * @param score
     *            new value of Score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name
     *            new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of reviewCategory
     *
     * @return the value of reviewCategory
     */
    public String getReviewCategory() {
        return reviewCategory;
    }

    /**
     * Set the value of reviewCategory
     *
     * @param reviewCategory
     *            new value of reviewCategory
     */
    public void setReviewCategory(String reviewCategory) {
        this.reviewCategory = reviewCategory;
    }

    /**
     * Get the value of testDate
     *
     * @return the value of testDate
     */
    public LocalDate getTestDate() {
        return testDate;
    }

    /**
     * Set the value of testDate
     *
     * @param testDate
     *            new value of testDate
     */
    public void setTestDate(LocalDate date) {
        this.testDate = date;
    }

    /**
     * Get the value of testTimes
     *
     * @return the value of testTimes
     */
    public int getTestTimes() {
        return testTimes;
    }

    /**
     * Set the value of testTimes
     *
     * @param testTimes
     *            new value of testTimes
     */
    public void setTestTimes(int testTimes) {
        this.testTimes = testTimes;
    }

    @Override
    public String toString() {
        return "Review{" + "id=" + id + ", Score=" + score + ", Name=" + name
                + ", Category=" + reviewCategory + ", TestDate" + testDate
                + ", TestTimes" + testTimes + '}';
    }

}
