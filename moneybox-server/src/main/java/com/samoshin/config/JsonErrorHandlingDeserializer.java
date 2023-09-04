package com.samoshin.config;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class JsonErrorHandlingDeserializer<T> implements Deserializer<T> {

    private final ErrorHandlingDeserializer<T> errorHandlingDeserializer;

    public JsonErrorHandlingDeserializer(Class<T> targetType) {
        JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<>(targetType);
        jsonDeserializer.addTrustedPackages("*");
        this.errorHandlingDeserializer = new ErrorHandlingDeserializer<>(jsonDeserializer);
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        return errorHandlingDeserializer.deserialize(topic, data);
    }
}