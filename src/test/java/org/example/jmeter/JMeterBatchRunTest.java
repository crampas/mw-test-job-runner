package org.example.jmeter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.example.scheduler.RunEnvironment;
import org.example.scheduler.RunResult;
import org.example.scheduler.RunResultState;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

@Slf4j
class JMeterBatchRunTest {
    @Test
    @SneakyThrows
    void submit() {
        JMeterTestJob testJob = new JMeterTestJob();
        JMeterBatchJob batchJob = new JMeterBatchJob(testJob);

        CompletableFuture<RunResult<JMeterBatchData>> batchResultFuture = batchJob.submit(new JMeterBatchParams());
        RunResult<JMeterBatchData> batchResult = batchResultFuture.get();

        System.out.println(new ObjectMapper().writeValueAsString(testJob.getRuns()));

        Assertions.assertThat(batchResult.getState()).isEqualTo(RunResultState.SUCCESS);
        Assertions.assertThat(batchResult.getData()).isNotNull();
        Assertions.assertThat(batchResult.getData().getResults()).hasSize(3);

        RunEnvironment env = batchJob.getRuns().getFirst().getEnvironment();
        System.out.println(env.getOutLines());

        testJob.waitFor();
        batchJob.waitFor();
    }
}
