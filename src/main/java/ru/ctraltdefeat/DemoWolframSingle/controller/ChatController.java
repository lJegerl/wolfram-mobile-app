package ru.ctraltdefeat.DemoWolframSingle.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ctraltdefeat.DemoWolframSingle.domain.JwtAuthentication;
import ru.ctraltdefeat.DemoWolframSingle.domain.Message;
import ru.ctraltdefeat.DemoWolframSingle.domain.MessageRequest;
import ru.ctraltdefeat.DemoWolframSingle.service.AuthService;
import ru.ctraltdefeat.DemoWolframSingle.service.MessageService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Api(description = "Controllers for chat")
public class ChatController {
    @Autowired
    private final AuthService authService;
    @Autowired
    private final MessageService messageService;
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/messages")
    @ApiOperation("Get messages")
    public ResponseEntity<List<Message>> getMessages(@RequestParam(defaultValue = "10") int limit) {
        List<Message> messages = messageService.getLastMessages(limit);
        return ResponseEntity.ok(messages);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/messages")
    @ApiOperation("Create message")
    public ResponseEntity<Message> createMessage(@RequestBody MessageRequest messageRequest) {
        JwtAuthentication authentication = authService.getAuthInfo();
        Message message = Message.builder()
                .content(messageRequest.getContent())
                .login(authentication.getUserName())
                .timestamp(LocalDateTime.now())
                .build();
        Message createdMessage = messageService.createMessage(message);
        messagingTemplate.convertAndSend("/topic/messages", createdMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

}
