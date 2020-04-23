package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import io.sealights.plugins.sealightsjenkins.buildsteps.cli.LogConfiguration;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.EndCommandArguments;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.NullLogger;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by shahar on 2/5/2017.
 */
public class EndTest {

    private Logger nullLogger = new NullLogger();

    @Test
    public void execute_giveValidEndArguments_shouldExecuteCorrectCommand() throws IOException {
        //Arrange
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        EndCommandArguments endArguments = new EndCommandArguments();
        EndCommandExecutor endExecutor = new EndCommandExecutor(nullLogger, baseCommandArguments, endArguments);

        ProcessExecutor executorMock = mock(ProcessExecutor.class);

        final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        //Act
        endExecutor.setProcessExecutor(executorMock);
        endExecutor.execute();
        verify(executorMock).command(captor.capture());
        final List<String> actualCommandLine = captor.getValue();

        // Assert
        Assert.assertEquals(
                "The command line that was executed for the 'end' executor is not as expected",
                createExpectedCommand(baseCommandArguments), actualCommandLine);
    }

    @Test
    public void execute_runtimeProcessThrowsException_shouldEndQuietly() throws IOException {
        //Arrange
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        EndCommandArguments endArguments = new EndCommandArguments();
        EndCommandExecutor endExecutor = new EndCommandExecutor(nullLogger, baseCommandArguments, endArguments);

        ProcessExecutor execMock = mock(ProcessExecutor.class);
        try {
            when(execMock.execute()).thenThrow(new IOException());

            //Act
            endExecutor.setProcessExecutor(execMock);
            boolean result = endExecutor.execute();
            Assert.assertFalse("endExecutor.execute() should be false!", result);
        } catch (Exception e) {
            Assert.fail("endExecutor.execute() should not throw exception!");
        }
    }

    private List<String> createExpectedCommand(BaseCommandArguments baseCommandArguments) {
        List<String> expected = new ArrayList<>();
        expected.add("path/to/java");
        expected.add(AbstractCommandExecutor.formatTagProp());
        expected.addAll(baseCommandArguments.getLogConfiguration().toSystemProperties());
        expected.add("-jar");
        expected.add("agent.jar");
        expected.add("end");
        expected.add("-token");
        expected.add("fake-token");
        expected.add("-buildsessionidfile");
        expected.add("/path/to/buildsessionid.txt");
        expected.add("-labid");
        expected.add("someEnv");
        return expected;
    }

    private BaseCommandArguments createBaseCommandArguments() {
        BaseCommandArguments baseCommandArguments = new BaseCommandArguments();
        baseCommandArguments.setJavaPath("path/to/java");
        baseCommandArguments.setAgentPath("agent.jar");
        baseCommandArguments.setToken("fake-token");
        baseCommandArguments.setBuildSessionIdFile("/path/to/buildsessionid.txt");
        baseCommandArguments.setLabId("someEnv");
        baseCommandArguments.setLogConfiguration(new LogConfiguration());
        return baseCommandArguments;
    }

}
