package model;

public class Word {
    private final String english;
    private final String translation;
    private final String category;
    private int attempts;
    private int correctAnswers;

    public Word(String english, String translation, String category) {
        this.english = english;
        this.translation = translation;
        this.category = category;
        this.attempts = 0;
        this.correctAnswers = 0;
    }

    public String getEnglish() {
        return english;
    }

    public String getTranslation() {
        return translation;
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

    public void incrementAttempts() {
        attempts++;
    }

    public void incrementCorrectAnswers() {
        correctAnswers++;
    }

    public void resetStatistics() {
        attempts = 0;
        correctAnswers = 0;
    }

    @Override
    public String toString() {
        return String.format("Word{english='%s', translation='%s', category='%s'}",
                english, translation, category);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Word word = (Word) obj;
        return english.equals(word.english) &&
                translation.equals(word.translation) &&
                category.equals(word.category);
    }

    @Override
    public int hashCode() {
        int result = english.hashCode();
        result = 31 * result + translation.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }
}