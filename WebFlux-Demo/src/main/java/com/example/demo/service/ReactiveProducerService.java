package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReactiveProducerService {

    private final Logger log = LoggerFactory.getLogger(ReactiveProducerService.class);
    private final ReactiveKafkaProducerTemplate<String, ProductDto> reactiveKafkaProducerTemplate;

    @Value(value = "${FAKE_PRODUCER_DTO_TOPIC}")
    private String topic;

    public ReactiveProducerService(ReactiveKafkaProducerTemplate<String, ProductDto> reactiveKafkaProducerTemplate) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
    }

    public void send(ProductDto productDto) {
        log.info("send to topic={}, {}={},", topic, ProductDto.class.getSimpleName(), productDto);
        reactiveKafkaProducerTemplate.send(topic, productDto)
                .doOnSuccess(senderResult -> log.info("sent {} offset : {}", productDto, senderResult.recordMetadata().offset()))
                .subscribe();
    }
}
