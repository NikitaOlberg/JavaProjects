package com.englishtutor.events;

public class ScoreChangedEvent extends GameEvent {
    private final int newScore;

    public ScoreChangedEvent(Object source, int newScore) {
        super(source, "SCORE_CHANGED", newScore);
        this.newScore = newScore;
    }

    public int getNewScore() {
        return newScore;
    }
}