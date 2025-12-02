package com.englishtutor.model;

import java.util.*;

public class LanguageModel {
    private List<Word> vocabulary;
    private List<Word> sessionWords;
    private int currentScore;
    private int currentWordIndex;
    private int totalAttempts;
    private int correctAttempts;
    private int maxStreak;
    private int currentStreak;
    private String currentMode; // "en-ru" или "ru-en"
    private Random random;

    public LanguageModel() {
        this.vocabulary = new ArrayList<>();
        this.sessionWords = new ArrayList<>();
        this.random = new Random();
        this.currentScore = 0;
        this.currentWordIndex = 0;
        this.totalAttempts = 0;
        this.correctAttempts = 0;
        this.maxStreak = 0;
        this.currentStreak = 0;
        this.currentMode = "en-ru"; // По умолчанию английский → русский

        initializeVocabulary();
    }

    private void initializeVocabulary() {
        // Базовый набор слов
        vocabulary.add(new Word("hello", "привет", "Основные"));
        vocabulary.add(new Word("goodbye", "до свидания", "Основные"));
        vocabulary.add(new Word("thank you", "спасибо", "Основные"));
        vocabulary.add(new Word("please", "пожалуйста", "Основные"));
        vocabulary.add(new Word("yes", "да", "Основные"));
        vocabulary.add(new Word("no", "нет", "Основные"));
        vocabulary.add(new Word("sorry", "извините", "Основные"));

        vocabulary.add(new Word("apple", "яблоко", "Еда"));
        vocabulary.add(new Word("bread", "хлеб", "Еда"));
        vocabulary.add(new Word("water", "вода", "Еда"));
        vocabulary.add(new Word("coffee", "кофе", "Еда"));
        vocabulary.add(new Word("milk", "молоко", "Еда"));
        vocabulary.add(new Word("meat", "мясо", "Еда"));
        vocabulary.add(new Word("vegetable", "овощ", "Еда"));

        vocabulary.add(new Word("house", "дом", "Дом"));
        vocabulary.add(new Word("room", "комната", "Дом"));
        vocabulary.add(new Word("kitchen", "кухня", "Дом"));
        vocabulary.add(new Word("bathroom", "ванная", "Дом"));
        vocabulary.add(new Word("window", "окно", "Дом"));
        vocabulary.add(new Word("door", "дверь", "Дом"));
        vocabulary.add(new Word("bed", "кровать", "Дом"));

        vocabulary.add(new Word("family", "семья", "Семья"));
        vocabulary.add(new Word("mother", "мать", "Семья"));
        vocabulary.add(new Word("father", "отец", "Семья"));
        vocabulary.add(new Word("brother", "брат", "Семья"));
        vocabulary.add(new Word("sister", "сестра", "Семья"));
        vocabulary.add(new Word("friend", "друг", "Семья"));

        vocabulary.add(new Word("work", "работа", "Работа"));
        vocabulary.add(new Word("office", "офис", "Работа"));
        vocabulary.add(new Word("meeting", "встреча", "Работа"));
        vocabulary.add(new Word("computer", "компьютер", "Работа"));
        vocabulary.add(new Word("phone", "телефон", "Работа"));
        vocabulary.add(new Word("email", "электронная почта", "Работа"));
    }

    public void startNewSession(int wordCount, String mode) {
        currentMode = mode;
        sessionWords.clear();

        // Копируем слова
        List<Word> tempList = new ArrayList<>(vocabulary);
        Collections.shuffle(tempList);

        // Ограничиваем количество слов
        if (wordCount > 0 && wordCount < tempList.size()) {
            sessionWords = new ArrayList<>(tempList.subList(0, wordCount));
        } else {
            sessionWords = new ArrayList<>(tempList);
        }

        // Сбрасываем состояние сессии
        currentWordIndex = 0;
        currentScore = 0;
        totalAttempts = 0;
        correctAttempts = 0;
        maxStreak = 0;
        currentStreak = 0;
    }

    public Word getCurrentWord() {
        if (sessionWords.isEmpty() || currentWordIndex >= sessionWords.size()) {
            return null;
        }
        return sessionWords.get(currentWordIndex);
    }

    public String getQuestion() {
        Word word = getCurrentWord();
        if (word == null) return "";

        return currentMode.equals("en-ru") ? word.getEnglish() : word.getRussian();
    }

    public String getCorrectAnswer() {
        Word word = getCurrentWord();
        if (word == null) return "";

        return currentMode.equals("en-ru") ? word.getRussian() : word.getEnglish();
    }

    public boolean checkAnswer(String userAnswer) {
        Word currentWord = getCurrentWord();
        if (currentWord == null) return false;

        String normalizedAnswer = userAnswer.trim().toLowerCase();
        String correctAnswer = getCorrectAnswer().toLowerCase();

        boolean isCorrect = normalizedAnswer.equals(correctAnswer);

        // Обновляем статистику слова
        currentWord.recordAttempt(isCorrect);

        // Обновляем общую статистику
        totalAttempts++;

        if (isCorrect) {
            correctAttempts++;
            currentStreak++;
            maxStreak = Math.max(maxStreak, currentStreak);
            currentScore += 10; // 10 очков за правильный ответ
        } else {
            currentStreak = 0;
        }

        return isCorrect;
    }

    public void nextWord() {
        if (currentWordIndex < sessionWords.size() - 1) {
            currentWordIndex++;
        }
    }

    public boolean isSessionComplete() {
        return sessionWords.isEmpty() || currentWordIndex >= sessionWords.size();
    }

    public String getHint() {
        Word currentWord = getCurrentWord();
        if (currentWord == null) return "";

        String answer = getCorrectAnswer();
        if (answer.length() <= 2) {
            return answer;
        }

        // Показываем первую и последнюю буквы
        return answer.charAt(0) + "..." + answer.charAt(answer.length() - 1);
    }

    // Геттеры
    public int getCurrentScore() { return currentScore; }
    public int getTotalWords() { return vocabulary.size(); }
    public int getSessionWordsCount() { return sessionWords.size(); }
    public int getCurrentWordNumber() { return currentWordIndex + 1; }
    public int getTotalAttempts() { return totalAttempts; }
    public int getCorrectAttempts() { return correctAttempts; }
    public double getSuccessPercentage() {
        return totalAttempts > 0 ? (double) correctAttempts / totalAttempts * 100 : 0;
    }
    public int getCurrentStreak() { return currentStreak; }
    public int getMaxStreak() { return maxStreak; }
    public String getCurrentMode() { return currentMode; }

    public int getProgressPercentage() {
        return sessionWords.isEmpty() ? 0 :
                (currentWordIndex * 100) / sessionWords.size();
    }

    public List<String> getAvailableModes() {
        return Arrays.asList("en-ru", "ru-en");
    }
}