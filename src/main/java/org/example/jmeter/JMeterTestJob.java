package org.example.jmeter;

import lombok.extern.slf4j.Slf4j;
import org.example.scheduler.Job;
import org.example.scheduler.RunEnvironment;
import org.example.scheduler.RunFailedException;

import java.io.IOException;

@Slf4j
public class JMeterTestJob extends Job<JMeterTestParams, JMeterTestData> {
    public JMeterTestJob() {
    }

    protected JMeterTestData execute(RunEnvironment env, JMeterTestParams params) throws IOException, InterruptedException {
        // starting process
        log.info("starting JMeter process for {}", params);
        ProcessBuilder processBuilder = new ProcessBuilder("sleep", "5");
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        log.info("JMeter process exited with code {}", exitCode);

        JMeterTestData data = new JMeterTestData()
                .setLink(params.getMicroservice() + ".html");
        if (exitCode != 0) {
            throw new RunFailedException("JMeter process exited with code %d".formatted(exitCode), data);
        }
        return data;
    }

}
