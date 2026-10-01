package ru.demo.producer;

import java.security.SecureRandom;
import java.util.HexFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RandomStringPublisher {
    private static final Logger logger = LoggerFactory.getLogger(RandomStringPublisher.class);
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public RandomStringPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Scheduled(
            fixedRateString = "${app.producer.interval-ms:15000}",
            initialDelayString = "${app.producer.initial-delay-ms:0}")
    public void publishRandomString() {
        byte[] randomBytes = new byte[12];
        RANDOM.nextBytes(randomBytes);
        String value = HEX_FORMAT.formatHex(randomBytes);

        kafkaTemplate.send(topic, value).whenComplete((result, error) -> {
            if (error != null) {
                logger.error("Не удалось отправить строку в Kafka topic '{}'", topic, error);
                return;
            }
            logger.info("Отправлена строка в Kafka topic '{}': {}", topic, value);
        });
    }
}
