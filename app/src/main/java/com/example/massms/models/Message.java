package com.example.massms.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public String getText() { return text; }

    public boolean empty() {
        return text.isEmpty();
    }

    @Override
    public String toString() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        return "Date: " + dateFormat.format(date.getTime()) +
               "\nMessage: " + text;
    }
}
