package io.sealights.plugins.sealightsjenkins;

import hudson.EnvVars;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import io.sealights.agents.infra.integration.enums.LogDestination;
import io.sealights.agents.infra.integration.enums.LogLevel;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.CLIHandler;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.CommandMode;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.LogConfiguration;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.Mockito.*;

public class EndExecutionPostBuildStepTest {
    private CLIHandler cliHandler;
    private AbstractBuild<?, ?> build;
    private EnvVars envVars;
    private Launcher launcher;
    private BuildListener listener;
    private PrintStream logger;

    @Before
    public void setUp() throws Exception {
        cliHandler = mock(CLIHandler.class);
        build = mock(AbstractBuild.class);
        envVars = new EnvVars();
        launcher = mock(Launcher.class);
        listener = mock(BuildListener.class);
        logger = mock(PrintStream.class);
        when(cliHandler.handle()).thenReturn(true);
        when(build.getEnvironment(any(BuildListener.class))).thenReturn(envVars);
        when(listener.getLogger()).thenReturn(logger);
    }

    @Test
    public void perform_withLabIdFromAdditionalArgs_shouldUseIt() throws IOException, InterruptedException {
        String lab1 = "lab1";
        EndExecutionPostBuildStep endExecutionPostBuildStep = createInstance("bsid", "labid=" + lab1);
        EndExecutionPostBuildStep spy = spy(endExecutionPostBuildStep);

        spy.perform(build, launcher, listener);

        verify(spy).runCLICommand(eq(build), eq(launcher), eq(listener), any(CommandMode.EndView.class),
                any(Logger.class), any(LogConfiguration.class), eq(lab1));
    }

    private EndExecutionPostBuildStep createInstance(String bsid, String additionalArgs) {
        return new EndExecutionPostBuildStep(bsid, additionalArgs, null, null, LogDestination.CONSOLE, LogLevel.OFF);
    }

}
