package org.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jmeter.JMeterBatchJob;
import org.example.jmeter.JMeterTestJob;
import org.example.jmeter.JMeterTestParams;

@Slf4j
public class Main {
    @SneakyThrows
    static void main() {
        JMeterTestJob testJob = new JMeterTestJob();
        JMeterBatchJob batchJob = new JMeterBatchJob(testJob);

        testJob.submit(new JMeterTestParams().setMicroservice("test1"));
        testJob.submit(new JMeterTestParams().setMicroservice("test2"));
        batchJob.submit(null);


        log.info("JMeterTestRuns {}", testJob.getRuns());
        log.info("JMeterBatchRuns {}", batchJob.getRuns());

        // shutdown hook
        testJob.waitFor();
        batchJob.waitFor();

        log.info("EXIT");
    }
}
