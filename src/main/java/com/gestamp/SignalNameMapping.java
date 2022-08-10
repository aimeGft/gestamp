package com.gestamp;

import com.gestamp.config.ToolConfigurableApp;
import com.gestamp.streamingjob.Streaming;
import com.gestamp.streamingjob.jobs.SignalNameMappingJob;
import org.apache.flink.api.java.utils.ParameterTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignalNameMapping extends ToolConfigurableApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignalNameMapping.class);

    public static void main(String[] args) throws Exception {

        if (args != null && args.length > 0) {
            //get input topic from args
            String kafkaInputTopic = args[0];
            //get environment
            String env =args[1];
            //load properties
            final ParameterTool appConfig = ParameterTool.fromMap(getQoEAppConfiguration(env).toMap());

            //create and run job
            Streaming job = new SignalNameMappingJob(appConfig, "Signal Name Mapping Job", kafkaInputTopic);
            job.init();
        } else {
            LOGGER.error("Not valid arguments. Input topic required");
            System.exit(1);
        }

    }
}
