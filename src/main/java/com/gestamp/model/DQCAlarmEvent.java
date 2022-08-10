package com.gestamp.model;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class DQCAlarmEvent {
    @JsonProperty("Description")
    private String description = "Process Alarm";
    @JsonProperty("TraceabilityCode")
    private String traceabilityCode;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Working")
    private String working;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("StartTime")
    private Timestamp startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("EndTime")
    private Timestamp endTime;
    @JsonProperty("Feature")
    private String feature;
    @JsonProperty("Asset")
    private String asset;
    @JsonProperty("Value")
    private String value;
    @JsonProperty("Limit")
    private String limit = "";
    @JsonProperty("Deviation")
    private String deviation;


    public DQCAlarmEvent() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTraceabilityCode() {
        return traceabilityCode;
    }

    public void setTraceabilityCode(String traceabilityCode) {
        this.traceabilityCode = traceabilityCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorking() {
        return working;
    }

    public void setWorking(String working) {
        this.working = working;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getDeviation() {
        return deviation;
    }

    public void setDeviation(String deviation) {
        this.deviation = deviation;
    }

    @Override
    public String toString() {
        return "DQCAlarmEvent{" +
                "description='" + description + '\'' +
                ", traceabilityCode='" + traceabilityCode + '\'' +
                ", status='" + status + '\'' +
                ", working='" + working + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", feature='" + feature + '\'' +
                ", asset='" + asset + '\'' +
                ", value='" + value + '\'' +
                ", limit='" + limit + '\'' +
                ", deviation='" + deviation + '\'' +
                '}';
    }
}
