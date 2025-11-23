package org.example.jmeter;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.example.scheduler.RunEnvironment;
import org.junit.jupiter.api.Test;

class JMeterTestJobTest {
    @Test
    @SneakyThrows
    void test() {
        JMeterTestJob job = new JMeterTestJob();
        RunEnvironment env = new RunEnvironment();
        JMeterTestData data = job.execute(env, new JMeterTestParams().setMicroservice("test"));
        Assertions.assertThat(data).isNotNull();
    }
}