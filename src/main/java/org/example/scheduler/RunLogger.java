package org.example.scheduler;

import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.AbstractLogger;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;

public class RunLogger extends AbstractLogger {
    private final PrintWriter writer;

    RunLogger(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return "---RUN---";
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String messagePattern, Object[] arguments, Throwable throwable) {
        String formattedMessage = MessageFormatter.basicArrayFormat(messagePattern, arguments);
        writer.println(formattedMessage);
        if (throwable != null) {
            throwable.printStackTrace(writer);
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return true;
    }
}
