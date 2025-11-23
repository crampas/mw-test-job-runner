package org.example.scheduler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import tools.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Defines an executable task definition.
 * Must be subclassed to define functionality.
 * @param <PARAMS> input type
 * @param <DATA> output type
 */
@Getter
public abstract class Job<PARAMS, DATA> {
    protected final List<Run<PARAMS, DATA>> runs = new ArrayList<>();
    @JsonIgnore
    protected final RunExecutor<DATA> executor;
    protected int runNumber = 0;

    public Job() {
        executor = new RunExecutor<>();
    }

    public String getName() {
        return StringUtils.substringAfterLast(getClass().getCanonicalName(), ".");
    }

    @JsonIgnore
    public abstract TypeReference<PARAMS> getParamsTypeReference();

    /**
     * Schedules an execution of the task.
     * @param params input params
     * @return the RUn object controlling the execution
     */
    public final Run<PARAMS, DATA> submitRun(PARAMS params) {
        Run<PARAMS, DATA> run = createRun(params);
        run.schedule(executor);
        return run;
    }

    /**
     * Schedules an execution of the task.
     * @param params input params
     * @return future with result data
     */
    public final CompletableFuture<RunResult<DATA>> submit(PARAMS params) {
        Run<PARAMS, DATA> run = createRun(params);
        return run.schedule(executor);
    }

    private Run<PARAMS, DATA> createRun(PARAMS params) {
        String runId = getName() + "-" + (++runNumber);
        Run<PARAMS, DATA> run = new Run<>(runId, params) {
            @Override
            public DATA execute() throws Exception {
                return Job.this.execute(getEnvironment(), getParams());
            }
        };
        runs.add(run);
        return run;
    }

    /**
     * Task implementation.
     * This function is called with the executor worker thread.
     * @param params input params
     * @return result data
     * @throws Exception allow any exception
     */
    protected abstract DATA execute(RunEnvironment env, PARAMS params) throws Exception;

    /**
     * Shuts down task execution and waits for competition.
     */
    public void waitFor() {
        executor.waitFor();
    }
}
