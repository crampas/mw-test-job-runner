package org.example.scheduler;

import lombok.Getter;

public class RunFailedException extends RuntimeException {
    @Getter
    private final Object data;

    public RunFailedException(String message, Object data) {
        super(message);
        this.data = data;
    }
}
