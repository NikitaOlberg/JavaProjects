package com.englishtutor.model;

import com.englishtutor.events.GameEventListener;
import com.englishtutor.events.GameEvent;
import com.englishtutor.events.ScoreChangedEvent;
import java.util.*;
import java.util.stream.Collectors;

public class LanguageModel {
    private Map<String, List<Word>> wordsByCategory;
    private List<Word> currentSessionWords;
    private Set<Word> masteredWords;
    private Map<String, Integer> categoryProgress;
    private List<GameEventListener> listeners;

    private int currentScore;
    private int currentWordIndex;
    private int totalAttempts;
    private int correctAttempts;
    private int maxStreak;
    private int currentStreak;
    private int level;
    private int experience;

    private String currentMode;
    private String currentCategory;
    private String currentGameMode;

    private Random random;
    private int timeLeft;

    public LanguageModel() {
        this.wordsByCategory = new HashMap<>();
        this.currentSessionWords = new ArrayList<>();
        this.masteredWords = new HashSet<>();
        this.categoryProgress = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.random = new Random();

        this.currentScore = 0;
        this.currentWordIndex = 0;
        this.totalAttempts = 0;
        this.correctAttempts = 0;
        this.maxStreak = 0;
        this.currentStreak = 0;
        this.level = 1;
        this.experience = 0;

        this.currentMode = "en-ru";
        this.currentCategory = "Все";
        this.currentGameMode = "standard";

        initializeVocabulary();
        initializeCategoryProgress();
    }

    private void initializeVocabulary() {
        addWordsToCategory("Основные", Arrays.asList(
                new Word("hello", "привет", "Основные"),
                new Word("goodbye", "до свидания", "Основные"),
                new Word("thank you", "спасибо", "Основные"),
                new Word("please", "пожалуйста", "Основные"),
                new Word("yes", "да", "Основные"),
                new Word("no", "нет", "Основные"),
                new Word("sorry", "извините", "Основные"),
                new Word("good morning", "доброе утро", "Основные"),
                new Word("good night", "спокойной ночи", "Основные")
        ));

        addWordsToCategory("Еда", Arrays.asList(
                new Word("apple", "яблоко", "Еда"),
                new Word("bread", "хлеб", "Еда"),
                new Word("water", "вода", "Еда"),
                new Word("coffee", "кофе", "Еда"),
                new Word("milk", "молоко", "Еда"),
                new Word("meat", "мясо", "Еда"),
                new Word("vegetable", "овощ", "Еда"),
                new Word("restaurant", "ресторан", "Еда"),
                new Word("breakfast", "завтрак", "Еда"),
                new Word("dinner", "ужин", "Еда")
        ));

        addWordsToCategory("Дом", Arrays.asList(
                new Word("house", "дом", "Дом"),
                new Word("room", "комната", "Дом"),
                new Word("kitchen", "кухня", "Дом"),
                new Word("bathroom", "ванная", "Дом"),
                new Word("window", "окно", "Дом"),
                new Word("door", "дверь", "Дом"),
                new Word("bed", "кровать", "Дом"),
                new Word("furniture", "мебель", "Дом"),
                new Word("apartment", "квартира", "Дом")
        ));

        addWordsToCategory("Семья", Arrays.asList(
                new Word("family", "семья", "Семья"),
                new Word("mother", "мать", "Семья"),
                new Word("father", "отец", "Семья"),
                new Word("brother", "брат", "Семья"),
                new Word("sister", "сестра", "Семья"),
                new Word("friend", "друг", "Семья"),
                new Word("grandmother", "бабушка", "Семья"),
                new Word("grandfather", "дедушка", "Семья"),
                new Word("cousin", "двоюродный брат/сестра", "Семья")
        ));

        addWordsToCategory("Работа", Arrays.asList(
                new Word("work", "работа", "Работа"),
                new Word("office", "офис", "Работа"),
                new Word("meeting", "встреча", "Работа"),
                new Word("computer", "компьютер", "Работа"),
                new Word("phone", "телефон", "Работа"),
                new Word("email", "электронная почта", "Работа"),
                new Word("boss", "начальник", "Работа"),
                new Word("colleague", "коллега", "Работа"),
                new Word("salary", "зарплата", "Работа")
        ));

        addWordsToCategory("Путешествия", Arrays.asList(
                new Word("travel", "путешествие", "Путешествия"),
                new Word("airport", "аэропорт", "Путешествия"),
                new Word("hotel", "отель", "Путешествия"),
                new Word("passport", "паспорт", "Путешествия"),
                new Word("ticket", "билет", "Путешествия"),
                new Word("luggage", "багаж", "Путешествия"),
                new Word("map", "карта", "Путешествия"),
                new Word("tourist", "турист", "Путешествия")
        ));
    }

    private void addWordsToCategory(String category, List<Word> words) {
        wordsByCategory.putIfAbsent(category, new ArrayList<>());
        wordsByCategory.get(category).addAll(words);
    }

    private void initializeCategoryProgress() {
        for (String category : wordsByCategory.keySet()) {
            categoryProgress.put(category, 0);
        }
    }

    public void addGameEventListener(GameEventListener listener) {
        listeners.add(listener);
    }

    public void removeGameEventListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    private void fireGameEvent(GameEvent event) {
        for (GameEventListener listener : listeners) {
            listener.onGameEvent(event);
        }
    }

    public void startNewSession(String category, String mode, String gameMode, int wordCount, int timeLimit) {
        currentCategory = category;
        currentMode = mode;
        currentGameMode = gameMode;

        currentSessionWords.clear();

        switch (gameMode) {
            case "standard":
                prepareStandardSession(category, wordCount);
                break;
            case "review":
                prepareReviewSession(category, wordCount);
                break;
            case "timed":
                prepareTimedSession(category, wordCount);
                timeLeft = timeLimit;
                break;
            case "challenge":
                prepareChallengeSession(category, wordCount);
                break;
        }

        Collections.shuffle(currentSessionWords);

        if (wordCount > 0 && wordCount < currentSessionWords.size()) {
            currentSessionWords = new ArrayList<>(currentSessionWords.subList(0, wordCount));
        }

        currentWordIndex = 0;
        currentScore = 0;
        currentStreak = 0;
        totalAttempts = 0;
        correctAttempts = 0;

        fireGameEvent(new GameEvent(this, "SESSION_STARTED", null));
    }

    private void prepareStandardSession(String category, int wordCount) {
        if ("Все".equals(category)) {
            for (List<Word> words : wordsByCategory.values()) {
                currentSessionWords.addAll(words);
            }
        } else {
            List<Word> categoryWords = wordsByCategory.get(category);
            if (categoryWords != null) {
                currentSessionWords.addAll(categoryWords);
            }
        }
    }

    private void prepareReviewSession(String category, int wordCount) {
        List<Word> allWords = new ArrayList<>();

        if ("Все".equals(category)) {
            for (List<Word> words : wordsByCategory.values()) {
                allWords.addAll(words);
            }
        } else {
            List<Word> categoryWords = wordsByCategory.get(category);
            if (categoryWords != null) {
                allWords.addAll(categoryWords);
            }
        }

        currentSessionWords = allWords.stream()
                .filter(Word::needsReview)
                .sorted(Comparator.comparing(Word::getSuccessRate))
                .collect(Collectors.toList());
    }

    private void prepareTimedSession(String category, int wordCount) {
        prepareStandardSession(category, wordCount);
    }

    private void prepareChallengeSession(String category, int wordCount) {
        List<Word> allWords = new ArrayList<>();

        if ("Все".equals(category)) {
            for (List<Word> words : wordsByCategory.values()) {
                allWords.addAll(words);
            }
        } else {
            List<Word> categoryWords = wordsByCategory.get(category);
            if (categoryWords != null) {
                allWords.addAll(categoryWords);
            }
        }

        currentSessionWords = new ArrayList<>(allWords);
    }

    public Word getCurrentWord() {
        if (currentSessionWords.isEmpty() || currentWordIndex >= currentSessionWords.size()) {
            return null;
        }
        return currentSessionWords.get(currentWordIndex);
    }

    public String getQuestion() {
        Word word = getCurrentWord();
        if (word == null) return "";

        if ("mixed".equals(currentMode)) {
            return random.nextBoolean() ? word.getEnglish() : word.getRussian();
        }

        return currentMode.equals("en-ru") ? word.getEnglish() : word.getRussian();
    }

    public String getCorrectAnswer() {
        Word word = getCurrentWord();
        if (word == null) return "";

        String question = getQuestion();

        if (question.equals(word.getEnglish())) {
            return word.getRussian();
        } else {
            return word.getEnglish();
        }
    }

    public boolean checkAnswer(String userAnswer) {
        Word currentWord = getCurrentWord();
        if (currentWord == null) return false;

        String normalizedAnswer = userAnswer.trim().toLowerCase();
        String correctAnswer = getCorrectAnswer().toLowerCase();

        boolean isCorrect = normalizedAnswer.equals(correctAnswer);

        currentWord.recordAttempt(isCorrect);

        totalAttempts++;

        if (isCorrect) {
            correctAttempts++;
            currentStreak++;
            maxStreak = Math.max(maxStreak, currentStreak);

            int points = 10;

            if (currentStreak >= 3) {
                points += currentStreak * 5;
            }

            currentScore += points;

            addExperience(points / 2);

            fireGameEvent(new ScoreChangedEvent(this, currentScore));
            fireGameEvent(new GameEvent(this, "CORRECT_ANSWER", points));

            if (currentWord.getSuccessRate() > 80 && currentWord.getAttempts() >= 3) {
                masteredWords.add(currentWord);
            }

            updateCategoryProgress(currentWord.getCategory());
        } else {
            currentStreak = 0;
            fireGameEvent(new GameEvent(this, "WRONG_ANSWER", correctAnswer));
        }

        return isCorrect;
    }

    private void addExperience(int exp) {
        experience += exp;
        while (experience >= level * 100) {
            experience -= level * 100;
            level++;
            fireGameEvent(new GameEvent(this, "LEVEL_UP", level));
        }
    }

    private void updateCategoryProgress(String category) {
        if (!"Все".equals(category)) {
            int progress = categoryProgress.getOrDefault(category, 0);
            progress += 5;
            categoryProgress.put(category, Math.min(progress, 100));
        }
    }

    public void nextWord() {
        if (currentWordIndex < currentSessionWords.size() - 1) {
            currentWordIndex++;
        }
    }

    public boolean isSessionComplete() {
        return currentSessionWords.isEmpty() || currentWordIndex >= currentSessionWords.size();
    }

    public void endSession() {
        fireGameEvent(new GameEvent(this, "SESSION_COMPLETED", currentScore));
    }

    public String getHint() {
        Word currentWord = getCurrentWord();
        if (currentWord == null) return "";

        String answer = getCorrectAnswer();
        if (answer.length() <= 2) {
            return answer;
        }

        return answer.charAt(0) + "..." + answer.charAt(answer.length() - 1);
    }

    public int getCurrentScore() { return currentScore; }
    public int getTotalWords() {
        return wordsByCategory.values().stream().mapToInt(List::size).sum();
    }
    public int getSessionWordsCount() { return currentSessionWords.size(); }
    public int getCurrentWordNumber() { return currentWordIndex + 1; }
    public int getTotalAttempts() { return totalAttempts; }
    public int getCorrectAttempts() { return correctAttempts; }
    public double getSuccessPercentage() {
        return totalAttempts > 0 ? (double) correctAttempts / totalAttempts * 100 : 0;
    }
    public int getCurrentStreak() { return currentStreak; }
    public int getMaxStreak() { return maxStreak; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getExperienceForNextLevel() { return level * 100; }
    public int getMasteredWordsCount() { return masteredWords.size(); }
    public String getCurrentMode() { return currentMode; }
    public String getCurrentCategory() { return currentCategory; }
    public String getCurrentGameMode() { return currentGameMode; }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>(wordsByCategory.keySet());
        categories.add(0, "Все");
        return categories;
    }

    public List<String> getModes() {
        return Arrays.asList("en-ru", "ru-en", "mixed");
    }

    public List<String> getGameModes() {
        return Arrays.asList("standard", "review", "timed", "challenge");
    }

    public String getModeDescription(String mode) {
        switch (mode) {
            case "en-ru": return "Английский → Русский";
            case "ru-en": return "Русский → Английский";
            case "mixed": return "Смешанный";
            case "standard": return "Стандартный";
            case "review": return "Повторение";
            case "timed": return "На время";
            case "challenge": return "Вызов";
            default: return mode;
        }
    }

    public Map<String, Integer> getCategoryProgress() {
        return new HashMap<>(categoryProgress);
    }

    public List<Word> getWordsForReview() {
        return wordsByCategory.values().stream()
                .flatMap(List::stream)
                .filter(Word::needsReview)
                .collect(Collectors.toList());
    }

    public void updateTimer() {
        if ("timed".equals(currentGameMode) && timeLeft > 0) {
            timeLeft--;
            fireGameEvent(new GameEvent(this, "TIMER_TICK", timeLeft));

            if (timeLeft <= 0) {
                fireGameEvent(new GameEvent(this, "TIME_UP", null));
            }
        }
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public boolean isTimeUp() {
        return "timed".equals(currentGameMode) && timeLeft <= 0;
    }
}