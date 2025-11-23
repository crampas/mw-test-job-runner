package org.example.service;

import org.example.jmeter.JMeterBatchJob;
import org.example.jmeter.JMeterTestJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {
    @Bean
    JMeterTestJob jmeterTestJob() {
        return new JMeterTestJob();
    }

    @Bean
    JMeterBatchJob jMeterBatchJob(JMeterTestJob testJob) {
        return new JMeterBatchJob(testJob);
    }
}
