package org.example.jmeter;

import lombok.Getter;
import org.example.scheduler.RunResult;

import java.util.ArrayList;
import java.util.List;

@Getter
public class JMeterBatchData {
    private final List<RunResult<JMeterTestData>> results = new ArrayList<>();
}
