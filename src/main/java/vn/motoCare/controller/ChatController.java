package vn.motoCare.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.motoCare.service.ChatService;
import vn.motoCare.service.dto.ChatRequest;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest req) throws IdInvalidException {
        return chatService.chat(req);
    }
}
