package com.gestamp.model;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class EventOut {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("TimeStamp")
    private Date timeStamp;

    @JsonProperty("SignalName")
    private String signalName;

    @JsonProperty("Value")
    private Object value;

    @JsonProperty("Line")
    private String line;

    @JsonProperty("Cell")
    private String cell;

    @JsonProperty("Asset")
    private String asset;

    @JsonProperty("SubAsset")
    private String subAsset;

    @JsonProperty("Operation")
    private String operation;

    @JsonProperty("ProcessType")
    private String processType;

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getSubAsset() {
        return subAsset;
    }

    public void setSubAsset(String subAsset) {
        this.subAsset = subAsset;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }
}
