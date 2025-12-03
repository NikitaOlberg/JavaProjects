package com.englishtutor.model;

public class Word {
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
        if (getSuccessRate() < 70) return true;

        long daysSinceLastPractice =
                (System.currentTimeMillis() - lastPracticed) / (1000 * 60 * 60 * 24);

        return daysSinceLastPractice > 7;
    }

    public int getPointsForCorrectAnswer() {
        return 10;
    }

    @Override
    public String toString() {
        return english + " - " + russian;
    }
}