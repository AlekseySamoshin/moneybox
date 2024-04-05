package com.samoshin.config

import org.apache.kafka.common.serialization.Deserializer
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer

class JsonErrorHandlingDeserializer<T>(targetType: Class<T>?) : Deserializer<T> {
    private val errorHandlingDeserializer: ErrorHandlingDeserializer<T>

    init {
        val jsonDeserializer = JsonDeserializer(targetType)
        jsonDeserializer.addTrustedPackages("*")
        this.errorHandlingDeserializer = ErrorHandlingDeserializer(jsonDeserializer)
    }

    override fun deserialize(topic: String, data: ByteArray): T {
        return errorHandlingDeserializer.deserialize(topic, data)
    }
}