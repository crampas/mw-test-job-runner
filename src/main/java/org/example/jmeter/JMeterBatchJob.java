package org.example.jmeter;

import lombok.extern.slf4j.Slf4j;
import org.example.scheduler.Job;
import org.example.scheduler.RunEnvironment;
import org.example.scheduler.RunResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class JMeterBatchJob extends Job<JMeterBatchParams, JMeterBatchData> {
    private final JMeterTestJob testJob;

    public JMeterBatchJob(JMeterTestJob testJob) {
        this.testJob = testJob;
    }

    @Override
    protected JMeterBatchData execute(RunEnvironment env, JMeterBatchParams params) {
        List<CompletableFuture<RunResult<JMeterTestData>>> futures = new ArrayList<>();
        futures.add(testJob.submit(new JMeterTestParams().setMicroservice("batch-test-1")));
        futures.add(testJob.submit(new JMeterTestParams().setMicroservice("batch-test-2")));
        futures.add(testJob.submit(new JMeterTestParams().setMicroservice("batch-test-3")));
        env.out.println("started all sub tasks!");
        env.log.info("das erste log {} mit daten", 22, new RuntimeException("test"));

        List<RunResult<JMeterTestData>> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        JMeterBatchData data = new JMeterBatchData();
        data.getResults().addAll(results);
        env.log.error("das letzte log");
        env.out.print("... done.");
        return data;
    }

}
