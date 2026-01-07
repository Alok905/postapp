package com.alok.postapp.service.impl;

import com.alok.postapp.entity.Session;
import com.alok.postapp.entity.User;
import com.alok.postapp.repository.SessionRepository;
import com.alok.postapp.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public void generateNewSession(User user, String refreshToken) {
        int MAX_SESSION = 2;

        List<Session> sessions = sessionRepository.findByUser(user);

        if(sessions.size() >= MAX_SESSION) {
            Session leastRecentlyUsedSession = sessions.stream()
                    .min(Comparator.comparing(Session::getLastUsedAt))
                    .get();
            sessionRepository.delete(leastRecentlyUsedSession);
        }

        Session session = Session.builder()
                .refreshToken(refreshToken)
                .user(user)
                .build();

        sessionRepository.save(session);
    }
}
