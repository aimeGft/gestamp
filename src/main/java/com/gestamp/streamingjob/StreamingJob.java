package com.gestamp.streamingjob;

import com.gestamp.model.*;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.mongoflink.config.MongoOptions;
import org.mongoflink.sink.MongoSink;

import java.io.IOException;
import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public abstract class StreamingJob implements Streaming {

    // URLS
    protected static final String CHECKPOINT_ACTIVATED = "flink.checkpoint.activated";
    protected static final String CHECKPOINT_PATH = "flink.checkpoint.path";
    protected static final String CHECKPOINT_INTERVAL = "flink.checkpoint.interval";
    protected static final String PARALLELISM = "flink.parallelism";

    // KAFKA
    protected static final String KAFKA_BROKERS_URLS = "kafka.kafkaBrokersUrls";
    protected static final String KAFKA_CONSUMERS_GROUP = "kafka.consumersGroup";
    protected static final String KAFKA_OUTPUT_TOPIC = "kafka.output.topic";

    //MONGO
    protected static final String MONGO_DB = "mongo.db";
    protected static final String MONGO_TABLE = "mongo.table";
    protected static final String MONGO_USER = "mongo.user";
    protected static final String MONGO_PASS = "mongo.pass";
    protected static final String MONGO_PORT = "mongo.port";
    protected static final String MONGO_URL = "mongo.url";

    public static final String ERROR_EVENT_OUT = "ERROR";


    // Config
    protected final ParameterTool appConfig;
    protected String jobName;

    public StreamingJob(ParameterTool appConfig, String jobName) {
        this.appConfig = appConfig;
        this.jobName = jobName;
    }


    protected StreamExecutionEnvironment loadFlinkEnv() throws IOException {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        if (appConfig.has(PARALLELISM))
            env.setParallelism(appConfig.getInt(PARALLELISM));
        else
            //default parallelism is 1
            env.setParallelism(1);
        return addConfigAndCheckpointing(env);
    }

    protected StreamExecutionEnvironment addConfigAndCheckpointing(StreamExecutionEnvironment env) {
        env.getConfig().setGlobalJobParameters(this.appConfig);
        if (Boolean.parseBoolean(this.appConfig.getRequired(CHECKPOINT_ACTIVATED))) {
            env.enableCheckpointing(Long.parseLong(this.appConfig.getRequired(CHECKPOINT_INTERVAL)), CheckpointingMode.EXACTLY_ONCE);
            env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
            env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
            env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
            env.getCheckpointConfig().setCheckpointStorage(this.appConfig.getRequired(CHECKPOINT_PATH));
        }
        return env;
    }

    @Override
    public void init() throws Exception {
        final StreamExecutionEnvironment env = loadFlinkEnv();
        buildPipeline(env);
        env.execute("Executing Gestamp Mapping Tools: " + jobName);
    }

    protected abstract void buildPipeline(StreamExecutionEnvironment env);

    /**
     * Retrieves kafka connector producer to a specific topic for DQCAlarmEvent objects
     *
     * @param outputTopic kafka output topic where to write DQCAlarmEvent objects
     * @return kafkaSink connected to outputTopic
     */
    protected KafkaSink<DQCAlarmEvent> getDQCAlarmEventSinkProducer(String outputTopic) {
        Properties prodProps = new Properties();
        prodProps.put(ACKS_CONFIG, "all");
        prodProps.put(RETRIES_CONFIG, 3);
        prodProps.put(ENABLE_IDEMPOTENCE_CONFIG, true);
        prodProps.put(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

        return KafkaSink.<DQCAlarmEvent>builder()
                .setBootstrapServers(this.appConfig.getRequired(KAFKA_BROKERS_URLS))
                .setKafkaProducerConfig(prodProps)
                .setDeliverGuarantee(Boolean.parseBoolean(this.appConfig.getRequired(CHECKPOINT_ACTIVATED)) ? DeliveryGuarantee.AT_LEAST_ONCE : DeliveryGuarantee.NONE)
                .setRecordSerializer(
                        KafkaRecordSerializationSchema.builder()
                                .setTopic(outputTopic)
                                .setValueSerializationSchema(new DQCAlarmSerializationSchema())
                                .build())
                .build();
    }


    /**
     * Retrieves kafka connector consumer from a specific topic of EventOut
     *
     * @param kafkaInputTopic kafka input topic from where to read EventOut objects
     * @return kafka EventOut source from topic
     */
    protected KafkaSource<EventOut> getEventOutFlinkKafkaConsumer(String kafkaInputTopic) {

        EventOutDeserializationSchema  eventDeserializer = new EventOutDeserializationSchema();
        return KafkaSource
                .<EventOut>builder()
                .setBootstrapServers(this.appConfig.getRequired(KAFKA_BROKERS_URLS))
                .setGroupId(this.appConfig.getRequired(KAFKA_CONSUMERS_GROUP))
                .setTopics(kafkaInputTopic)
                .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(eventDeserializer))
                .setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
                .setStartingOffsets(OffsetsInitializer.latest())
                .build();

    }


    //FIXME: MongoSink is deprecated -> using connector from kafka to mongo directly instead

    /**
     * Retrieves Mongo connector producer of DQCAlarmEvent objects.
     *
     * @param db    mongo data base
     * @param table mongo table
     * @return Mongo connector for specific database and table of DQCAlarmEvent objects
     */
    protected MongoSink<DQCAlarmEvent> getDQCAlarmEventMongoProducer(String db, String table) {

        Properties properties = new Properties();
        properties.setProperty(MongoOptions.SINK_TRANSACTION_ENABLED, "false");
        properties.setProperty(MongoOptions.SINK_FLUSH_ON_CHECKPOINT, "false");
        properties.setProperty(MongoOptions.SINK_FLUSH_SIZE, String.valueOf(1_000L));
        properties.setProperty(MongoOptions.SINK_FLUSH_INTERVAL, String.valueOf(10_000L));

        String user = this.appConfig.get(MONGO_USER);
        String pass = this.appConfig.get(MONGO_PASS);
        String port = this.appConfig.get(MONGO_PORT);
        String url = this.appConfig.get(MONGO_URL);

        return new MongoSink<>(String.format("mongodb://%s:%s@%s:%s", user, pass, url, port), db, table,
                new DQCAlarmEventDocumentSerializer(), properties);
    }

}