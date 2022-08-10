package com.gestamp.model;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DQCAlarmSerializationSchema implements SerializationSchema<DQCAlarmEvent> {

    Logger logger = LoggerFactory.getLogger(DQCAlarmSerializationSchema.class);
    ObjectMapper objectMapper;

    @Override
    public byte[] serialize(DQCAlarmEvent element) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.writeValueAsString(element).getBytes();
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse object: %s. Error: %s", element, ExceptionUtils.getStackTrace(e));
        }
        return new byte[0];
    }
}
