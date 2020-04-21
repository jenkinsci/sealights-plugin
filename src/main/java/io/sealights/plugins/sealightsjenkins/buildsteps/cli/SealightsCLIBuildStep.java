package io.sealights.plugins.sealightsjenkins.buildsteps.cli;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.DescribableList;
import io.sealights.agents.infra.integration.enums.LogDestination;
import io.sealights.agents.infra.integration.enums.LogLevel;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptionsDescriptor;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.CommandBuildNamingStrategy;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.PropertiesUtils;
import io.sealights.plugins.sealightsjenkins.utils.StringUtils;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.IOException;
import java.util.*;

public class SealightsCLIBuildStep extends Builder {

    public boolean enabled;
    public boolean failBuildIfStepFail;
    public CommandMode commandMode;
    public CLIRunner cliRunner;
    private String logFolder;
    private String logFilename;
    private LogDestination logDestination = LogDestination.CONSOLE;
    private LogLevel logLevel = LogLevel.OFF;

    @DataBoundConstructor
    public SealightsCLIBuildStep(boolean enabled, boolean failBuildIfStepFail,
                                 CommandMode commandMode, CLIRunner cliRunner,
                                  LogDestination logDestination, String logFolder, LogLevel logLevel, String logFilename) {
        this.enabled = enabled;
        this.failBuildIfStepFail = failBuildIfStepFail;
        this.commandMode = commandMode;
        this.cliRunner = cliRunner;
        this.logLevel = logLevel;
        this.logDestination = logDestination;
        this.logFolder = logFolder;
        this.logFilename = logFilename;
    }

    /* * The goal of this method is to support migration of data between versions
    * of this plugin.
    */
    private Object readResolve() {
        if (cliRunner != null) {
            resolveFromCLiRunner();
        }
        if(commandMode instanceof CommandMode.ConfigView){
            resolveTechOption();
        }
        return this;
    }

    private SealightsCLIBuildStep resolveTechOption(){
        CommandMode.ConfigView config = (CommandMode.ConfigView) commandMode;
        DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> technologyOptions = config.getTechOptions();
        config.setTechOptions(technologyOptions);
        return this;
    }

    private SealightsCLIBuildStep resolveFromCLiRunner() {
        StringBuilder  additionalArgs = new StringBuilder();
        if (this.commandMode instanceof CommandMode.ConfigView) {
               commandMode =getConfigParams();
        } else  {
            if (!StringUtils.isNullOrEmpty(cliRunner.getAppName())) {
                additionalArgs.append("appname=" + cliRunner.getAppName() + "\n");
            }
            if (!StringUtils.isNullOrEmpty(cliRunner.getBranchName())) {
                additionalArgs.append("branchname=" + cliRunner.getBranchName() + "\n");
            }
            if (shouldAddBuildName()) {
                additionalArgs.append("buildname=" + resolveBuildName(cliRunner.getBuildName()) + "\n");
            }

            if(this.commandMode instanceof  CommandMode.UploadReportsView){
                CommandMode.UploadReportsView uploadReportsView = (CommandMode.UploadReportsView) this.commandMode;
                if(!StringUtils.isNullOrEmpty(uploadReportsView.getSource())){
                    additionalArgs.append("source="+uploadReportsView.getSource()+"\n");
                }

            }
        }
        if (!StringUtils.isNullOrEmpty(cliRunner.getAdditionalArguments()))
            additionalArgs.insert(0, cliRunner.getAdditionalArguments().trim() + "\n");
        commandMode.setAdditionalArguments(additionalArgs.toString());
        return this;
    }

    private CommandMode.ConfigView getConfigParams(){
        CommandMode.ConfigView configView = (CommandMode.ConfigView) commandMode;
        configView.setAppName(cliRunner.getAppName());
        configView.setBranchName(cliRunner.getBranchName());
        configView.setBuildName(cliRunner.getBuildName());
        return  configView;
    }

    public CommandMode getCommandMode() {
        return commandMode;
    }

    public void setCommandMode(CommandMode commandMode) {
        this.commandMode = commandMode;
    }

    public CLIRunner getCliRunner() {
        return cliRunner;
    }

    public void setCliRunner(CLIRunner cliRunner) {
        this.cliRunner = cliRunner;
    }

    public boolean isEnable() {
        return enabled;
    }

    public void setEnable(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isFailBuildIfStepFail() {
        return failBuildIfStepFail;
    }

    public void setFailBuildIfStepFail(boolean failBuildIfStepFail) {
        this.failBuildIfStepFail = failBuildIfStepFail;
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

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        boolean isStepSuccessful = false;
        Logger logger = new Logger(listener.getLogger(), "SeaLights CLI - " + commandMode.getCurrentMode().getName());

        try {
            if (!enabled) {
                logger.info("Sealights CLI step is disabled.");
                return true;
            }
            LogConfiguration logConfiguration = new LogConfiguration(logFolder, logDestination, logLevel, logFilename);

            CLIHandler cliHandler = new CLIHandler(logger);
            cliRunner = createCLIRunner(commandMode);
            isStepSuccessful = cliRunner.perform(build, launcher, listener, commandMode, cliHandler, logger, logConfiguration);
        } catch (Exception e) {
            logger.error("Error occurred while performing 'Sealights CLI Build Step' Skipping sealights integration. " +
             "Error: ", e);
            enabled = false;
        }

        if (failBuildIfStepFail) {
            return isStepSuccessful;
        }

        return true;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public DescriptorImpl() {
            load();
        }

        public DescriptorExtensionList<CommandMode, CommandMode.CommandModeDescriptor> getCommandModeDescriptorList() {
            DescriptorExtensionList<CommandMode, CommandMode.CommandModeDescriptor> descriptorList = Jenkins.getInstance().getDescriptorList(CommandMode.class);
            return descriptorList;
        }

        @Override
        public Builder newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            JSONObject commandMode = (JSONObject) formData.get("commandMode");
            if(commandMode == null){
                return null;
            }
            if(!commandMode.containsKey("techOptions")){
                commandMode.put("techOptions",new ArrayList<TechnologyOptions>());
            }
            return req.bindJSON(SealightsCLIBuildStep.class, formData);
        }

        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        public String getDisplayName() {
            return "Sealights CLI";
        }
    }

    private CLIRunner createCLIRunner(CommandMode commandMode){
        if(commandMode instanceof CommandMode.ConfigView){
            CommandMode.ConfigView configView=(CommandMode.ConfigView) commandMode;
            return new CLIRunner(configView.getBuildSessionId(),configView.getAppName(),configView.getBranchName(),
                    configView.getBuildName(),configView.getAdditionalArguments(),null);
        }else {
            Properties properties = PropertiesUtils.toProperties(commandMode.getAdditionalArguments());
            String appName = (properties.get("appname")!= null)? properties.get("appname").toString():null;
            String branchName= (properties.get("branchname")!= null)? properties.get("branchname").toString():null;
            String labId= (properties.get("labid")!= null)? properties.get("labid").toString():null;
            CommandBuildName buildName = new CommandBuildName.ManualBuildName((String) properties.get("buildname"));
            return new CLIRunner(commandMode.getBuildSessionId(),appName,branchName,buildName,commandMode.getAdditionalArguments(),labId);
        }
    }

    private String resolveBuildName(CommandBuildName buildName){
        if(CommandBuildNamingStrategy.MANUAL.equals(buildName.getBuildNamingStrategy())) {
            CommandBuildName.ManualBuildName manual = (CommandBuildName.ManualBuildName) buildName;
            return manual.getInsertedBuildName();
        }
        if (CommandBuildNamingStrategy.JENKINS_BUILD.equals(buildName.getBuildNamingStrategy()))
            return "${BUILD_NUMBER}";
        if(CommandBuildNamingStrategy.JENKINS_UPSTREAM.equals(buildName.getBuildNamingStrategy()))
            return "SL_UPSTREAM_BUILD";
        return null;
    }

    private boolean shouldAddBuildName(){
        return cliRunner.getBuildName() != null &&
                cliRunner.getBuildName().getBuildNamingStrategy() != CommandBuildNamingStrategy.EMPTY_BUILD;
    }
}
