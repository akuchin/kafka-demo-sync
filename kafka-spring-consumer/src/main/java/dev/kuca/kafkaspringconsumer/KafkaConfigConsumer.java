package dev.kuca.kafkaspringconsumer;

import dev.kuca.kafkaspring.msg.StockReply;
import dev.kuca.kafkaspring.msg.StockRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfigConsumer {

    @Value("${kafka.topic.request-topic}")
    private String requestTopic;

    @Value("${kafka.topic.reply-topic}")
    private String replyTopic;

    @Value("${kafka.groupId}")
    private String groupId;

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> consumerConfigsCons() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return props;
    }

    @Bean
    public Map<String, Object> producerConfigsCons() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

    @Bean
    public ConsumerFactory<String, StockRequest> requestConsumerFactory() {
        JsonDeserializer<StockRequest> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");


        return new DefaultKafkaConsumerFactory<>(consumerConfigsCons(), new StringDeserializer(), deserializer);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, StockRequest>> requestListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StockRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(requestConsumerFactory());
        factory.setReplyTemplate(replyTemplate());
        return factory;
    }

    @Bean
    public ProducerFactory<String, StockReply> replyProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigsCons());
    }

    @Bean
    public KafkaTemplate<String, StockReply> replyTemplate() {
        return new KafkaTemplate<>(replyProducerFactory());
    }
}
