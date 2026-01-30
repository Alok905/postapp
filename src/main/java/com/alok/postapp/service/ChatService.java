package com.alok.postapp.service;

import com.alok.postapp.dto.chat.ChatRequest;
import com.alok.postapp.dto.chat.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class ChatService {
    private final ChatClient chatClient;
    private final VectorStore faqVectorStore;
    private final ChatMemory chatMemory;


    @Value("classpath:static/postapp.pdf")
    private Resource postappPdfResource;

    public ChatService(ChatClient chatClient, @Qualifier("faq") VectorStore faqVectorStore, ChatMemory chatMemory) {
//    public ChatService(ChatClient chatClient, VectorStore faqVectorStore, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.faqVectorStore = faqVectorStore;
        this.chatMemory = chatMemory;
    }

    public void storeFaqInDB() {
        PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(postappPdfResource);
        List<Document> pages = pdfDocumentReader.get();

        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
                .withChunkSize(10)
                .build();
        List<Document> chunks = tokenTextSplitter.transform(pages);

        faqVectorStore.add(chunks); // it'll do the embedding and store

        /// /// just I was trying the embeddings
//        EmbeddingOptions embeddingOptions = OpenAiEmbeddingOptions.builder()
//                .user("user1")
//                        .build();
//        List<float[]> embeds = embeddingModel.embed(chunks, embeddingOptions, new TokenCountBatchingStrategy());
//        System.out.println("embeds: " + embeds + " ####################################################################");
//        for (float[] embed : embeds) {
//            System.out.println(Arrays.toString(embed));
//        }
//        System.out.println("###########################################################################################");
    }

    // manual
    public ChatResponse respondToFaqsManual(ChatRequest chatRequest) {
        String userQuestion = chatRequest.message();
        List<Document> documents = faqVectorStore.similaritySearch(SearchRequest.builder()
                .query(userQuestion)
                .similarityThreshold(0.6)
                .build());
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        String systemPromptTemplate = """
                You are a helpful AI assistant for a backend application.
                
                  You must answer the user's question using **ONLY** the information explicitly present in the CONTEXT below.
                  You are NOT allowed to use:
                  - prior knowledge
                  - general world knowledge
                  - assumptions
                  - logical guesses
                  - training data
                  - external facts
                
                  If the required information is not explicitly stated in the CONTEXT, you MUST respond exactly with:
                  "I don't have enough information to answer that based on the available FAQs."
                
                  Rules:
                  1. Use only the CONTEXT provided below as your source of truth.
                  2. Do NOT add, infer, assume, or extrapolate any information.
                  3. Do NOT fill gaps with general knowledge.
                  4. Do NOT hallucinate or invent details.
                  5. If the CONTEXT is partially relevant, answer only the part that is explicitly supported.
                  6. Do NOT mention the words "context", "documents", "vector store", "knowledge base", or "retrieval".
                  7. Keep the answer short, precise, and user-friendly.
                  8. If the answer cannot be fully supported by the CONTEXT, use the fallback response verbatim.
                
                  CONTEXT:
                  {context}
                
                  User Question:
                  {question}
                
                """;
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(systemPromptTemplate)
                .variables(Map.of(
                        "context", context,
                        "question", userQuestion
                ))
                .build();

        String systemPromptFinal = promptTemplate.render();
        if (!documents.isEmpty())
            System.out.println("################# documents size: " + documents.size() + " #######################");


        String response = chatClient.prompt()
                .user(userQuestion)
                .system(systemPromptFinal)
                .call()
                .content();
        return new ChatResponse(response);
    }

    // using advisors
    public ChatResponse respondToFaqs(ChatRequest chatRequest) {
        String userId = chatRequest.userId();
        String userQuestion = chatRequest.message();


        String systemPrompt = """
                RULES:
                    - Always greet the user with a good note.
                    - Always write in bullet points, don't write in a single paragraph.
                    - stay humble, and make the user happy.
                """;
        String response = chatClient.prompt()
                .user(userQuestion)
                .system(systemPrompt)
                .advisors(
                                MessageChatMemoryAdvisor.builder(chatMemory)
                                        .conversationId(userId)
                                                .build(),

                        QuestionAnswerAdvisor.builder(faqVectorStore)
                                .searchRequest(
                                        SearchRequest.builder()
                                                .similarityThreshold(0.6)
                                                .topK(6)
                                                .build()
                                )
                                .build()
                )
                .call()
                .content();

        System.out.println("################# chatMemoryData START #####################");
        System.out.println(chatMemory instanceof MessageWindowChatMemory);
        chatMemory.get(userId).forEach(mes -> System.out.println(mes + "\n-----------------------"));
        System.out.println("################# chatMemoryData END #####################\n");
        return new ChatResponse(response);
    }

}
