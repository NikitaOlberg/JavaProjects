package com.englishtutor.events;

import java.util.EventObject;

public class GameEvent extends EventObject {
    private final String eventType;
    private final Object data;

    public GameEvent(Object source, String eventType, Object data) {
        super(source);
        this.eventType = eventType;
        this.data = data;
    }

    public String getEventType() {
        return eventType;
    }

    public Object getData() {
        return data;
    }
}