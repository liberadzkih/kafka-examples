package pl.hubert.kafkaexample.listener;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {


    @KafkaListener(topics = "word-count", groupId = "groupdId", containerFactory = "kafkaListenerContainerFactory")
    void listener(ConsumerRecord<String, Long> record) {
        System.out.println(record.key() + " -> " + record.value());
    }
}
