package org.example.scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class RunExecutor<DATA> {
    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    public CompletableFuture<RunResult<DATA>> schedule(Run<?, DATA> run) {
        return CompletableFuture.supplyAsync(run::run, executor);
    }

    @SneakyThrows
    public void waitFor() {
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.HOURS);
    }
}
