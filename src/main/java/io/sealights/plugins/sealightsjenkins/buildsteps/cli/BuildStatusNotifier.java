package io.sealights.plugins.sealightsjenkins.buildsteps.cli;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import io.sealights.plugins.sealightsjenkins.BeginAnalysis;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.ExternalReportCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors.ExternalReportCommandExecutor;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;

/**
 * This class creates build status report file on the file system and then
 * sends it to the server using the 'externalReport' CLI command.
 * ===================Deprecated=======================
 */
public class BuildStatusNotifier extends Notifier {

    private boolean enabled;
    private String buildSessionId;
    private String appName;
    private String branchName;
    private CommandBuildName buildName;
    private String additionalArguments;
    private String reportTitle;
    private BeginAnalysis beginAnalysis = new BeginAnalysis();

    @DataBoundConstructor
    public BuildStatusNotifier(boolean enabled, String buildSessionId, String appName, String branchName,
                               CommandBuildName buildName, String additionalArguments, String reportTitle) {
        super();
        this.enabled = enabled;
        this.buildSessionId = buildSessionId;
        this.appName = appName;
        this.branchName = branchName;
        this.buildName = buildName;
        this.additionalArguments = additionalArguments;
        this.reportTitle =reportTitle;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {
        Logger logger = new Logger(listener.getLogger(), "SeaLights Build Status Notifier");
        return new ExternalReportCommandExecutor(logger, new BaseCommandArguments(),
         new ExternalReportCommandArguments()).execute();

    }


    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(DescriptorImpl.class);
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        //This is here to ensure that the reported build status is actually correct. If we were to return false here,
        //other build plugins could still modify the build result, making the sent out HipChat notification incorrect.
        return true;
    }

    @Exported
    public boolean isEnabled() {
        return enabled;
    }

    @Exported
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
    public String getAppName() {
        return appName;
    }

    @Exported
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Exported
    public String getBranchName() {
        return branchName;
    }

    @Exported
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Exported
    public CommandBuildName getBuildName() {
        return buildName;
    }

    @Exported
    public void setBuildName(CommandBuildName buildName) {
        this.buildName = buildName;
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
    public BeginAnalysis getBeginAnalysis() {
        return beginAnalysis;
    }

    @Exported
    public void setBeginAnalysis(BeginAnalysis beginAnalysis) {
        this.beginAnalysis = beginAnalysis;
    }

    @Exported
    public String getReportTitle() {
        return reportTitle;
    }

    @Exported
    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        public DescriptorImpl() {
            load();
        }

        @Override
        public String getDisplayName() {
            return "SeaLights - Report Build Status";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public DescriptorExtensionList<CommandBuildName, CommandBuildName.CommandBuildNameDescriptor> getBuildNameDescriptorList() {
            return Jenkins.getInstance().getDescriptorList(CommandBuildName.class);
        }

    }
}
