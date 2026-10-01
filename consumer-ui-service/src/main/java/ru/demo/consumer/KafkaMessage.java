package ru.demo.consumer;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kafka_messages")
public class KafkaMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    protected KafkaMessage() {
    }

    public KafkaMessage(String content, Instant receivedAt) {
        this.content = content;
        this.receivedAt = receivedAt;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }
}
