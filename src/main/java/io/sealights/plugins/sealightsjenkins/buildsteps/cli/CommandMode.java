package io.sealights.plugins.sealightsjenkins.buildsteps.cli;

import com.thoughtworks.xstream.mapper.Mapper;
import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.XmlFile;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.Items;
import io.sealights.plugins.sealightsjenkins.BeginAnalysis;
import io.sealights.plugins.sealightsjenkins.RestoreBuildFile;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.CommandModes;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * This class holds the different command options and their arguments in the UI
 */
public class CommandMode implements Describable<CommandMode>, ExtensionPoint, Serializable {

    private final CommandModes currentMode;
    protected DefaultBuildParams defaultBuildParams;

    public DefaultBuildParams getDefaultBuildParams() {
        return defaultBuildParams;
    }

    public void setDefaultBuildParams(DefaultBuildParams defaultBuildParams) {
        this.defaultBuildParams = defaultBuildParams;
    }

    private CommandMode(final CommandModes currentMode, DefaultBuildParams defaultBuildParams) {
        this.currentMode = currentMode;
        this.defaultBuildParams =defaultBuildParams;
    }

    @Exported
    public CommandModes getCurrentMode() {
        return currentMode;
    }

    @Override
    public Descriptor<CommandMode> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(this.getClass());
    }

    public static class CommandModeDescriptor extends Descriptor<CommandMode> {

        private String selectedMode;

        protected CommandModeDescriptor(final Class<? extends CommandMode> clazz, final String selectedMode) {
            super(clazz);
            this.selectedMode = selectedMode;
        }

        public boolean isDefault() {
            return false;
        }

        @Override
        public String getDisplayName() {
            return selectedMode;
        }

        public DescriptorExtensionList<CommandMode, CommandModeDescriptor> getRepositoryLocationDescriptors() {
            return Hudson.getInstance().getDescriptorList(CommandMode.class);
        }
        public DescriptorExtensionList<CommandBuildName, CommandBuildName.CommandBuildNameDescriptor> getBuildNameDescriptorList() {
            return Jenkins.getInstance().getDescriptorList(CommandBuildName.class);
        }
    }

    public static class StartView extends CommandMode {
        CLIRunner cliRunner;
        private String testStage;

        @DataBoundConstructor
        public StartView(String testStage,DefaultBuildParams defaultBuildParams, CLIRunner cliRunner) {
            super(CommandModes.Start,defaultBuildParams);
            this.testStage = testStage;
            this.cliRunner=cliRunner;
        }

        public String getTestStage() {
            return testStage;
        }

        public void setTestStage(String testStage) {
            this.testStage = testStage;
        }

        @Override
        public Descriptor<CommandMode> getDescriptor() {
            return Jenkins.getInstance().getDescriptorOrDie(this.getClass());
        }



        @Extension
        public static class StartDescriptor extends CommandModeDescriptor {

            @Override
            public boolean isDefault() {
                return true;
            }

            public StartDescriptor() {
                super(StartView.class, CommandModes.Start.getDisplayName());
            }
        }

    }

    public static class EndView extends CommandMode {
        @DataBoundConstructor
        public EndView(DefaultBuildParams defaultBuildParams) {
            super(CommandModes.End, defaultBuildParams);
        }

        @Extension
        public static class EndDescriptor extends CommandModeDescriptor {
            public EndDescriptor() {
                super(EndView.class, CommandModes.End.getDisplayName());
            }
        }
    }

    public static class UploadReportsView extends CommandMode {
        private String reportFiles;
        private String reportsFolders;
        private boolean hasMoreRequests;
        private String source;

        @DataBoundConstructor
        public UploadReportsView(String reportFiles, String reportsFolders, boolean hasMoreRequests, String source, DefaultBuildParams defaultBuildParams) {
            super(CommandModes.UploadReports, defaultBuildParams);
            this.reportFiles = reportFiles;
            this.reportsFolders = reportsFolders;
            this.hasMoreRequests = hasMoreRequests;
            this.source = source;
        }

        public String getReportFiles() {
            return reportFiles;
        }

        public void setReportFiles(String reportFiles) {
            this.reportFiles = reportFiles;
        }

        public String getReportsFolders() {
            return reportsFolders;
        }

        public void setReportsFolders(String reportsFolders) {
            this.reportsFolders = reportsFolders;
        }

        public boolean getHasMoreRequests() {
            return hasMoreRequests;
        }

        public void setHasMoreRequests(boolean hasMoreRequests) {
            this.hasMoreRequests = hasMoreRequests;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }


        @Extension
        public static class UploadReportsDescriptor extends CommandModeDescriptor {
            public UploadReportsDescriptor() {
                super(UploadReportsView.class, CommandModes.UploadReports.getDisplayName());
            }
        }

    }

    public static class ExternalReportView extends CommandMode {
        private String report;

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        @DataBoundConstructor
        public ExternalReportView(String report, DefaultBuildParams defaultBuildParams) {
            super(CommandModes.ExternalReport, defaultBuildParams);
            this.report = report;
        }

        @Extension
        public static class ExternalReportDescriptor extends CommandModeDescriptor {
            public ExternalReportDescriptor() {
                super(ExternalReportView.class, CommandModes.ExternalReport.getDisplayName());
            }
        }

    }

    public static class ConfigView extends CommandMode {
        private String branchName;
        private CommandBuildName buildName;
        private String labId;
        private String appName;
        private BeginAnalysis beginAnalysis = new BeginAnalysis();
        private String packagesIncluded;
        private String packagesExcluded;

        @DataBoundConstructor
        public ConfigView(CommandModes currentMode, DefaultBuildParams defaultBuildParams, String branchName,
                          CommandBuildName buildName, String labId, String appName, BeginAnalysis beginAnalysis,
                          String packagesIncluded, String packagesExcluded) {
            super(currentMode.Config, defaultBuildParams);
            this.branchName = branchName;
            this.buildName = buildName;
            this.labId = labId;
            this.appName = appName;
            this.beginAnalysis = beginAnalysis;
            this.packagesIncluded = packagesIncluded;
            this.packagesExcluded = packagesExcluded;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public CommandBuildName getBuildName() {
            return buildName;
        }

        public void setBuildName(CommandBuildName buildName) {
            this.buildName = buildName;
        }

        public String getLabId() {
            return labId;
        }

        public void setLabId(String labId) {
            this.labId = labId;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public BeginAnalysis getBeginAnalysis() {
            return beginAnalysis;
        }

        public void setBeginAnalysis(BeginAnalysis beginAnalysis) {
            this.beginAnalysis = beginAnalysis;
        }

        public String getPackagesIncluded() {
            return packagesIncluded;
        }

        public void setPackagesIncluded(String packagesIncluded) {
            this.packagesIncluded = packagesIncluded;
        }

        public String getPackagesExcluded() {
            return packagesExcluded;
        }

        public void setPackagesExcluded(String packagesExcluded) {
            this.packagesExcluded = packagesExcluded;
        }

        @Extension
        public static class ConfigDescriptor extends CommandModeDescriptor {

            @Override
            public boolean isDefault() {
                return true;
            }

            public ConfigDescriptor() {
                super(ConfigView.class, CommandModes.Config.getDisplayName());
            }


        }

    }

}
