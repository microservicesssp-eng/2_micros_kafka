package ru.demo.consumer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaMessageRepository extends JpaRepository<KafkaMessage, Long> {
    List<KafkaMessage> findTop100ByOrderByIdDesc();
}
