package com.gestamp.streamingjob.jobs;

import com.gestamp.maps.DQCAlarmMap;
import com.gestamp.model.DQCAlarmEvent;
import com.gestamp.model.EventOut;
import com.gestamp.streamingjob.StreamingJob;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SignalNameMappingJob extends StreamingJob {

    String kafkaInputTopic;
//    protected static final String SIGNAL_NAME = "SignalName";
    protected static final String SIGNAL_NAME_VALUE = "DQCAlarmOut";

    public SignalNameMappingJob(ParameterTool appConfig, String jobName, String inputTopic) {
        super(appConfig, jobName);
        kafkaInputTopic = inputTopic;
    }

    @Override
    protected void buildPipeline(StreamExecutionEnvironment env) {
        //sources
        final KafkaSource<EventOut> kakfaJsonConsumer = getEventOutFlinkKafkaConsumer(kafkaInputTopic);
        //Sinks
        final KafkaSink<DQCAlarmEvent> kafkaProducer = getDQCAlarmEventSinkProducer(this.appConfig.get(KAFKA_OUTPUT_TOPIC));
//        final MongoSink<DQCAlarmEvent> mongoProducer = getDQCAlarmEventMongoProducer(this.appConfig.get(MONGO_DB), this.appConfig.get(MONGO_TABLE));

        //input kafka stream
        final SingleOutputStreamOperator<EventOut> inputFilteredEvents = env.fromSource(kakfaJsonConsumer, WatermarkStrategy.noWatermarks(),"kafka" )
                //filter out wrong JSONs messages and take only those having SignalName = “DQCAlarmOut”
                .filter(event -> !event.getSignalName().equals(ERROR_EVENT_OUT)).filter(event -> event.getSignalName().equals(SIGNAL_NAME_VALUE));

        //map json to DQCAlarmEvent and send to kafka
        final SingleOutputStreamOperator<DQCAlarmEvent> dqcEvents =inputFilteredEvents.map(new DQCAlarmMap());

        //send to kafka
        dqcEvents.sinkTo(kafkaProducer);

        //Instead of using deprecated Mongo Sink, connection is done from kafka topic to Mongo database
        //send to mongo
//        dqcEvents.sinkTo(mongoProducer);


    }
}
