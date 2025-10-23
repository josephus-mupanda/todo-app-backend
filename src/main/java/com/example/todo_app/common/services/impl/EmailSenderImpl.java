//package com.example.todo_app.common.services.impl;
//
//import com.example.todo_app.common.services.EmailSenderService;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailSenderImpl implements EmailSenderService {
//    @Autowired
//    private JavaMailSender mailSender;
//    @Value("${app.mail.from}")
//    private String fromEmail;
//    @Value("${app.frontend-base-url}")
//    private String appUrl;
//
//    public void sendHtmlEmail(String toEmail, String subject, String body) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            helper.setFrom(fromEmail);
//            helper.setTo(toEmail);
//            helper.setSubject(subject);
//            // Create professional HTML email
//            String htmlContent = createProfessionalEmailTemplate(subject, body);
//            helper.setText(htmlContent, true); // true = HTML content
//
//            mailSender.send(message);
//        } catch (Exception ex) {
//            throw new RuntimeException("Failed to send email to " + toEmail, ex);
//        }
//    }
//    private String createProfessionalEmailTemplate(String title, String content) {
//        return """
//        <!DOCTYPE html>
//        <html lang="en">
//        <head>
//            <meta charset="UTF-8">
//            <meta name="viewport" content="width=device-width, initial-scale=1.0">
//            <title>%s</title>
//            <style>
//                body {
//                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
//                    line-height: 1.6;
//                    color: #333;
//                    margin: 0;
//                    padding: 0;
//                    background-color: #f6f9fc;
//                }
//                .email-container {
//                    max-width: 600px;
//                    margin: 0 auto;
//                    background: #ffffff;
//                    border-radius: 8px;
//                    overflow: hidden;
//                    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
//                }
//                .email-header {
//                    background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
//                    color: white;
//                    padding: 30px 20px;
//                    text-align: center;
//                }
//                .email-header h1 {
//                    margin: 0;
//                    font-size: 24px;
//                    font-weight: 600;
//                }
//                .email-body {
//                    padding: 40px 30px;
//                }
//                .code-container {
//                    background: #f8f9fa;
//                    border: 2px dashed #dee2e6;
//                    border-radius: 8px;
//                    padding: 20px;
//                    text-align: center;
//                    margin: 25px 0;
//                }
//                .verification-code {
//                    font-size: 32px;
//                    font-weight: bold;
//                    color: #495057;
//                    letter-spacing: 8px;
//                    font-family: 'Courier New', monospace;
//                }
//                .button {
//                    display: inline-block;
//                    background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
//                    color: white;
//                    padding: 14px 28px;
//                    text-decoration: none;
//                    border-radius: 6px;
//                    font-weight: 600;
//                    margin: 20px 0;
//                }
//                .footer {
//                    text-align: center;
//                    padding: 20px;
//                    background: #f8f9fa;
//                    color: #6c757d;
//                    font-size: 12px;
//                }
//                .security-note {
//                    background: #fff3cd;
//                    border: 1px solid #ffeaa7;
//                    border-radius: 6px;
//                    padding: 15px;
//                    margin: 20px 0;
//                    font-size: 14px;
//                }
//            </style>
//        </head>
//        <body>
//            <div class="email-container">
//                <div class="email-header">
//                    <h1>Todo App</h1>
//                </div>
//                <div class="email-body">
//                    <h2>%s</h2>
//                    <p>Hello,</p>
//                    %s
//                    <div class="security-note">
//                        <strong>Security Tip:</strong> Never share this code with anyone.
//                        Our team will never ask for your verification code.
//                    </div>
//                    <p>If you didn't request this, please ignore this email.</p>
//                    <p>Best regards,<br>The Todo App Team</p>
//                </div>
//                <div class="footer">
//                    <p>&copy; 2024 Todo App. All rights reserved.</p>
//                    <p>This is an automated message, please do not reply to this email.</p>
//                </div>
//            </div>
//        </body>
//        </html>
//        """.formatted(title, title, content);
//    }
//    @Override
//    public void sendVerificationEmail(String toEmail, String verificationCode) {
//        String subject = "Verify Your Email Address";
//        String content = """
//            <p>Thank you for registering with Todo App! To complete your registration,
//            please use the verification code below:</p>
//
//            <div class="code-container">
//                <div class="verification-code">%s</div>
//            </div>
//
//            <p>Enter this code in the verification page to activate your account.
//            This code will expire in 24 hours.</p>
//            """.formatted(verificationCode);
//
//        sendHtmlEmail(toEmail, subject, content);
//    }
//
//    @Override
//    public void sendPasswordResetEmail(String toEmail, String resetCode) {
//        String subject = "Reset Your Password";
//        String content = """
//            <p>We received a request to reset your password for your Todo App account.</p>
//
//            <div class="code-container">
//                <div class="verification-code">%s</div>
//            </div>
//
//            <p>Enter this code on the password reset page to create a new password.
//            This code will expire in 24 hours.</p>
//
//            <p>If you didn't request a password reset, please ignore this email
//            or contact support if you have concerns.</p>
//            """.formatted(resetCode);
//
//        sendHtmlEmail(toEmail, subject, content);
//    }
//    @Override
//    public void sendWelcomeEmail(String toEmail, String username) {
//        String subject = "Welcome to Todo App!";
//        String content = """
//            <p>Welcome to Todo App, <strong>%s</strong>! 🎉</p>
//
//            <p>Your account has been successfully verified and is now active.
//            You can start using all the features of our application.</p>
//
//            <p>Here's what you can do now:</p>
//            <ul>
//                <li>Create and manage your todo lists</li>
//                <li>Organize tasks by categories</li>
//                <li>Set due dates and reminders</li>
//                <li>Collaborate with team members</li>
//            </ul>
//
//            <a href="%s" class="button">Get Started</a>
//
//            <p>If you have any questions, feel free to reach out to our support team.</p>
//            """.formatted(username, appUrl);
//
//        sendHtmlEmail(toEmail, subject, content);
//    }
//
//}