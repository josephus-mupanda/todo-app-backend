package com.example.todo_app.common.listeners;

import com.example.todo_app.common.services.LogService;
import com.example.todo_app.user.models.User;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserListener implements HttpSessionListener {
    @Autowired
    private LogService logService;

    public void logUserAction(User user, String action) {

        logService.logAction(action, user.getEmail());
    }
}
