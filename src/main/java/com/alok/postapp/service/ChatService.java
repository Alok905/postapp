package com.alok.postapp.service;

import com.alok.postapp.dto.chat.ChatRequest;
import com.alok.postapp.dto.chat.ChatResponse;

public interface ChatService {
    public void storeFaqInDB();
    public ChatResponse respondToFaqsManual(ChatRequest chatRequest);
    public ChatResponse respondToFaqs(ChatRequest chatRequest);
    public ChatResponse aiChat(ChatRequest chatRequest);
}
