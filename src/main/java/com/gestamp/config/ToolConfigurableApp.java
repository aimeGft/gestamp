package com.gestamp.config;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;

import java.util.Arrays;
import java.util.List;

public class ToolConfigurableApp {

    private static final List<String> ENVS = Arrays.asList("PRE", "PROD");

    /**
     * Process params
     *
     * @return configuration for instance of job
     */
    protected static Configuration getQoEAppConfiguration(String env) {
        Configuration configuration;
        if (ENVS.contains(env.toUpperCase())) {
            // for real environment
            configuration = GlobalConfiguration.loadConfiguration();
        } else {
            // for local and env-toy
            configuration = GlobalConfiguration.loadConfiguration("src/main/resources");
        }
        return configuration;
    }
}
