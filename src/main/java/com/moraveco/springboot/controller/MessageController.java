package com.moraveco.springboot.controller;

import com.moraveco.springboot.entity.ChatMessage;
import com.moraveco.springboot.repository.MessageRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController // Handles REST endpoints
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;


    public MessageController(MessageRepository chatMessageRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // WebSocket message endpoint
    @MessageMapping("/chat/send")
    public void sendMessage(ChatMessage message) {
        String id = UUID.randomUUID().toString();
        String timestamp = LocalDateTime.now().toString();

        chatMessageRepository.insertMessage(
                id,
                message.getSenderUid(),
                message.getReceiverUid(),
                message.getContent(),
                timestamp
        );

        message.setId(id);
        message.setTimestamp(timestamp);

        // Send to specific user using Spring's user-specific messaging
        // This uses /user/{receiverUid}/queue/messages
        messagingTemplate.convertAndSendToUser(
                message.getReceiverUid(),
                "/queue/messages",
                message
        );

        // Also send confirmation back to sender
        messagingTemplate.convertAndSendToUser(
                message.getSenderUid(),
                "/queue/messages",
                message
        );
    }

    // REST endpoint to get all messages
    @GetMapping("/getAllMessages")
    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.getAllMessages(); // Uses @Query
    }

    @GetMapping("/getSecretMessages")
    public List<ChatMessage> getSecretMessages(@RequestParam String senderUid, @RequestParam String receiverUid) {
        return chatMessageRepository.getSecretMessages(senderUid, receiverUid); // Uses @Query
    }
}
