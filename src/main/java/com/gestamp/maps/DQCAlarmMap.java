package com.gestamp.maps;

import com.gestamp.model.DQCAlarmEvent;
import com.gestamp.model.EventOut;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

public class DQCAlarmMap implements MapFunction<EventOut, DQCAlarmEvent> {
    static Logger LOGGER = LoggerFactory.getLogger(DQCAlarmMap.class);
    @Override
    public DQCAlarmEvent map(EventOut inputEvent) {
        DQCAlarmEvent event = new DQCAlarmEvent();
        try{
            event.setStartTime(new Timestamp(inputEvent.getTimeStamp().getTime()));
            event.setEndTime(new Timestamp(inputEvent.getTimeStamp().getTime()));

            //take values
            if(inputEvent.getValue() != null) {
                String[] values = inputEvent.getValue().toString().split(",");
                event.setTraceabilityCode(values[0].equals("0") ? "" : values[0]);
                event.setStatus(values[0].equals("0") ? "" : values[0].substring(1));
                event.setAsset(values[1].equals("0") ? "" : values[1]);
                event.setWorking(values[2].equals("0") ? "" : values[2]);
                event.setFeature(values[3].equals("0") ? "" : values[3]);
                event.setValue(values[5].equals("0") ? "" : values[5].trim());
                event.setDeviation(values[6].equals("0") ? "" : values[6]);
            }

        }catch (Exception ex)
        {
            LOGGER.error(String.format("Invalid mapping for event %s . Exception: %s", inputEvent.toString(), ExceptionUtils.getStackTrace(ex)));
        }
        return event;
    }
}
