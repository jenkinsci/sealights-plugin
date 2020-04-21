package io.sealights.plugins.sealightsjenkins.buildsteps.cli;

import io.sealights.agents.infra.integration.enums.LogDestination;
import io.sealights.agents.infra.integration.enums.LogLevel;
import io.sealights.plugins.sealightsjenkins.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogConfiguration {
    public static final String SL_LOG_LEVEL = "sl.log.level";
    public static final String SL_LOG_TO_CONSOLE = "sl.log.toConsole";
    public static final String SL_LOG_TO_FILE = "sl.log.toFile";
    public static final String SL_LOG_FOLDER = "sl.log.folder";
    public static final String SL_LOG_FILE_NAME = "sl.log.filename";

    private String logFolder;
    private LogDestination logDestination = LogDestination.CONSOLE;
    private LogLevel logLevel = LogLevel.OFF;
    private String logFilename;


    public List<String> toSystemProperties() {
        List<String> properties = new ArrayList<>();
        properties.add(formatKeyValue(SL_LOG_LEVEL, logLevel.getDisplayName()));
        if (logLevel.equals(LogLevel.OFF)) {
            return properties;
        }
        if (logDestination.equals(LogDestination.CONSOLE)) {
            properties.add(formatKeyValue(SL_LOG_TO_CONSOLE, "true"));
        }
        if (logDestination.equals(LogDestination.FILE)) {
            properties.add(formatKeyValue(SL_LOG_TO_FILE, "true"));
            if(!StringUtils.isNullOrEmpty(logFilename)){
                properties.add(formatKeyValue(SL_LOG_FILE_NAME, logFilename));
            }
            if (!StringUtils.isNullOrEmpty(logFolder)) {
                properties.add(formatKeyValue(SL_LOG_FOLDER, logFolder));
            }
        }
        return properties;
    }

    public static String formatKeyValue(String key, String value) {
        return "-D" + key + "=" + value;
    }
}
