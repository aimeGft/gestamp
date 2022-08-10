package com.gestamp.model;

import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import static com.gestamp.streamingjob.StreamingJob.ERROR_EVENT_OUT;

public class EventOutDeserializationSchema extends AbstractDeserializationSchema<EventOut> {
    private static final long serialVersionUID = -1699854177598621044L;
    private final ObjectMapper mapper = new ObjectMapper();

    public EventOutDeserializationSchema() {
    }

    public EventOut deserialize(byte[] message) {
        EventOut eventOut;
        try {
            eventOut = this.mapper.readValue(message, EventOut.class);
        } catch (Exception ex) {
            //flag as wrong event
            //TODO: use flag to send wrong messages to error queue
            EventOut errorObjectNode = new EventOut();
            errorObjectNode.setSignalName(ERROR_EVENT_OUT);
            eventOut = errorObjectNode;
        }
        return eventOut;
    }
}
