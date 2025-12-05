package com.englishtutor.model;

public class Word {
    private static final int DEFAULT_POINTS_FOR_CORRECT = 10;
    private static final int REVIEW_THRESHOLD_PERCENTAGE = 70;
    private static final int REVIEW_DAYS_THRESHOLD = 7;
    private static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

    private final String english;
    private final String russian;
    private final String category;
    private int attempts;
    private int correctAnswers;
    private long lastPracticed;

    public Word(String english, String russian, String category) {
        this.english = english;
        this.russian = russian;
        this.category = category;
        this.attempts = 0;
        this.correctAnswers = 0;
        this.lastPracticed = System.currentTimeMillis();
    }

    public String getEnglish() { return english; }
    public String getRussian() { return russian; }
    public String getCategory() { return category; }
    public int getAttempts() { return attempts; }
    public int getCorrectAnswers() { return correctAnswers; }
    public long getLastPracticed() { return lastPracticed; }

    public double getSuccessRate() {
        return attempts > 0 ? (double) correctAnswers / attempts * 100 : 0;
    }

    public void recordAttempt(boolean isCorrect) {
        attempts++;
        if (isCorrect) {
            correctAnswers++;
        }
        lastPracticed = System.currentTimeMillis();
    }

    public void resetStatistics() {
        attempts = 0;
        correctAnswers = 0;
        lastPracticed = System.currentTimeMillis();
    }

    public boolean needsReview() {
        if (attempts == 0) return true;

        boolean lowSuccessRate = getSuccessRate() < REVIEW_THRESHOLD_PERCENTAGE;
        boolean longTimeSincePractice = getDaysSinceLastPractice() > REVIEW_DAYS_THRESHOLD;

        return lowSuccessRate || longTimeSincePractice;
    }

    private long getDaysSinceLastPractice() {
        return (System.currentTimeMillis() - lastPracticed) / MILLISECONDS_IN_DAY;
    }

    public int getPointsForCorrectAnswer() {
        return DEFAULT_POINTS_FOR_CORRECT;
    }

    @Override
    public String toString() {
        return english + " - " + russian;
    }
}