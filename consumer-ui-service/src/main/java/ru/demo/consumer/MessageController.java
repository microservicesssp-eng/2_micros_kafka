package ru.demo.consumer;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final KafkaMessageRepository repository;

    public MessageController(KafkaMessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public MessagesResponse getMessages() {
        long totalCount = repository.count();
        List<KafkaMessage> latestFirst = repository.findTop100ByOrderByIdDesc();
        Collections.reverse(latestFirst);
        List<MessageResponse> messages = latestFirst.stream()
                .map(MessageResponse::from)
                .toList();
        return new MessagesResponse(totalCount, messages);
    }

    public record MessagesResponse(long totalCount, List<MessageResponse> messages) {
    }
}
