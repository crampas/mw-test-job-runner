package org.example.scheduler;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class RunEnvironment {
    public final PrintWriter out;
    public final Logger log;
    private final StringWriter outBuffer = new StringWriter();

    public RunEnvironment() {
        out = new PrintWriter(outBuffer);
        log = new RunLogger(out);
    }

    public String getOutText() {
        return outBuffer.toString();
    }

    public List<String> getOutLines() {
        BufferedReader lineReader = new BufferedReader(new StringReader(outBuffer.toString()));
        return lineReader.lines().toList();
    }

}
