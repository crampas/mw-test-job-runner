package org.example.scheduler;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RunResult<DATA> {
    private RunResultState state;
    private DATA data;
}
