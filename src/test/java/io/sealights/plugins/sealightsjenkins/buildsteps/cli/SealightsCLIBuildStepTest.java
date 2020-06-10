package io.sealights.plugins.sealightsjenkins.buildsteps.cli;

import io.sealights.agents.infra.integration.enums.LogDestination;
import io.sealights.agents.infra.integration.enums.LogLevel;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class SealightsCLIBuildStepTest {
    String labId = "lab1";
    String buildSessionId = "bs1";

   @Test
    public void createCLIRunner_startCommand_shouldNotSetAppBranchAndBuild() {
        String additionalArgs = "appname=app1\nbuildname=1\nbranchname=master";

        CommandMode.StartView startView = new CommandMode.StartView("stage", buildSessionId, labId, additionalArgs);

        runTest(startView);
    }

    @Test
    public void createCLIRunner_endCommand_shouldNotSetAppBranchAndBuild() {
        String additionalArgs = "appname=app1\nbuildname=1\nbranchname=master";

        CommandMode.EndView endView = new CommandMode.EndView(buildSessionId, labId, additionalArgs);

        runTest(endView);
    }

    @Test
    public void createCLIRunner_uploadReportCommand_shouldNotSetAppBranchAndBuild() {
        String additionalArgs = "appname=app1\nbuildname=1\nbranchname=master";

        CommandMode.UploadReportsView reportsView = new CommandMode.UploadReportsView("report.xml", null, "Junit",
                buildSessionId, labId, additionalArgs);

        runTest(reportsView);
    }

    @Test
    public void createCLIRunner_endCommand_shouldParseLabIdFromAdditionalArgs() {
        String labid = "lab2";
        String additionalArgs = "labid=" + labid;
        CommandMode.EndView endView = new CommandMode.EndView(buildSessionId, null, additionalArgs);
        SealightsCLIBuildStep sealightsCLIBuildStep = createSealightsCLIBuildStep(endView);

        CLIRunner cliRunner = sealightsCLIBuildStep.createCLIRunner(endView);
        Assert.assertEquals(labid,cliRunner.getLabId());
    }

    @Test
    public void invokeReadResolve_nullLogParams_shouldInitiateToDefault() {
        SealightsCLIBuildStep buildStep = new SealightsCLIBuildStep(true, false, mock(CommandMode.class),
                null, null, null, null, null);

        buildStep.invokeReadResolve();

        Assert.assertEquals(buildStep.getLogDestination(), LogDestination.CONSOLE);
        Assert.assertEquals(buildStep.getLogLevel(), LogLevel.OFF);
        Assert.assertEquals(buildStep.getLogFolder(), SealightsCLIBuildStep.DEFAULT_LOGS_FOLDER);
    }

    @Test
    public void createCliRunner_commandModeHasLabId_shouldUseIt() {
        SealightsCLIBuildStep buildStep = new SealightsCLIBuildStep(true, false, mock(CommandMode.class),
                null, null, null, null, null);

        CommandMode.EndView endView = new CommandMode.EndView(buildSessionId, labId, null);
        CLIRunner cliRunner = buildStep.createCLIRunner(endView);

        Assert.assertEquals(cliRunner.getLabId(), labId);
    }

    @Test
    public void createCliRunner_commandModeHasNoLabId_shouldSetToNull() {
        SealightsCLIBuildStep buildStep = new SealightsCLIBuildStep(true, false, mock(CommandMode.class),
                null, null, null, null, null);

        CommandMode.EndView endView = new CommandMode.EndView(buildSessionId, null, null);
        CLIRunner cliRunner = buildStep.createCLIRunner(endView);

        Assert.assertEquals(cliRunner.getLabId(), null);
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
