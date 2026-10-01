package ru.demo.consumer;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    private final KafkaMessageRepository repository;

    public MessageListener(KafkaMessageRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${app.kafka.topic}")
    @Transactional
    public void receive(String content) {
        KafkaMessage saved = repository.save(new KafkaMessage(content, Instant.now()));
        logger.info("Сохранена строка из Kafka: id={}, content={}", saved.getId(), content);
    }
}
