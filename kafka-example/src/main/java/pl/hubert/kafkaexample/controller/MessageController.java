package pl.hubert.kafkaexample.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    @Value("${kafka.producer.topic.name}")
    private String producerTopicName;

    private KafkaTemplate<String, String> kafkaTemplate;

    public MessageController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

//    @PostMapping
//    public void publish(@RequestBody MessageRequest request) {
//        kafkaTemplate.send(producerTopicName, request.getMessage());
//    }

    @PostMapping
    public void publish(@RequestBody String string) {
        kafkaTemplate.send(producerTopicName, string);
    }
}
