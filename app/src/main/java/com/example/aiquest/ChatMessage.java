package com.example.aiquest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMessage {
    private final String text;
    private final boolean isUser;
    private String time; // 채팅 시간 저장용

    public ChatMessage(String text, boolean isUser) {
        this.text = text;
        this.isUser = isUser;
        this.time = new SimpleDateFormat("a hh:mm", Locale.KOREA).format(new Date());
    }

    public String getText() {
        return text;
    }

    public boolean isUser() {
        return isUser;
    }
    public String getTime() {
        return time;
    }
}
