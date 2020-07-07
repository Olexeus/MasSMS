package com.example.massms.models;

import java.util.Calendar;

public class Message{
    private String text;
    private Calendar date;

    public Message() {
        this.text = null;
        this.date = null;
    }

    public Message(String newText, Calendar newDate) {
        this.text = newText;
        this.date = newDate;
    }

    public boolean empty() {
        return text.isEmpty();
    }

}
