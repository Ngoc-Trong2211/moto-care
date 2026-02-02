package vn.motoCare.service.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import vn.motoCare.service.ChatService;
import vn.motoCare.service.UserService;
import vn.motoCare.service.dto.ChatRequest;
import vn.motoCare.util.exception.IdInvalidException;

@Service
public class ChatServiceImpl implements ChatService{
    private final ChatClient chatClient;
    private final UserService userService;
    private final Map<String, String> excelData = new HashMap<>();

    public ChatServiceImpl(ChatClient.Builder chatBuilder, JdbcChatMemoryRepository chatMemoryRepository, UserService userService){
        this.userService = userService;
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
            .chatMemoryRepository(chatMemoryRepository)
            .maxMessages(20)
            .build();
        
        this.chatClient = chatBuilder
            .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
            .build();

        loadExcelData("Chat.xlsx");
    }

    private String excelFallback(String userMessage) {
        String key = userMessage.trim().toLowerCase();
        return excelData.getOrDefault(key, "Xin lỗi, mình chưa biết câu trả lời cho câu hỏi này.");
    }

    private void loadExcelData(String fileName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
            Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell questionCell = row.getCell(0);
                Cell answerCell = row.getCell(1);

                if (questionCell != null && answerCell != null) {
                    excelData.put(
                            questionCell.getStringCellValue().trim().toLowerCase(),
                            answerCell.getStringCellValue().trim()
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Không tìm thấy file Excel: " + fileName);
        }
    }

    @Override
    public String chat(ChatRequest req) {
        String excelAnswer = excelFallback(req.message());
        if (!excelAnswer.equals("Xin lỗi, mình chưa biết câu trả lời cho câu hỏi này.")) {
            return excelAnswer;
        }

        String conversationId;
        String email = AuthServiceImpl.getCurrentUserLogin().isPresent() ? AuthServiceImpl.getCurrentUserLogin().get() : null;
        if (email!=null && !email.equals("anonymousUser")){
            Long idUser = this.userService.findByEmail(email)
                    .orElseThrow(() -> new IdInvalidException("Người dùng không tồn tại!"))
                    .getId();
            conversationId = "conversation" + idUser;
        }else{
            conversationId = "conversation" + UUID.randomUUID();
        }

        String aiResponse = null;
        try {
            aiResponse = chatClient.prompt(getPrompt(req))
                    .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                    .call()
                    .content();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return aiResponse != null && !aiResponse.isBlank()
                ? aiResponse
                : "Xin lỗi, mình chưa biết câu trả lời cho câu hỏi này.";
    }

    private static Prompt getPrompt(ChatRequest req){
        SystemMessage systemMessage = new SystemMessage("""
            You are NT-Kara
            If you don't know name user, you must ask user their name
            If user writes in English, reply in English
            If user writes in Vietnamese, reply in Vietnamese
            If user ask how to register or login, write to path /login or /register
        """);
        UserMessage userMessage = new UserMessage(req.message());

        return new Prompt(systemMessage, userMessage);
    }
}
