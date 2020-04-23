package io.sealights.plugins.sealightsjenkins.buildsteps.cli;

import io.sealights.agents.infra.integration.enums.LogDestination;
import io.sealights.agents.infra.integration.enums.LogLevel;
import org.junit.Assert;
import org.junit.Test;

public class SealightsCLIBuildStepTest {
    String labId = "lab1";
    @Test
    public void createCLIRunner_startCommand_shouldNotSetAppBranchAndBuild() {
        String additionalArgs = "appname=app1\nbuildname=1\nbranchname=master";

        CommandMode.StartView startView = new CommandMode.StartView("stage", "bs1", labId, additionalArgs);

        runTest(startView);
    }

    @Test
    public void createCLIRunner_endCommand_shouldNotSetAppBranchAndBuild() {
        String additionalArgs = "appname=app1\nbuildname=1\nbranchname=master";

        CommandMode.EndView endView = new CommandMode.EndView("bs1", labId, additionalArgs);

        runTest(endView);
    }

    @Test
    public void createCLIRunner_uploadReportCommand_shouldNotSetAppBranchAndBuild() {
        String additionalArgs = "appname=app1\nbuildname=1\nbranchname=master";

        CommandMode.UploadReportsView reportsView = new CommandMode.UploadReportsView("report.xml", null, "Junit",
        "bs1", labId, additionalArgs);

        runTest(reportsView);
    }

    @Test
    public void createCLIRunner_endCommand_shouldParseLabIdFromAdditionalArgs() {
        String labid = "lab2";
        String additionalArgs = "labid=" + labid;
        CommandMode.EndView endView = new CommandMode.EndView("bs1", null, additionalArgs);
        SealightsCLIBuildStep sealightsCLIBuildStep = createSealightsCLIBuildStep(endView);

        CLIRunner cliRunner = sealightsCLIBuildStep.createCLIRunner(endView);
        Assert.assertEquals(labid,cliRunner.getLabId());
    }

    private void runTest(CommandMode mode) {
        SealightsCLIBuildStep sealightsCLIBuildStep = createSealightsCLIBuildStep(mode);

        CLIRunner cliRunner = sealightsCLIBuildStep.createCLIRunner(mode);
        Assert.assertNull(cliRunner.getAppName());
        Assert.assertNull(cliRunner.getBranchName());
        Assert.assertTrue(cliRunner.getBuildName() instanceof CommandBuildName.EmptyBuildName);
    }

    private SealightsCLIBuildStep createSealightsCLIBuildStep(CommandMode mode) {
        return new SealightsCLIBuildStep(true, false, mode, null,
                LogDestination.CONSOLE, null, LogLevel.INFO, null);
    }
}
