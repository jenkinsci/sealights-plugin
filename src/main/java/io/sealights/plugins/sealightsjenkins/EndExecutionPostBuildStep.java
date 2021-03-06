package io.sealights.plugins.sealightsjenkins;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import io.sealights.agents.infra.integration.enums.LogDestination;
import io.sealights.agents.infra.integration.enums.LogLevel;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.*;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.PropertiesUtils;
import io.sealights.plugins.sealightsjenkins.utils.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;

import java.io.IOException;
import java.util.Properties;


/**
 * If tests are failing and start execution command used before then the execution dose not ends.
 * This step is triggering the end execution command as post build action to avoid that scenario.
 */
public class EndExecutionPostBuildStep extends Recorder {
    public static final String DEFAULT_BSID_VAL = "${SL_BUILD_SESSION_ID}";

    private String buildSessionId;
    private String additionalArguments;
    private String logFolder;
    private String logFilename;
    private LogDestination logDestination = LogDestination.CONSOLE;
    private LogLevel logLevel = LogLevel.OFF;
    private String labId;

    @DataBoundConstructor
    public EndExecutionPostBuildStep(String buildSessionId, String additionalArguments, String logFolder,
     String logFilename, LogDestination logDestination, LogLevel logLevel, String labId) {
        this.buildSessionId = buildSessionId;
        this.additionalArguments = additionalArguments;
        this.logFolder = logFolder;
        this.logFilename = logFilename;
        this.logDestination = logDestination;
        this.logLevel = logLevel;
        this.labId = labId;
    }

    /**
     * In order to avoid duplicate code the actual step logic is in {@link CLIRunner} (Same as CLI end command)
     * TODO:// Refactor after handling https://sealights.atlassian.net/browse/SLDEV-7243
     * @param build
     * @param launcher
     * @param listener
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        CommandMode.EndView endView = new CommandMode.EndView(buildSessionId, additionalArguments, labId);
        Logger logger = new Logger(listener.getLogger(), "SeaLights CLI - " + endView.getCurrentMode());
        EnvVars envVars = build.getEnvironment(listener);

        if(isBuildSessionIdNull(envVars)) {
            logger.error("Could not resolve build session id value. raw value is '%s'. Sealights is disabled", buildSessionId);
            return true;
        }
        LogConfiguration logConfiguration = new LogConfiguration(logFolder, logDestination, logLevel, logFilename,
                envVars, logger);
        Properties properties = PropertiesUtils.toProperties(additionalArguments);
        return runCLICommand(build, launcher, listener, endView, logger, logConfiguration, labId);
    }

    public boolean runCLICommand(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener,
     CommandMode.EndView endView, Logger logger, LogConfiguration logConfiguration, String labid) throws IOException,
      InterruptedException {
        CLIHandler cliHandler = new CLIHandler(logger);
        return new CLIRunner(buildSessionId, additionalArguments, new CommandBuildName.DefaultBuildName(),
                labid).perform(build, launcher, listener, endView,
                cliHandler, logger, logConfiguration);
    }

    private boolean isBuildSessionIdNull(EnvVars envVars){
        String expandedVal = envVars.expand(buildSessionId);
        if(StringUtils.isNullOrEmpty(expandedVal) || expandedVal.equals(DEFAULT_BSID_VAL)){
            return true;
        }
        return  false;
    }

    @Exported
    public String getBuildSessionId() {
        return buildSessionId;
    }

    @Exported
    public void setBuildSessionId(String buildSessionId) {
        this.buildSessionId = buildSessionId;
    }

    @Exported
    public String getAdditionalArguments() {
        return additionalArguments;
    }

    @Exported
    public void setAdditionalArguments(String additionalArguments) {
        this.additionalArguments = additionalArguments;
    }

    @Exported
    public LogDestination getLogDestination() {
        return logDestination;
    }

    @Exported
    public void setLogDestination(LogDestination logDestination) {
        this.logDestination = logDestination;
    }

    @Exported
    public String getLogFolder() {
        return logFolder;
    }

    @Exported
    public LogLevel getLogLevel() {
        return logLevel;
    }

    @Exported
    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    @Exported
    public String getLogFilename() {
        return logFilename;
    }

    @Exported
    public void setLogFilename(String logFilename) {
        this.logFilename = logFilename;
    }

    @Exported
    public String getLabId() {
        return labId;
    }

    @Exported
    public void setLabId(String labId) {
        this.labId = labId;
    }

    @Override
    public EndExecutionPostBuildStep.DescriptorImpl getDescriptor() {
        return (EndExecutionPostBuildStep.DescriptorImpl) super.getDescriptor();
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }


    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            return "SeaLights - End test execution";
        }

    }
}
