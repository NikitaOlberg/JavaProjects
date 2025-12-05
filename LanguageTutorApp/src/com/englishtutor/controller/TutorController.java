package com.englishtutor.controller;

import com.englishtutor.model.LanguageModel;
import com.englishtutor.view.ExtendedFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TutorController {
    private static final int TIMER_INTERVAL_MS = 1000;
    private static final int MIN_TIME_LEFT_TO_RESUME = 0;

    private LanguageModel model;
    private Timer gameTimer;
    private boolean isTimerActive;

    public TutorController(LanguageModel model) {
        this.model = model;
        this.isTimerActive = false;
    }

    public void startNewSession(String category, String mode, String gameMode, int wordCount, int timeLimit) {
        stopTimerIfActive();
        model.startNewSession(category, mode, gameMode, wordCount, timeLimit);

        if ("timed".equals(gameMode)) {
            initializeTimer();
        }
    }

    private void stopTimerIfActive() {
        if (isTimerActive && gameTimer != null) {
            gameTimer.stop();
            isTimerActive = false;
        }
    }

    private void initializeTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }

        gameTimer = new Timer(TIMER_INTERVAL_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.updateTimer();

                if (model.isTimeUp()) {
                    stopTimer();
                    model.endSession();
                }
            }
        });

        gameTimer.start();
        isTimerActive = true;
    }

    public void checkAnswer(String userAnswer) {
        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            return;
        }

        model.checkAnswer(userAnswer);

        if (model.isSessionComplete()) {
            model.endSession();
        }
    }

    public void nextWord() {
        model.nextWord();
        if (model.isSessionComplete()) {
            model.endSession();
        }
    }

    public void showHint() {
        String hint = model.getHint();
        if (!hint.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Подсказка: " + hint,
                    "Подсказка",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void pauseTimer() {
        if (isTimerActive && gameTimer != null) {
            gameTimer.stop();
            isTimerActive = false;
        }
    }

    public void resumeTimer() {
        if (!isTimerActive && gameTimer != null && model.getTimeLeft() > MIN_TIME_LEFT_TO_RESUME) {
            gameTimer.start();
            isTimerActive = true;
        }
    }

    private void stopTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
            isTimerActive = false;
        }
    }

    public LanguageModel getModel() {
        return model;
    }

    public boolean isTimerRunning() {
        return isTimerActive;
    }
}