package io.sealights.plugins.sealightsjenkins.buildsteps.cli;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.Items;
import io.sealights.plugins.sealightsjenkins.BuildName;
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
    private  String buildSessionId;
    private  String additionalArguments;

    private CommandMode(final CommandModes currentMode, final String buildSessionId, final String additionalArguments) {
        this.currentMode = currentMode;
        this.buildSessionId = buildSessionId;
        this.additionalArguments = additionalArguments;
    }

    @Exported
    public void setBuildSessionId(String buildSessionId) {
        this.buildSessionId = buildSessionId;
    }

    @Exported
    public void setAdditionalArguments(String additionalArguments) {
        this.additionalArguments = additionalArguments;
    }

    @Exported

    public String getBuildSessionId() {
        return buildSessionId;
    }

    @Exported
    public String getAdditionalArguments() {
        return additionalArguments;
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
    }

    public static class StartView extends CommandMode {

        private String testStage;

        @DataBoundConstructor
        public StartView(String testStage, String buildSessionId, String additionalArguments) {
            super(CommandModes.Start, buildSessionId, additionalArguments);
            this.testStage = testStage;
        }

        public String getTestStage() {
            return testStage;
        }

        public void setTestStage(String testStage) {
            this.testStage = testStage;
        }

        @Extension
        public static class StartDescriptor extends CommandModeDescriptor {

            @Override
            public boolean isDefault() {
                return false;
            }

            public StartDescriptor() {
                super(StartView.class, CommandModes.Start.getDisplayName());
            }
        }

    }

    public static class EndView extends CommandMode {

        @DataBoundConstructor
        public EndView(String buildSessionId, String additionalArguments) {
            super(CommandModes.End, buildSessionId, additionalArguments);
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
        public UploadReportsView(String reportFiles, String reportsFolders, boolean hasMoreRequests,
                                 String source, String buildSessionId, String additionalArguments) {
            super(CommandModes.UploadReports, buildSessionId, additionalArguments);
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
        public ExternalReportView(String report, String buildSessionId, String additionalArguments) {
            super(CommandModes.ExternalReport, buildSessionId, additionalArguments);
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

        private String packagesIncluded;
        private String packagesExcluded;

        private String appName;
        private String branchName;
        private CommandBuildName buildName;
        private String labId;

        @DataBoundConstructor
        public ConfigView(String packagesIncluded, String packagesExcluded, String appName, String branchName,
                          CommandBuildName buildName, String labId, String buildSessionId, String additionalArguments) {
            super(CommandModes.Config, buildSessionId, additionalArguments);
            this.packagesIncluded = packagesIncluded;
            this.packagesExcluded = packagesExcluded;
            this.appName = appName;
            this.branchName = branchName;
            this.buildName = buildName;
            this.labId = labId;
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
        public String getLabId() {
            return labId;
        }

        @Exported
        public void setLabId(String labId) {
            this.labId = labId;
        }

        @Exported
        public String getPackagesIncluded() {
            return packagesIncluded;
        }

        @Exported
        public void setPackagesIncluded(String packagesIncluded) {
            this.packagesIncluded = packagesIncluded;
        }

        @Exported
        public String getPackagesExcluded() {
            return packagesExcluded;
        }

        @Exported
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

            public DescriptorExtensionList<CommandBuildName, CommandBuildName.CommandBuildNameDescriptor> getBuildNameDescriptorList() {
                return Jenkins.getInstance().getDescriptorList(CommandBuildName.class);
            }
        }

    }


}
