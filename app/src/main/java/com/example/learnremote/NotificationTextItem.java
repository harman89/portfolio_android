package com.example.learnremote;

public class NotificationTextItem {
    private String date;
    private String text;
    public NotificationTextItem(String date, String text)
    {
        this.date=date;
        this.text=text;
    }
    public String getDate()
    {
        return date;
    }
    public String getNotificationText()
    {
        return text;
    }
}
