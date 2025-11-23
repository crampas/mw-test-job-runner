package org.example.jmeter;

import lombok.extern.slf4j.Slf4j;
import org.example.scheduler.Job;
import org.example.scheduler.RunEnvironment;
import org.example.scheduler.RunResult;
import tools.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class JMeterBatchJob extends Job<JMeterBatchParams, JMeterBatchData> {
    private final JMeterTestJob testJob;

    public JMeterBatchJob(JMeterTestJob testJob) {
        this.testJob = testJob;
    }

    public TypeReference<JMeterBatchParams> getParamsTypeReference() {
        return new TypeReference<>() {};
    }

    @Override
    protected JMeterBatchData execute(final RunEnvironment env, JMeterBatchParams params) {
        List<CompletableFuture<RunResult<JMeterTestData>>> futures = new ArrayList<>();
        futures.add(submitTest(env, new JMeterTestParams().setMicroservice("batch-test-1")));
        futures.add(submitTest(env, new JMeterTestParams().setMicroservice("batch-test-2")));
        futures.add(submitTest(env, new JMeterTestParams().setMicroservice("batch-test-3")));
        env.out.println("started all sub tasks!");
        // env.log.info("das erste log {} mit daten", 22, new RuntimeException("test"));

        // wait for completition
        List<RunResult<JMeterTestData>> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        JMeterBatchData data = new JMeterBatchData();
        data.getResults().addAll(results);
        env.log.error("das letzte log");
        env.out.print("... done.");
        return data;
    }

    private CompletableFuture<RunResult<JMeterTestData>> submitTest(RunEnvironment env, JMeterTestParams params) {
        env.log.info("scheduling sub task {}", params);
        return testJob.submit(params)
                .thenApply(item -> {
                    env.log.info("sub task {} completed with {}", params, item.getState());
                    return item;
                });
    }


}
