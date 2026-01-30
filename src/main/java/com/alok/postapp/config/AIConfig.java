package com.alok.postapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class AIConfig {

    @Value("${vectorstore.schema-name}")
    private String vectorSchemaName;



    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    // VectorStore bean already present; I want to create multiple vector tables for multiple purpose.
    @Bean("faq")
    public VectorStore faqVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .schemaName(vectorSchemaName)
                .vectorTableName("faqs")
                .initializeSchema(true)
                .build();
    }

    @Bean("chat-history")
    public VectorStore chatVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .schemaName(vectorSchemaName)
                .vectorTableName("chats")
                .initializeSchema(true)
                .build();
    }

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(30)
                .build();
//        return MessageWindowChatMemory.builder()  // by default InMemoryChatMemoryRepository will be added here
//                .maxMessages(3)
//                .build();
    }
}
