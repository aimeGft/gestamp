package com.gestamp.maps;

import com.gestamp.model.DQCAlarmEvent;
import com.gestamp.model.EventOut;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class DQCAlarmMapTest {

    @Test
    public void testDefaulDQCAlarmMap() throws Exception {

        DQCAlarmMap dqcAlarmMap = new DQCAlarmMap();
        ObjectMapper objectMapper = new ObjectMapper();
        EventOut message = objectMapper.readValue(new File("src/test/resources/messages/DQCAlarm/DQCAlarmMessages1.json"), EventOut.class);

        // run mapping for messages
        DQCAlarmEvent dqcAlarmEvent = dqcAlarmMap.map(message);
        // check results
        Assert.assertEquals("Process Alarm", dqcAlarmEvent.getDescription());
        Assert.assertEquals("WR01", dqcAlarmEvent.getAsset());
        Assert.assertEquals("120", dqcAlarmEvent.getDeviation());
        Assert.assertEquals("9990120101123320", dqcAlarmEvent.getStatus());
        Assert.assertEquals("8100", dqcAlarmEvent.getFeature());
        Assert.assertEquals("59990120101123320", dqcAlarmEvent.getTraceabilityCode());
        Assert.assertEquals("", dqcAlarmEvent.getLimit());
        Assert.assertEquals("1", dqcAlarmEvent.getWorking());
        Assert.assertEquals("34", dqcAlarmEvent.getValue());
        Assert.assertEquals(1, message.getTimeStamp().compareTo(dqcAlarmEvent.getStartTime()));
        Assert.assertEquals(1, message.getTimeStamp().compareTo(dqcAlarmEvent.getEndTime()));
    }


}

