package com.example.todo_app.common.interfaces;

import com.example.todo_app.common.domains.IamUserDetails;
public interface IamUserDetailsService {
    IamUserDetails findUserByUsername(String username);
}

