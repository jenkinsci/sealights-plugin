package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import hudson.EnvVars;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.LogConfiguration;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.ExternalReportCommandArguments;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.NullLogger;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SendExternalReportTest {

    private Logger nullLogger = new NullLogger();

    @Test
    public void execute_giveValidExternalReportArguments_shouldExecuteCorrectCommand() throws IOException {
        //Arrange
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        ExternalReportCommandArguments externalReportArguments = createExternalReportArguments();
        ExternalReportCommandExecutor externalReportExecutor = new ExternalReportCommandExecutor(nullLogger, baseCommandArguments, externalReportArguments);


        ProcessExecutor execMock = mock(ProcessExecutor.class);
        final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        //Act
        externalReportExecutor.setProcessExecutor(execMock);
        externalReportExecutor.execute();
        verify(execMock).command(captor.capture());
        final List<String> actualCommandLine = captor.getValue();

        // Assert
        Assert.assertEquals(
                "The command line that was executed for the 'external report' executor is not as expected",
                createExpectedCommand(baseCommandArguments), actualCommandLine);
    }

    @Test
    public void execute_runtimeProcessThrowsException_shouldEndQuietly() throws IOException {
        //Arrange
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        ExternalReportCommandArguments externalReportArguments = createExternalReportArguments();
        ExternalReportCommandExecutor externalReportExecutor = new ExternalReportCommandExecutor(nullLogger,
         baseCommandArguments, externalReportArguments);

        ProcessExecutor execMock = mock(ProcessExecutor.class);
        try {
            when(execMock.execute()).thenThrow(new IOException());

            //Act
            externalReportExecutor.setProcessExecutor(execMock);
            boolean result = externalReportExecutor.execute();
            Assert.assertFalse("externalReportExecutor.execute() should be false!", result);
        } catch (Exception e) {
            Assert.fail("externalReportExecutor.execute() should not throw exception!");
        }
    }

    private ExternalReportCommandArguments createExternalReportArguments() {
        ExternalReportCommandArguments externalReportArguments = new ExternalReportCommandArguments(
                "fake-report"
        );

        return externalReportArguments;
    }

    private BaseCommandArguments createBaseCommandArguments() {
        BaseCommandArguments baseCommandArguments = new BaseCommandArguments();
        baseCommandArguments.setJavaPath("path/to/java");
        baseCommandArguments.setAgentPath("agent.jar");
        baseCommandArguments.setToken("fake-token");
        baseCommandArguments.setEnvVars(new EnvVars());
        baseCommandArguments.setLogConfiguration(new LogConfiguration());
        return baseCommandArguments;
    }

    private List<String> createExpectedCommand(BaseCommandArguments baseCommandArguments) {
        List<String> expected = new ArrayList<>();
        expected.add("path/to/java");
        expected.add(AbstractCommandExecutor.formatTagProp());
        expected.addAll(baseCommandArguments.getLogConfiguration().toSystemProperties());
        expected.add("-jar");
        expected.add("agent.jar");
        expected.add("externalReport");
        expected.add("-token");
        expected.add("fake-token");
        expected.add("-report");
        expected.add("fake-report");
        return expected;
    }
}
