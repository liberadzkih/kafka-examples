package pl.hubert.kafkaexample2;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

public class KafkaExample2Application {

    public static void main(String[] args) {
        consumeAndProcessMessages();
    }

    public static void consumeAndProcessMessages() {
        Properties props = getConfig();
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> source = streamsBuilder.stream("sentences");
        KTable<String, Long> wordCount = source
            .flatMapValues(v -> Arrays.asList(v.toLowerCase().split(" ")))
            .groupBy((key, value) -> value)
            .count();

        wordCount.toStream().foreach((k, v) -> System.out.println(k + ": " + v));
        wordCount.toStream().to("word-count", Produced.with(Serdes.String(), Serdes.Long()));

        final KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), props);
        kafkaStreams.start();

        //attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

    private static Properties getConfig() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-example2-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
        return properties;
    }

}
