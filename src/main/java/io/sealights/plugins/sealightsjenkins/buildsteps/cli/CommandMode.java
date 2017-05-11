package io.sealights.plugins.sealightsjenkins.buildsteps.cli;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import io.sealights.plugins.sealightsjenkins.BeginAnalysis;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.CommandModes;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;

import java.io.Serializable;

/**
 * This class holds the different command options and their arguments in the UI
 */
public class CommandMode implements Describable<CommandMode>, ExtensionPoint, Serializable {

    private final CommandModes currentMode;

    private CommandMode(final CommandModes currentMode) {
        this.currentMode = currentMode;
    }

    @Exported
    public CommandModes getCurrentMode() {
        return currentMode;
    }

    @Override
    public Descriptor<CommandMode> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
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

        private String testStage;
        private DefaultBuildParams defaultBuildParams;

        @DataBoundConstructor
        public StartView(String testStage,DefaultBuildParams defaultBuildParams) {
            super(CommandModes.Start);
            this.testStage = testStage;
            this.defaultBuildParams=defaultBuildParams;
        }

        public String getTestStage() {
            return testStage;
        }

        public void setTestStage(String testStage) {
            this.testStage = testStage;
        }

        public DefaultBuildParams getDefaultBuildParams() {
            return defaultBuildParams;
        }

        public void setDefaultBuildParams(DefaultBuildParams defaultBuildParams) {
            this.defaultBuildParams = defaultBuildParams;
        }

        @Extension
        public static class StartDescriptor extends CommandModeDescriptor {
            private DefaultBuildParams defaultBuildParams;
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
        private DefaultBuildParams defaultBuildParams;
        @DataBoundConstructor
        public EndView(DefaultBuildParams defaultBuildParams) {
            super(CommandModes.End);
            this.defaultBuildParams=defaultBuildParams;
        }

        @Extension
        public static class EndDescriptor extends CommandModeDescriptor {
            public EndDescriptor() {
                super(EndView.class, CommandModes.End.getDisplayName());
            }
        }

        public DefaultBuildParams getDefaultBuildParams() {
            return defaultBuildParams;
        }

        public void setDefaultBuildParams(DefaultBuildParams defaultBuildParams) {
            this.defaultBuildParams = defaultBuildParams;
        }
    }

    public static class UploadReportsView extends CommandMode {
        private DefaultBuildParams defaultBuildParams;
        private String reportFiles;
        private String reportsFolders;
        private boolean hasMoreRequests;
        private String source;

        @DataBoundConstructor
        public UploadReportsView(String reportFiles, String reportsFolders, boolean hasMoreRequests, String source, DefaultBuildParams defaultBuildParams) {
            super(CommandModes.UploadReports);
            this.reportFiles = reportFiles;
            this.reportsFolders = reportsFolders;
            this.hasMoreRequests = hasMoreRequests;
            this.source = source;
            this.defaultBuildParams=defaultBuildParams;
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

        public DefaultBuildParams getDefaultBuildParams() {
            return defaultBuildParams;
        }

        public void setDefaultBuildParams(DefaultBuildParams defaultBuildParams) {
            this.defaultBuildParams = defaultBuildParams;
        }

        @Extension
        public static class UploadReportsDescriptor extends CommandModeDescriptor {
            public UploadReportsDescriptor() {
                super(UploadReportsView.class, CommandModes.UploadReports.getDisplayName());
            }
        }

    }

    public static class ExternalReportView extends CommandMode {
        private DefaultBuildParams defaultBuildParams;
        private String report;

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public DefaultBuildParams getDefaultBuildParams() {
            return defaultBuildParams;
        }

        public void setDefaultBuildParams(DefaultBuildParams defaultBuildParams) {
            this.defaultBuildParams = defaultBuildParams;
        }

        @DataBoundConstructor
        public ExternalReportView(String report, DefaultBuildParams defaultBuildParams) {
            super(CommandModes.ExternalReport);
            this.report = report;
            this.defaultBuildParams=defaultBuildParams;
        }

        @Extension
        public static class ExternalReportDescriptor extends CommandModeDescriptor {
            public ExternalReportDescriptor() {
                super(ExternalReportView.class, CommandModes.ExternalReport.getDisplayName());
            }
        }

    }

    public static class ConfigView extends CommandMode {
        private DefaultBuildParams defaultBuildParams;
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
            super(currentMode.Config);
            this.defaultBuildParams = defaultBuildParams;
            this.branchName = branchName;
            this.buildName = buildName;
            this.labId = labId;
            this.appName = appName;
            this.beginAnalysis = beginAnalysis;
            this.packagesIncluded = packagesIncluded;
            this.packagesExcluded = packagesExcluded;
        }

        public DefaultBuildParams getDefaultBuildParams() {
            return defaultBuildParams;
        }

        public void setDefaultBuildParams(DefaultBuildParams defaultBuildParams) {
            this.defaultBuildParams = defaultBuildParams;
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
