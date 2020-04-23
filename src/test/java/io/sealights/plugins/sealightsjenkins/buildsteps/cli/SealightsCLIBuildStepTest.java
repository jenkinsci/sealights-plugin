package io.sealights.plugins.sealightsjenkins.buildsteps.cli;

import io.sealights.agents.infra.integration.enums.LogDestination;
import io.sealights.agents.infra.integration.enums.LogLevel;
import org.junit.Assert;
import org.junit.Test;

public class SealightsCLIBuildStepTest {

    @Test
    public void createCLIRunner_startCommand_shouldNotSetAppBranchAndBuild() {
        String additionalArgs = "appname=app1\nbuildname=1\nbranchname=master";

        CommandMode.StartView startView = new CommandMode.StartView("stage", "bs1", additionalArgs);

        runTest(startView);

    }

    @Test
    public void createCLIRunner_endCommand_shouldNotSetAppBranchAndBuild() {
        String additionalArgs = "appname=app1\nbuildname=1\nbranchname=master";

        CommandMode.EndView endView = new CommandMode.EndView("bs1", additionalArgs);

        runTest(endView);

    }

    @Test
    public void createCLIRunner_uploadReportCommand_shouldNotSetAppBranchAndBuild() {
        String additionalArgs = "appname=app1\nbuildname=1\nbranchname=master";

        CommandMode.UploadReportsView reportsView = new CommandMode.UploadReportsView("report.xml", null, "Junit",
        "bs1", additionalArgs);

        runTest(reportsView);

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
