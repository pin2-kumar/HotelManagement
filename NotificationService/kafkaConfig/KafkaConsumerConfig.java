package com.NotificationService.kafkaConfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.NotificationService.appConstants.AppConstants;
import com.NotificationService.dto.EmailRequest;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, EmailRequest> consumerFactory() {
        Map<String, Object> kafkaConfigProps = new HashMap<>();

        kafkaConfigProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.KAFKA_HOST);
        kafkaConfigProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaConfigProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // Create JsonDeserializer with trusted packages
        JsonDeserializer<EmailRequest> deserializer = new JsonDeserializer<>(EmailRequest.class);
        deserializer.addTrustedPackages("com.NotificationService.dto");

        return new DefaultKafkaConsumerFactory<>(
                kafkaConfigProps,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailRequest> kafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EmailRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
