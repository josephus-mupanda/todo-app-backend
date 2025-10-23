package com.example.todo_app.common.services.impl;

import com.example.todo_app.common.services.EmailSenderService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class EmailSenderImpl implements EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${app.mail.from}")
    private String fromEmail;
    @Value("${app.frontend-base-url}")
    private String appUrl;
    @Value("${app.primary.color}")
    private String primaryColor;
    @Value("${app.logo.path}")
    private String logoPath;
    @Value("${app.name}")
    private String appName;
    private String base64Logo;

    @PostConstruct
    public void init() {
        this.base64Logo = loadLogoAsBase64();
    }

    private String loadLogoAsBase64() {
        try {
            // Load logo from configured path in resources
            ClassPathResource logoResource = new ClassPathResource(logoPath);
            byte[] imageBytes = logoResource.getInputStream().readAllBytes();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            // Fallback to a simple colored SVG if logo fails to load
            return createFallbackLogo();
        }
    }
    private String createFallbackLogo() {
        // Create a simple colored checkmark SVG as fallback
        String color = primaryColor.startsWith("#") ? primaryColor.replace("#", "%23") : "%2301c3a7";
        String fallbackSvg = "<svg width=\"48\" height=\"48\" viewBox=\"0 0 48 48\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">" +
                "<rect width=\"48\" height=\"48\" rx=\"10\" fill=\"" + color + "\"/>" +
                "<path d=\"M15 24L20 29L33 16\" stroke=\"white\" stroke-width=\"3\" stroke-linecap=\"round\" stroke-linejoin=\"round\"/>" +
                "</svg>";
        return Base64.getEncoder().encodeToString(fallbackSvg.getBytes());
    }
    public void sendHtmlEmail(String toEmail, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            String htmlContent = createProfessionalEmailTemplate(subject, body);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to send email to " + toEmail, ex);
        }
    }
    private String createProfessionalEmailTemplate(String title, String content) {
        // Add # to the color if it doesn't have one
        String color = primaryColor.startsWith("#") ? primaryColor : "#" + primaryColor;
        return """
        <!DOCTYPE html>
        <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>%s</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        margin: 0;
                        padding: 0;
                        background-color: #f8fafc;
                    }
                    .email-container {
                        max-width: 600px;
                        margin: 0 auto;
                        background: #ffffff;
                        border-radius: 12px;
                        overflow: hidden;
                        box-shadow: 0 8px 24px rgba(1, 195, 167, 0.1);
                        border: 1px solid #e1e8ed;
                    }
                    .email-header {
                        background: linear-gradient(135deg, %s 0%%, #019876 100%%);
                        color: white;
                        padding: 30px 20px;
                        text-align: center;
                    }
                    .logo-container {
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        gap: 15px;
                        margin-bottom: 10px;
                    }
                    .logo {
                        height: 48px;
                        width: 48px;
                        border-radius: 10px;
                        object-fit: contain;
                    }
                    .email-header h1 {
                        margin: 0;
                        font-size: 28px;
                        font-weight: 700;
                        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                    }
                    .email-body {
                        padding: 40px 30px;
                    }
                    .code-container {
                        background: #f8fafc;
                        border: 2px dashed %s;
                        border-radius: 10px;
                        padding: 25px;
                        text-align: center;
                        margin: 30px 0;
                    }
                    .verification-code {
                        font-size: 36px;
                        font-weight: bold;
                        color: %s;
                        letter-spacing: 8px;
                        font-family: 'Courier New', monospace;
                        text-shadow: 0 1px 2px rgba(1, 195, 167, 0.2);
                    }
                    .button {
                        display: inline-block;
                        background: linear-gradient(135deg, %s 0%%, #019876 100%%);
                        color: white !important;
                        padding: 16px 32px;
                        text-decoration: none;
                        border-radius: 8px;
                        font-weight: 600;
                        margin: 25px 0;
                        box-shadow: 0 4px 12px rgba(1, 195, 167, 0.3);
                        transition: all 0.3s ease;
                        border: none;
                        cursor: pointer;
                    }
                    .button:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 6px 16px rgba(1, 195, 167, 0.4);
                    }
                    .button a {
                        color: white !important;
                        text-decoration: none;
                    }
                    .footer {
                        text-align: center;
                        padding: 25px;
                        background: #f8fafc;
                        color: #64748b;
                        font-size: 13px;
                        border-top: 1px solid #e1e8ed;
                    }
                    .security-note {
                        background: linear-gradient(135deg, #fff9e6 0%%, #fff3cd 100%%);
                        border-left: 4px solid %s;
                        border-radius: 8px;
                        padding: 20px;
                        margin: 25px 0;
                        font-size: 14px;
                    }
                    .brand-color {
                        color: %s;
                        font-weight: 600;
                    }
                    .feature-list {
                        background: #f8fafc;
                        border-radius: 8px;
                        padding: 20px;
                        margin: 20px 0;
                    }
                    .feature-list ul {
                        margin: 0;
                        padding-left: 20px;
                    }
                    .feature-list li {
                        margin-bottom: 8px;
                        color: #475569;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="email-header">
                        <div class="logo-container">
                            <img src="data:image/png;base64,%s" alt="%s Logo" class="logo">
                            <h1>%s</h1>
                        </div>
                    </div>
                    <div class="email-body">
                        <h2>%s</h2>
                        <p>Hello,</p>
                        %s
                        <div class="security-note">
                            <strong>Security Tip:</strong> Never share this code with anyone. 
                            Our team will never ask for your verification code.
                        </div>
                        <p>If you didn't request this, please ignore this email.</p>
                        <p>Best regards,<br><span class="brand-color">The %s Team</span></p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 %s. All rights reserved.</p>
                        <p>This is an automated message, please do not reply to this email.</p>
                    </div>
                </div>
            </body>
        </html>
        """.formatted(
                title,          // %s - title
                color,          // %s - header gradient
                color,          // %s - code container border
                color,          // %s - verification code color
                color,          // %s - button gradient
                color,          // %s - security note border
                color,          // %s - brand color
                base64Logo,     // %s - logo
                appName,        // %s - alt text for logo
                appName,        // %s - header h1
                title,          // %s - h2 title
                content,        // %s - content
                appName,        // %s - signature team name
                appName         // %s - footer copyright
        );
    }
    @Override
    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Verify Your Email Address";
        String content = """
            <p>Thank you for registering with <span class="brand-color">Todo App</span>! To complete your registration, 
            please use the verification code below:</p>
            
            <div class="code-container">
                <div class="verification-code">%s</div>
            </div>
            
            <p>Enter this code in the verification page to activate your account. 
            This code will expire in 24 hours.</p>
            """.formatted(verificationCode);

        sendHtmlEmail(toEmail, subject, content);
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetCode) {
        String subject = "Reset Your Password";
        String content = """
            <p>We received a request to reset your password for your <span class="brand-color">Todo App</span> account.</p>
            
            <div class="code-container">
                <div class="verification-code">%s</div>
            </div>
            
            <p>Enter this code on the password reset page to create a new password. 
            This code will expire in 24 hours.</p>
            
            <p>If you didn't request a password reset, please ignore this email 
            or contact support if you have concerns.</p>
            """.formatted(resetCode);

        sendHtmlEmail(toEmail, subject, content);
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String username) {
        String subject = "Welcome to Todo App!";
        String content = """
            <p>Welcome to <span class="brand-color">Todo App</span>, <strong>%s</strong>! 🎉</p>
            
            <p>Your account has been successfully verified and is now active. 
            You can start using all the features of our application.</p>
            
            <div class="feature-list">
                <p><strong>Here's what you can do now:</strong></p>
                <ul>
                    <li>Create and manage your todo lists</li>
                    <li>Organize tasks by categories and priorities</li>
                    <li>Set due dates and reminders</li>
                    <li>Collaborate with team members</li>
                    <li>Track your productivity</li>
                </ul>
            </div>
            
            <a href="%s" class="button">Get Started Now</a>
            
            <p>If you have any questions, feel free to reach out to our support team.</p>
            """.formatted(username, appUrl);

        sendHtmlEmail(toEmail, subject, content);
    }
}