package com.gestamp.model;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DQCAlarmDeserializationSchema extends AbstractDeserializationSchema<DQCAlarmEvent>  {
    private static final long serialVersionUID = -1699854177598621044L;
    private final ObjectMapper mapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(DQCAlarmDeserializationSchema.class);
    public DQCAlarmDeserializationSchema() {
    }

    public DQCAlarmEvent deserialize(byte[] message) {
        DQCAlarmEvent event;
        try{
            event = this.mapper.readValue(message, DQCAlarmEvent.class);
        }catch (Exception ex)
        {
            event =new DQCAlarmEvent();
            logger.error("Failed to parse object: %s. Error: %s", message, ExceptionUtils.getStackTrace(ex));
        }
        return event;

    }
    @Override
    public TypeInformation<DQCAlarmEvent> getProducedType() {
        return TypeInformation.of(DQCAlarmEvent.class);
    }
}
