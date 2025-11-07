package com.englishtutor.controller;

import com.englishtutor.model.LanguageModel;
import com.englishtutor.view.MainFrame;

public class TutorController {
    private final LanguageModel model;
    private MainFrame view;

    public TutorController(LanguageModel model) {
        this.model = model;
    }

    public void setView(MainFrame view) {
        this.view = view;
    }

    public void startNewSession() {
        model.startNewSession();
        if (view != null) {
            view.updateUI();
        }
    }

    public void checkAnswer(String userAnswer) {
        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            if (view != null) {
                view.showMessage("Введите ответ!", "Предупреждение");
            }
            return;
        }

        boolean isCorrect = model.checkAnswer(userAnswer);

        if (view != null) {
            if (isCorrect) {
                view.showCorrectAnswer();
            } else {
                view.showWrongAnswer(model.getCurrentWord().getTranslation());
            }
            view.updateUI();
        }
    }

    public void nextWord() {
        boolean hasNext = model.nextWord();
        if (view != null) {
            if (hasNext) {
                view.updateUI();
            } else {
                view.showSessionComplete();
            }
        }
    }

    public void showHint() {
        String hint = model.getHint();
        if (view != null) {
            view.showHint(hint);
        }
    }

    public String getStatistics() {
        return String.format(
                "Счет: %d | Правильно: %d/%d (%.1f%%) | Прогресс: %d%%",
                model.getCurrentScore(),
                model.getCorrectAttempts(),
                model.getTotalAttempts(),
                model.getSuccessPercentage(),
                model.getSessionProgress()
        );
    }

    public LanguageModel getModel() {
        return model;
    }

    public boolean isSessionComplete() {
        return model.isSessionComplete();
    }
}