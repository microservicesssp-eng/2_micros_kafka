package ru.demo.consumer;

import java.time.Instant;

public record MessageResponse(Long id, String content, Instant receivedAt) {
    public static MessageResponse from(KafkaMessage message) {
        return new MessageResponse(message.getId(), message.getContent(), message.getReceivedAt());
    }
}
