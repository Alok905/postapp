package com.alok.postapp.service;

import com.alok.postapp.entity.User;

public interface SessionService {

    void generateNewSession(User user, String refreshToken);
}
