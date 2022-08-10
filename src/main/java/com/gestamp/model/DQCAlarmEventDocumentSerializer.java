package com.gestamp.model;

import org.bson.Document;
import org.mongoflink.serde.DocumentSerializer;

public class DQCAlarmEventDocumentSerializer implements DocumentSerializer<DQCAlarmEvent> {

    @Override
    public Document serialize(DQCAlarmEvent event) {
        Document document = new Document();
        document.append("Description", event.getDescription());
        document.append("TraceabilityCode", event.getTraceabilityCode());
        document.append("Status", event.getStatus());
        document.append("Working", event.getWorking());
        document.append("StartTime", event.getStartTime());
        document.append("EndTime", event.getEndTime());
        document.append("Feature", event.getFeature());
        document.append("Asset", event.getAsset());
        document.append("value", event.getValue());
        document.append("Limit", event.getLimit());
        document.append("Deviation", event.getDeviation());
        return document;
    }
}
