package com.example.todo_app.common.services;
public interface EmailSenderService {
//    void sendEmail(String toEmail, String subject, String body);
    void sendHtmlEmail(String toEmail, String subject, String body);
    void sendVerificationEmail(String toEmail, String verificationCode);
    void sendPasswordResetEmail(String toEmail, String resetCode);
    void sendWelcomeEmail(String toEmail, String username);
}
