package com.englishtutor.model;

import com.englishtutor.events.GameEventListener;
import com.englishtutor.events.GameEvent;
import com.englishtutor.events.ScoreChangedEvent;
import java.util.*;
import java.util.stream.Collectors;

public class LanguageModel {
    private static final int BASE_POINTS_PER_CORRECT_ANSWER = 10;
    private static final int STREAK_BONUS_THRESHOLD = 3;
    private static final int STREAK_BONUS_MULTIPLIER = 5;
    private static final int EXPERIENCE_PER_POINT_DIVISOR = 2;
    private static final int MASTERY_SUCCESS_RATE_THRESHOLD = 80;
    private static final int MIN_ATTEMPTS_FOR_MASTERY = 3;
    private static final int PROGRESS_INCREMENT_PER_CORRECT = 5;
    private static final int MAX_PROGRESS_PERCENTAGE = 100;
    private static final String ALL_CATEGORIES = "Все";

    private final Map<String, List<Word>> wordsByCategory;
    private final Set<Word> masteredWords;
    private final Map<String, Integer> categoryProgress;
    private final List<GameEventListener> listeners;
    private final Random random;

    private List<Word> currentSessionWords;
    private int currentScore;
    private int currentWordIndex;
    private int totalAttempts;
    private int correctAttempts;
    private int maxStreak;
    private int currentStreak;
    private int level;
    private int experience;
    private int timeLeft;

    private String currentMode;
    private String currentCategory;
    private String currentGameMode;

    public LanguageModel() {
        this.wordsByCategory = new HashMap<>();
        this.currentSessionWords = new ArrayList<>();
        this.masteredWords = new HashSet<>();
        this.categoryProgress = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.random = new Random();

        resetSessionStatistics();
        initializeVocabulary();
        initializeCategoryProgress();
    }

    private void resetSessionStatistics() {
        currentScore = 0;
        currentWordIndex = 0;
        totalAttempts = 0;
        correctAttempts = 0;
        maxStreak = 0;
        currentStreak = 0;
        level = 1;
        experience = 0;
        timeLeft = 0;

        currentMode = "en-ru";
        currentCategory = ALL_CATEGORIES;
        currentGameMode = "standard";
    }

    private void initializeVocabulary() {
        VocabularyInitializer initializer = new VocabularyInitializer(wordsByCategory);
        initializer.initialize();
    }

    private static class VocabularyInitializer {
        private final Map<String, List<Word>> vocabularyMap;

        VocabularyInitializer(Map<String, List<Word>> vocabularyMap) {
            this.vocabularyMap = vocabularyMap;
        }

        void initialize() {
            addCategory("Основные", createBasicWords());
            addCategory("Еда", createFoodWords());
            addCategory("Дом", createHomeWords());
            addCategory("Семья", createFamilyWords());
            addCategory("Работа", createWorkWords());
            addCategory("Путешествия", createTravelWords());
        }

        private void addCategory(String category, List<Word> words) {
            vocabularyMap.put(category, words);
        }

        private List<Word> createBasicWords() {
            return Arrays.asList(
                    new Word("hello", "привет", "Основные"),
                    new Word("goodbye", "до свидания", "Основные"),
                    new Word("thank you", "спасибо", "Основные"),
                    new Word("please", "пожалуйста", "Основные"),
                    new Word("yes", "да", "Основные"),
                    new Word("no", "нет", "Основные"),
                    new Word("sorry", "извините", "Основные"),
                    new Word("good morning", "доброе утро", "Основные"),
                    new Word("good night", "спокойной ночи", "Основные")
            );
        }

        private List<Word> createFoodWords() {
            return Arrays.asList(
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
            );
        }

        private List<Word> createHomeWords() {
            return Arrays.asList(
                    new Word("house", "дом", "Дом"),
                    new Word("room", "комната", "Дом"),
                    new Word("kitchen", "кухня", "Дом"),
                    new Word("bathroom", "ванная", "Дом"),
                    new Word("window", "окно", "Дом"),
                    new Word("door", "дверь", "Дом"),
                    new Word("bed", "кровать", "Дом"),
                    new Word("furniture", "мебель", "Дом"),
                    new Word("apartment", "квартира", "Дом")
            );
        }

        private List<Word> createFamilyWords() {
            return Arrays.asList(
                    new Word("family", "семья", "Семья"),
                    new Word("mother", "мать", "Семья"),
                    new Word("father", "отец", "Семья"),
                    new Word("brother", "брат", "Семья"),
                    new Word("sister", "сестра", "Семья"),
                    new Word("friend", "друг", "Семья"),
                    new Word("grandmother", "бабушка", "Семья"),
                    new Word("grandfather", "дедушка", "Семья"),
                    new Word("cousin", "двоюродный брат/сестра", "Семья")
            );
        }

        private List<Word> createWorkWords() {
            return Arrays.asList(
                    new Word("work", "работа", "Работа"),
                    new Word("office", "офис", "Работа"),
                    new Word("meeting", "встреча", "Работа"),
                    new Word("computer", "компьютер", "Работа"),
                    new Word("phone", "телефон", "Работа"),
                    new Word("email", "электронная почта", "Работа"),
                    new Word("boss", "начальник", "Работа"),
                    new Word("colleague", "коллега", "Работа"),
                    new Word("salary", "зарплата", "Работа")
            );
        }

        private List<Word> createTravelWords() {
            return Arrays.asList(
                    new Word("travel", "путешествие", "Путешествия"),
                    new Word("airport", "аэропорт", "Путешествия"),
                    new Word("hotel", "отель", "Путешествия"),
                    new Word("passport", "паспорт", "Путешествия"),
                    new Word("ticket", "билет", "Путешествия"),
                    new Word("luggage", "багаж", "Путешествия"),
                    new Word("map", "карта", "Путешествия"),
                    new Word("tourist", "турист", "Путешествия")
            );
        }
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

        SessionPreparer preparer = new SessionPreparer(category, wordCount);
        currentSessionWords = preparer.prepareSession(gameMode);

        if (wordCount > 0 && wordCount < currentSessionWords.size()) {
            currentSessionWords = new ArrayList<>(currentSessionWords.subList(0, wordCount));
        }

        Collections.shuffle(currentSessionWords);

        resetSessionState();
        timeLeft = "timed".equals(gameMode) ? timeLimit : 0;

        fireGameEvent(new GameEvent(this, "SESSION_STARTED", null));
    }

    private class SessionPreparer {
        private final String category;
        private final int wordCount;

        SessionPreparer(String category, int wordCount) {
            this.category = category;
            this.wordCount = wordCount;
        }

        List<Word> prepareSession(String gameMode) {
            switch (gameMode) {
                case "standard":
                    return prepareStandardSession();
                case "review":
                    return prepareReviewSession();
                case "timed":
                    return prepareTimedSession();
                case "challenge":
                    return prepareChallengeSession();
                default:
                    return prepareStandardSession();
            }
        }

        private List<Word> prepareStandardSession() {
            return getAllWordsForCategory();
        }

        private List<Word> prepareReviewSession() {
            return getAllWordsForCategory().stream()
                    .filter(Word::needsReview)
                    .sorted(Comparator.comparing(Word::getSuccessRate))
                    .collect(Collectors.toList());
        }

        private List<Word> prepareTimedSession() {
            return prepareStandardSession();
        }

        private List<Word> prepareChallengeSession() {
            return new ArrayList<>(getAllWordsForCategory());
        }

        private List<Word> getAllWordsForCategory() {
            if (ALL_CATEGORIES.equals(category)) {
                return wordsByCategory.values().stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
            } else {
                return wordsByCategory.getOrDefault(category, new ArrayList<>());
            }
        }
    }

    private void resetSessionState() {
        currentWordIndex = 0;
        currentScore = 0;
        currentStreak = 0;
        totalAttempts = 0;
        correctAttempts = 0;
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
        return question.equals(word.getEnglish()) ? word.getRussian() : word.getEnglish();
    }

    public boolean checkAnswer(String userAnswer) {
        Word currentWord = getCurrentWord();
        if (currentWord == null) return false;

        String normalizedAnswer = userAnswer.trim().toLowerCase();
        String correctAnswer = getCorrectAnswer().toLowerCase();
        boolean isCorrect = normalizedAnswer.equals(correctAnswer);

        processAnswerResult(currentWord, isCorrect, correctAnswer);
        return isCorrect;
    }

    private void processAnswerResult(Word word, boolean isCorrect, String correctAnswer) {
        word.recordAttempt(isCorrect);
        totalAttempts++;

        if (isCorrect) {
            handleCorrectAnswer(word);
        } else {
            handleWrongAnswer(correctAnswer);
        }
    }

    private void handleCorrectAnswer(Word word) {
        correctAttempts++;
        currentStreak++;
        maxStreak = Math.max(maxStreak, currentStreak);

        int points = calculatePointsForCorrectAnswer();
        currentScore += points;

        addExperience(points / EXPERIENCE_PER_POINT_DIVISOR);

        fireGameEvent(new ScoreChangedEvent(this, currentScore));
        fireGameEvent(new GameEvent(this, "CORRECT_ANSWER", points));

        checkForWordMastery(word);
        updateCategoryProgress(word.getCategory());
    }

    private int calculatePointsForCorrectAnswer() {
        int points = BASE_POINTS_PER_CORRECT_ANSWER;

        if (currentStreak >= STREAK_BONUS_THRESHOLD) {
            points += currentStreak * STREAK_BONUS_MULTIPLIER;
        }

        return points;
    }

    private void checkForWordMastery(Word word) {
        boolean highSuccessRate = word.getSuccessRate() > MASTERY_SUCCESS_RATE_THRESHOLD;
        boolean enoughAttempts = word.getAttempts() >= MIN_ATTEMPTS_FOR_MASTERY;

        if (highSuccessRate && enoughAttempts) {
            masteredWords.add(word);
        }
    }

    private void handleWrongAnswer(String correctAnswer) {
        currentStreak = 0;
        fireGameEvent(new GameEvent(this, "WRONG_ANSWER", correctAnswer));
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
        if (!ALL_CATEGORIES.equals(category)) {
            int progress = categoryProgress.getOrDefault(category, 0);
            progress += PROGRESS_INCREMENT_PER_CORRECT;
            categoryProgress.put(category, Math.min(progress, MAX_PROGRESS_PERCENTAGE));
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

    // Геттеры остаются без изменений, кроме исправления опечаток
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
        categories.add(0, ALL_CATEGORIES);
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