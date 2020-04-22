package io.sealights.plugins.sealightsjenkins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.CLIHandler;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.CLIRunner;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.CommandBuildName;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.CommandMode;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;

import java.io.IOException;

public class EndExecutionPostBuildStep extends Recorder {

    private String buildSessionId;
    private String additionalArguments;

    @DataBoundConstructor
    public EndExecutionPostBuildStep(String buildSessionId, String additionalArguments) {
        this.buildSessionId = buildSessionId;
        this.additionalArguments = additionalArguments;
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
        CommandMode.EndView endView = new CommandMode.EndView(buildSessionId, additionalArguments);
        Logger logger = new Logger(listener.getLogger(), "SeaLights CLI - " + endView.getCurrentMode());
        CLIHandler cliHandler = new CLIHandler(logger);
        return new CLIRunner(buildSessionId, additionalArguments, new CommandBuildName.DefaultBuildName()).perform(build, launcher, listener, endView,
                cliHandler, logger);
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
