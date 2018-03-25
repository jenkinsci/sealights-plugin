package io.sealights.plugins.sealightsjenkins.utils;


import io.sealights.onpremise.agents.java.agent.infra.logging.ILogger;
import io.sealights.onpremise.agents.java.agent.infra.logging.LogFormatAdapter;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Nadav on 5/5/2016.
 */
public class Logger implements ILogger {
    private String PREFIX;
    private transient PrintStream printStream;

    public Logger(PrintStream printStream, String prefix) {
        this.printStream = printStream;
        this.PREFIX = prefix;
    }

    public Logger(PrintStream printStream) {
        this(printStream, "SeaLights Jenkins Plugin");
    }

    // The methods below implement the bridge to the ILogger interface
    @Override
    public void trace(String string) {
        // Trace is not supported
        debug(string);
    }

    @Override
    public void trace(String format, Object arg) {
        // Trace is not supported
        debug(format, arg);
    }

    @Override
    public void trace(String format, Object... args) {
        // Trace is not supported
        debug(format, args);
    }

    @Override
    public void debug(String message) {
        log("DEBUG", message);
    }

    @Override
    public void debug(String format, Object arg) {
        debug(LogFormatAdapter.normalizedMessage(format, arg));
    }

    @Override
    public void debug(String format, Object... args) { debug(LogFormatAdapter.normalizedMessage(format, args)); }

    @Override
    public void info(String message) {
        log("INFO", message);
    }

    @Override
    public void info(String format, Object arg) {
        info(LogFormatAdapter.normalizedMessage(format, arg));
    }

    @Override
    public void info(String format, Object... args) {
        info(LogFormatAdapter.normalizedMessage(format, args));
    }

    @Override
    public void warning(String message) {
        log("WARNING", message);
    }

    @Override
    public void warning(String format, Object arg) {
        warning(LogFormatAdapter.normalizedMessage(format, arg));
    }

    @Override
    public void warning(String format, Object... args) {
        warning(LogFormatAdapter.normalizedMessage(format, args));
    }


    @Override
    public void error(String message) {
        log("ERROR", message);
    }

    @Override
    public void error(String format, Object arg) {
        error(String.format(LogFormatAdapter.normalizeFormat(format), arg));
    }

    @Override
    public void error(String format, Object... args) {
        error(LogFormatAdapter.normalizedMessage(format, args));
    }


    @Override
    public void error(String message, Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        message += sw.toString();
        error(message);
    }

    private void log(String level, String message) {
        this.printStream.println("[" + PREFIX + "] " + "[" + level + "]" + " " + message);
    }

}
