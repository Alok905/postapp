package com.alok.postapp.controller;

import com.alok.postapp.dto.chat.ChatRequest;
import com.alok.postapp.dto.chat.ChatResponse;
import com.alok.postapp.service.ChatService;
import com.alok.postapp.service.impl.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
//import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class AiChatController {

    private final ChatService chatService;

    @PostMapping("/faq-store")
    public ResponseEntity<Void> storeFaq(@RequestBody ChatRequest chatRequest) {
        chatService.storeFaqInDB();
        return ResponseEntity.ok(null);
    }

    @PostMapping("/faq")
    public ResponseEntity<ChatResponse> askFaq(@RequestBody ChatRequest chatRequest) {
        ChatResponse chatResponse = chatService.respondToFaqs(chatRequest);
        return ResponseEntity.ok(chatResponse);
    }

    @PostMapping("/talk")
    public ResponseEntity<ChatResponse> talkWithAI(@RequestBody ChatRequest chatRequest) {
        ChatResponse chatResponse = chatService.aiChat(chatRequest);
        return ResponseEntity.ok(chatResponse);
    }
}
