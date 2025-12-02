package com.englishtutor.model;

public class Word {
    private final String english;
    private final String russian;
    private final String category;
    private int attempts;
    private int correctAnswers;

    public Word(String english, String russian, String category) {
        this.english = english;
        this.russian = russian;
        this.category = category;
        this.attempts = 0;
        this.correctAnswers = 0;
    }

    // Геттеры
    public String getEnglish() {
        return english;
    }

    public String getRussian() {
        return russian;
    }

    public String getCategory() {
        return category;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public double getSuccessRate() {
        return attempts > 0 ? (double) correctAnswers / attempts * 100 : 0;
    }

    // Методы для обновления статистики
    public void recordAttempt(boolean isCorrect) {
        attempts++;
        if (isCorrect) {
            correctAnswers++;
        }
    }

    public void resetStatistics() {
        attempts = 0;
        correctAnswers = 0;
    }

    @Override
    public String toString() {
        return english + " - " + russian;
    }
}