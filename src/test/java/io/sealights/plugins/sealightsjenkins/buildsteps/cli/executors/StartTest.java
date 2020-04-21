package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import hudson.EnvVars;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.LogConfiguration;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.StartCommandArguments;
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

/**
 * Created by shahar on 2/5/2017.
 */
public class StartTest {

    private Logger nullLogger = new NullLogger();

    @Test
    public void execute_giveValidStartArguments_shouldExecuteCorrectCommand() throws IOException {
        //Arrange
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        StartCommandArguments startArguments = new StartCommandArguments("newEnv");
        StartCommandExecutor startExecutor = new StartCommandExecutor(nullLogger, baseCommandArguments, startArguments);

        ProcessExecutor execMock = mock(ProcessExecutor.class);

        final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        //Act
        startExecutor.setProcessExecutor(execMock);
        startExecutor.execute();
        verify(execMock).command(captor.capture());
        final List<String> actualCommandLine = captor.getValue();

        // Assert
        Assert.assertEquals(
                "The command line that was executed for the 'start' executor is not as expected",
                createExpectedCommand(baseCommandArguments), actualCommandLine);
    }

    @Test
    public void execute_runtimeProcessThrowsException_shouldEndQuietly() throws IOException {
        //Arrange
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        StartCommandArguments startArguments = new StartCommandArguments("newEnv");
        StartCommandExecutor startExecutor = new StartCommandExecutor(nullLogger, baseCommandArguments, startArguments);

        ProcessExecutor execMock = mock(ProcessExecutor.class);
        try {
            when(execMock.execute()).thenThrow(new IOException());

            //Act
            startExecutor.setProcessExecutor(execMock);
            boolean result = startExecutor.execute();
            Assert.assertFalse("startExecutor.execute() should be false!", result);
        } catch (Exception e) {
            Assert.fail("startExecutor.execute() should not throw exception!");
        }
    }

    private List<String> createExpectedCommand(BaseCommandArguments baseCommandArguments) {
        List<String> expected = new ArrayList<>();
        expected.add("path/to/java");
        expected.add(AbstractCommandExecutor.formatTagProp());
        expected.addAll(baseCommandArguments.getLogConfiguration().toSystemProperties());
        expected.add("-jar");
        expected.add("agent.jar");
        expected.add("start");
        expected.add("-token");
        expected.add("fake-token");
        expected.add("-buildsessionidfile");
        expected.add("/path/to/buildsessionid.txt");
        expected.add("-appname");
        expected.add("demoApp");
        expected.add("-buildname");
        expected.add("1");
        expected.add("-branchname");
        expected.add("branchy");
        expected.add("-labid");
        expected.add("someEnv");
        expected.add("-testStage");
        expected.add("newEnv");
        return expected;
    }
    private BaseCommandArguments createBaseCommandArguments() {
        BaseCommandArguments baseCommandArguments = new BaseCommandArguments();
        baseCommandArguments.setJavaPath("path/to/java");
        baseCommandArguments.setAgentPath("agent.jar");
        baseCommandArguments.setToken("fake-token");
        baseCommandArguments.setAppName("demoApp");
        baseCommandArguments.setBuildName("1");
        baseCommandArguments.setBranchName("branchy");
        baseCommandArguments.setBuildSessionIdFile("/path/to/buildsessionid.txt");
        baseCommandArguments.setLabId("someEnv");
        baseCommandArguments.setEnvVars(new EnvVars());
        baseCommandArguments.setLogConfiguration(new LogConfiguration());
        return baseCommandArguments;
    }

}
