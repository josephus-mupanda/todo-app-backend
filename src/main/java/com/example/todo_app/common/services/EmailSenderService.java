package com.example.todo_app.common.services;
public interface EmailSenderService {
    void sendEmail(String toEmail, String subject, String body);
}
