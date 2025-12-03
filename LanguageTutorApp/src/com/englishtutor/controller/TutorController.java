package com.englishtutor.controller;

import com.englishtutor.model.LanguageModel;
import com.englishtutor.view.ExtendedFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TutorController {
    private LanguageModel model;
    private Timer gameTimer;
    private boolean timerRunning;

    public TutorController(LanguageModel model) {
        this.model = model;
        this.timerRunning = false;
    }

    public void startNewSession(String category, String mode, String gameMode, int wordCount, int timeLimit) {
        if (timerRunning && gameTimer != null) {
            gameTimer.stop();
            timerRunning = false;
        }

        model.startNewSession(category, mode, gameMode, wordCount, timeLimit);

        if ("timed".equals(gameMode)) {
            startTimer();
        }
    }

    private void startTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }

        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.updateTimer();

                if (model.isTimeUp()) {
                    gameTimer.stop();
                    timerRunning = false;
                    model.endSession();
                }
            }
        });

        gameTimer.start();
        timerRunning = true;
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
        if (timerRunning && gameTimer != null) {
            gameTimer.stop();
            timerRunning = false;
        }
    }

    public void resumeTimer() {
        if (!timerRunning && gameTimer != null && model.getTimeLeft() > 0) {
            gameTimer.start();
            timerRunning = true;
        }
    }

    public LanguageModel getModel() {
        return model;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }
}