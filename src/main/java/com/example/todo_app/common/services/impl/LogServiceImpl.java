package com.example.todo_app.common.services.impl;

import com.example.todo_app.common.models.LogResult;
import com.example.todo_app.common.repositories.LogRepository;
import com.example.todo_app.common.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logAction (String action,String email){
        try {
            LogResult logResult = new LogResult();
            logResult.setUsername(email);
            logResult.setAction(action);
            logResult.setTimestamp(LocalDateTime.now());
            logRepository.save(logResult);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
