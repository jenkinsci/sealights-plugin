package io.sealights.plugins.sealightsjenkins.buildsteps.cli;


import io.sealights.agents.infra.integration.enums.LogDestination;
import io.sealights.agents.infra.integration.enums.LogLevel;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LogConfigurationTest {

    @Test
    public void toSystemProperties_logsLevelOff_shouldNotAddLogToConsole(){
        List<String> expectedProps = new ExpectedPropsBuilder().withLogLevel(LogLevel.OFF).build();
        LogConfiguration logConfiguration = new LogConfiguration(null, LogDestination.CONSOLE, LogLevel.OFF, null);

        runTestAndAssert(expectedProps, logConfiguration);
    }

    @Test
    public void toSystemProperties_logsLevelInfo_shouldAddLogToConsole(){
        runLogLevelTest(LogLevel.INFO);
    }

    @Test
    public void toSystemProperties_logsLevelDebug_shouldAddLogToConsole(){
        runLogLevelTest(LogLevel.DEBUG);
    }

    @Test
    public void toSystemProperties_logsLevelWarn_shouldAddLogToConsole(){
        runLogLevelTest(LogLevel.WARN);
    }

    @Test
    public void toSystemProperties_logsLevelError_shouldAddLogToConsole(){
        runLogLevelTest(LogLevel.ERROR);
    }

    @Test
    public void toSystemProperties_logsToFile_shouldAddToProperties(){
        List<String> expectedProps = new ExpectedPropsBuilder().withLogLevel(LogLevel.INFO).withLogToFile().build();
        LogConfiguration logConfiguration = new LogConfiguration(null, LogDestination.FILE, LogLevel.INFO, null);

        runTestAndAssert(expectedProps, logConfiguration);
    }

    @Test
    public void toSystemProperties_logsToFileWithFolder_shouldAddFolderToProperties(){
        String logFolder = "log/sl";
        List<String> expectedProps =
         new ExpectedPropsBuilder().withLogLevel(LogLevel.INFO).withLogToFile().withLogToFolder(logFolder).build();
        LogConfiguration logConfiguration = new LogConfiguration(logFolder, LogDestination.FILE, LogLevel.INFO, null);

        runTestAndAssert(expectedProps, logConfiguration);
    }

    @Test
    public void toSystemProperties_logsToConsoleWithFolder_shouldNotAddFolderToProperties(){
        String logFolder = "log/sl";
        List<String> expectedProps =
                new ExpectedPropsBuilder().withLogLevel(LogLevel.INFO).withLogToConsole().build();
        LogConfiguration logConfiguration = new LogConfiguration(logFolder, LogDestination.CONSOLE, LogLevel.INFO, null);

        runTestAndAssert(expectedProps, logConfiguration);
    }

    @Test
    public void toSystemProperties_logsToFileWithFilename_shouldAddFileNameToProperties(){
        String logFile = "sl-logs";
        List<String> expectedProps =
                new ExpectedPropsBuilder().withLogLevel(LogLevel.INFO).withLogToFile().withLogFilename(logFile).build();
        LogConfiguration logConfiguration = new LogConfiguration(null, LogDestination.FILE, LogLevel.INFO, logFile);

        runTestAndAssert(expectedProps, logConfiguration);
    }

    @Test
    public void toSystemProperties_logsToConsoleWithFilename_shouldNotAddFileNameToProperties(){
        String logFile = "sl-logs";
        List<String> expectedProps =
                new ExpectedPropsBuilder().withLogLevel(LogLevel.INFO).withLogToConsole().build();
        LogConfiguration logConfiguration = new LogConfiguration(null, LogDestination.CONSOLE, LogLevel.INFO, logFile);

        runTestAndAssert(expectedProps, logConfiguration);
    }

    private void runLogLevelTest(LogLevel level){
        List<String> expectedProps = new ExpectedPropsBuilder().withLogLevel(level).withLogToConsole().build();
        LogConfiguration logConfiguration = new LogConfiguration(null, LogDestination.CONSOLE, level, null);

        runTestAndAssert(expectedProps, logConfiguration);
    }

    private void runTestAndAssert(List<String> expectedProps, LogConfiguration logConfiguration) {
        List<String> actualProps = logConfiguration.toSystemProperties();

        Assert.assertEquals(expectedProps, actualProps);
    }
}

class ExpectedPropsBuilder {
    List<String> props = new ArrayList<>();

    public ExpectedPropsBuilder withLogLevel(LogLevel level){
        props.add(LogConfiguration.formatKeyValue(LogConfiguration.SL_LOG_LEVEL, level.getDisplayName()));
        return this;
    }

    public ExpectedPropsBuilder withLogToConsole(){
        props.add(LogConfiguration.formatKeyValue(LogConfiguration.SL_LOG_TO_CONSOLE, "true"));
        return this;
    }

    public ExpectedPropsBuilder withLogToFile(){
        props.add(LogConfiguration.formatKeyValue(LogConfiguration.SL_LOG_TO_FILE, "true"));
        return this;
    }

    public ExpectedPropsBuilder withLogToFolder(String folder){
        props.add(LogConfiguration.formatKeyValue(LogConfiguration.SL_LOG_FOLDER, folder));
        return this;
    }

    public ExpectedPropsBuilder withLogFilename(String filename){
        props.add(LogConfiguration.formatKeyValue(LogConfiguration.SL_LOG_FILE_NAME, filename));
        return this;
    }

    public List<String> build() {
        return props;
    }
}
