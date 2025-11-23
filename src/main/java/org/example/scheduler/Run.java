package org.example.scheduler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a single execution of a task defined in Job.
 * @param <PARAMS> input value type
 * @param <DATA> output value type
 */
@Slf4j
public abstract class Run<PARAMS, DATA> {
    @Getter
    protected final String id;
    @Getter
    private RunState state = RunState.NOT_STARTED;
    @Getter
    private RunResultState resultState = RunResultState.NONE;
    @Getter
    private Instant startTimestamp;
    @Getter
    private Instant endTimestamp;
    @Getter
    private final PARAMS params;
    @Getter
    private DATA data;
    @Getter
    private String message;

    @Getter
    private RunEnvironment environment = new RunEnvironment();


    protected Run(String id, PARAMS params) {
        this.id = id;
        this.params = params;
    }

    /**
     * Schedules this run at the given executor.
     * Does some preparations and calls RunExecutor.schedule().
     * @param executor the executor which schedules the run execution
     * @return Future object with result.
     */
    public final CompletableFuture<RunResult<DATA>> schedule(RunExecutor<DATA> executor) {
        state = RunState.SCHEDULED;
        log.info("run {} scheduled", id);
        return executor.schedule(this);
    }

    /**
     * Called by the executor thread of RunExecutor.
     * @return the RunResult returned by execute()
     */
    public final RunResult<DATA> run() {
        try {
            state = RunState.RUNNING;
            startTimestamp = Instant.now();
            log.info("run {} started", id);
            data = execute();
            resultState = RunResultState.SUCCESS;
        } catch (RunFailedException ex) {
            log.warn("Caught RunFailedException during run {}", id, ex);
            data = (DATA)ex.getData();
            resultState = RunResultState.FAILED;
            message = ex.getMessage();
        } catch (Exception ex) {
            log.warn("Caught exception during run {}", id, ex);
            resultState = RunResultState.FAILED;
            message = "%s: %s".formatted(ex.getClass().getCanonicalName(), ex.getMessage());
        } finally {
            state = RunState.COMPLETED;
            endTimestamp = Instant.now();
            log.info("run {} completed with {} in {}",
                    id, resultState, Duration.between(startTimestamp, endTimestamp));
        }
        return new RunResult<DATA>()
                .setState(resultState)
                .setData(data);
    }

    /**
     * Only for explicit subclassing - the main working function.
     * @return result data
     * @throws Exception allow throwing of any exception
     */
    protected abstract DATA execute() throws Exception;
}
