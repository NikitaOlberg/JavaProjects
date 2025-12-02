package com.englishtutor.controller;

import com.englishtutor.model.LanguageModel;
import com.englishtutor.events.*;
import com.englishtutor.view.SimpleFrame;

import javax.swing.*;

public class TutorController {
    private LanguageModel model;
    private SimpleFrame view;

    public TutorController(LanguageModel model) {
        this.model = model;
    }

    public void setView(SimpleFrame view) {
        this.view = view;
    }

    public void startNewSession(int wordCount, String mode) {
        model.startNewSession(wordCount, mode);
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
                view.showWrongAnswer(model.getCorrectAnswer());
            }
            view.updateUI();
        }

        // Проверяем завершение сессии
        if (model.isSessionComplete()) {
            endSession();
        }
    }

    public void nextWord() {
        model.nextWord();
        if (view != null) {
            if (!model.isSessionComplete()) {
                view.updateUI();
            } else {
                endSession();
            }
        }
    }

    private void endSession() {
        if (view != null) {
            view.showSessionComplete();
        }
    }

    public void showHint() {
        String hint = model.getHint();
        if (view != null) {
            view.showHint(hint);
        }
    }

    public LanguageModel getModel() {
        return model;
    }

    public String getStatistics() {
        return String.format("Очки: %d | Правильно: %d/%d | Серия: %d",
                model.getCurrentScore(),
                model.getCorrectAttempts(),
                model.getTotalAttempts(),
                model.getCurrentStreak());
    }
}