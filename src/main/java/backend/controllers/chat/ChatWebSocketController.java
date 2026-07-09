package backend.controllers.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import backend.dtos.chat.ChatMessageRequest;
import backend.dtos.chat.ChatMessageResponse;
import backend.services.chat.ChatService;
import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin(value = "${frontend.url}")
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendPrivateMessage(@Payload ChatMessageRequest request, Authentication auth) {
        if (auth == null) {
            throw new RuntimeException("Unauthorized");
        }
        
        ChatMessageResponse response = chatService.sendMessage(auth, request);

        messagingTemplate.convertAndSend(
                "/topic/chat/rooms/" + response.getRoomId(),
                response
        );

        messagingTemplate.convertAndSendToUser(
                response.getReceiverEmail(),
                "/queue/messages",
                response
        );

        messagingTemplate.convertAndSendToUser(
                response.getSenderEmail(),
                "/queue/messages",
                response
        );
    }
}
