package io.sealights.plugins.sealightsjenkins.utils;


import io.sealights.onpremise.agents.infra.logging.LogFormatAdapter;
import org.slf4j.helpers.MarkerIgnoringBase;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Nadav on 5/5/2016.
 */
public class Logger extends MarkerIgnoringBase {
    private String PREFIX;

    /* This member is static in order to avoid serialization
      (see https://sealights.atlassian.net/browse/SLDEV-4430?filter=10625)
    */
    private static PrintStream printStream;

    public Logger(PrintStream printStream, String prefix) {
        Logger.printStream = printStream;
        this.PREFIX = prefix;
    }

    public Logger(PrintStream printStream) {
        this(printStream, "SeaLights Jenkins Plugin");
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

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
    public void trace(String format, Object arg1, Object arg2) {
        // Trace is not supported
        debug(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... args) {
        // Trace is not supported
        debug(format, args);
    }

    @Override
    public void trace(String message, Throwable throwable) {
        trace(message + stackTraceString(throwable));
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
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
    public void debug(String format, Object arg1, Object arg2) {
        debug(LogFormatAdapter.normalizedMessage(format, arg1, arg2));
    }

    @Override
    public void debug(String format, Object... args) {
        debug(LogFormatAdapter.normalizedMessage(format, args));
    }

    @Override
    public void debug(String message, Throwable throwable) {
        debug(message + stackTraceString(throwable));
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(String message) {
        log("INFO", message);
    }

    @Override
    public void info(String format, Object arg) {
        info(LogFormatAdapter.normalizedMessage(format, arg));
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        info(LogFormatAdapter.normalizedMessage(format, arg1, arg2));
    }

    @Override
    public void info(String format, Object... args) {
        info(LogFormatAdapter.normalizedMessage(format, args));
    }

    @Override
    public void info(String message, Throwable throwable) {
        info(message + stackTraceString(throwable));
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        warn(LogFormatAdapter.normalizedMessage(format, arg1, arg2));
    }

    @Override
    public void warn(String message, Throwable throwable) {
        warn(message + stackTraceString(throwable));
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void warn(String message) {
        log("WARNING", message);
    }

    @Override
    public void warn(String format, Object arg) {
        warn(LogFormatAdapter.normalizedMessage(format, arg));
    }

    @Override
    public void warn(String format, Object... args) {
        warn(LogFormatAdapter.normalizedMessage(format, args));
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
    public void error(String format, Object arg1, Object arg2) {
        error(LogFormatAdapter.normalizedMessage(format, arg1, arg2));
    }

    @Override
    public void error(String format, Object... args) {
        error(LogFormatAdapter.normalizedMessage(format, args));
    }

    @Override
    public void error(String message, Throwable throwable) {
        error(message + stackTraceString(throwable));
    }

    private String stackTraceString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    private void log(String level, String message) {
        printStream.println("[" + PREFIX + "] " + "[" + level + "]" + " " + message);
    }

}
