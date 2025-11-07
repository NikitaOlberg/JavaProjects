package com.englishtutor.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class LanguageModel {
    private final List<Word> vocabulary;
    private final List<Word> sessionWords;
    private int currentScore;
    private int currentWordIndex;
    private int totalAttempts;
    private int correctAttempts;
    private final Random random;

    public LanguageModel() {
        this.vocabulary = new ArrayList<>();
        this.sessionWords = new ArrayList<>();
        this.random = new Random();
        this.currentScore = 0;
        this.currentWordIndex = 0;
        this.totalAttempts = 0;
        this.correctAttempts = 0;

        initializeVocabulary();
    }

    private void initializeVocabulary() {
        addWord(new Word("apple", "яблоко", "fruit"));
        addWord(new Word("book", "книга", "education"));
        addWord(new Word("computer", "компьютер", "technology"));
    }

    public void addWord(Word word) {
        if (!vocabulary.contains(word)) {
            vocabulary.add(word);
        }
    }

    public void startNewSession() {
        sessionWords.clear();
        sessionWords.addAll(vocabulary);
        Collections.shuffle(sessionWords);
        currentWordIndex = 0;
        currentScore = 0;
        totalAttempts = 0;
        correctAttempts = 0;
    }

    public Word getCurrentWord() {
        if (sessionWords.isEmpty() || currentWordIndex >= sessionWords.size()) {
            return null;
        }
        return sessionWords.get(currentWordIndex);
    }

    public boolean nextWord() {
        if (currentWordIndex < sessionWords.size() - 1) {
            currentWordIndex++;
            return true;
        }
        return false;
    }

    public boolean checkAnswer(String userAnswer) {
        Word currentWord = getCurrentWord();
        if (currentWord == null) return false;

        currentWord.incrementAttempts();
        totalAttempts++;

        boolean isCorrect = currentWord.getTranslation().equalsIgnoreCase(userAnswer.trim());

        if (isCorrect) {
            currentWord.incrementCorrectAnswers();
            correctAttempts++;
            currentScore += 10;
        }

        return isCorrect;
    }

    public String getHint() {
        Word currentWord = getCurrentWord();
        if (currentWord == null) return "";

        String translation = currentWord.getTranslation();
        if (translation.length() > 1) {
            return translation.substring(0, 1) + "...";
        }
        return translation;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getTotalWords() {
        return vocabulary.size();
    }

    public int getSessionWordsCount() {
        return sessionWords.size();
    }

    public int getCurrentWordNumber() {
        return currentWordIndex + 1;
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public int getCorrectAttempts() {
        return correctAttempts;
    }

    public double getSuccessPercentage() {
        return totalAttempts > 0 ? (double) correctAttempts / totalAttempts * 100 : 0;
    }

    public List<Word> getVocabulary() {
        return new ArrayList<>(vocabulary);
    }

    public boolean isSessionComplete() {
        return currentWordIndex >= sessionWords.size();
    }

    public int getSessionProgress() {
        if (sessionWords.isEmpty()) return 0;
        return (currentWordIndex * 100) / sessionWords.size();
    }
}