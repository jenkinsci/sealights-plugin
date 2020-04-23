package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import hudson.EnvVars;
import io.sealights.plugins.sealightsjenkins.TestHelper;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.LogConfiguration;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.UploadReportsCommandArguments;
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
public class UploadsReportsTest {

    private TestHelper testHelper = new TestHelper();
    private Logger nullLogger = new NullLogger();
    private boolean NO_MORE_REQUESTS = false;

    @Test
    public void execute_giveValidUploadReportsArguments_shouldExecuteCorrectCommand() throws IOException {
        //Arrange
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        UploadReportsCommandArguments uploadReportsArguments =
                new UploadReportsCommandArguments("report1.txt,report2.txt", "folders", NO_MORE_REQUESTS, "someSource");
        UploadReportsCommandExecutor uploadReportsExecutor = new UploadReportsCommandExecutor(nullLogger, baseCommandArguments, uploadReportsArguments);

        ProcessExecutor execMock = mock(ProcessExecutor.class);

        final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        //Act
        uploadReportsExecutor.setProcessExecutor(execMock);
        uploadReportsExecutor.execute();
        verify(execMock).command(captor.capture());
        final List<String> actualCommandLine = captor.getValue();

        // Assert
        Assert.assertEquals(
                "The command line that was executed for the 'upload reports' executor is not as expected",
                createExpectedCommand(baseCommandArguments), actualCommandLine);
    }

    @Test
    public void execute_runtimeProcessThrowsException_shouldEndQuietly() throws IOException {
        //Arrange
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        UploadReportsCommandArguments uploadReportsArguments =
                new UploadReportsCommandArguments("report1.txt,report2.txt", "folders", NO_MORE_REQUESTS, "someSource");
        UploadReportsCommandExecutor uploadReportsExecutor = new UploadReportsCommandExecutor(nullLogger,
         baseCommandArguments, uploadReportsArguments);

        ProcessExecutor execMock = mock(ProcessExecutor.class);
        try {
            when(execMock.execute()).thenThrow(new IOException());

            //Act
            uploadReportsExecutor.setProcessExecutor(execMock);
            boolean result = uploadReportsExecutor.execute();
            Assert.assertFalse("uploadReportsExecutor.execute() should be false!", result);
        } catch (Exception e) {
            Assert.fail("uploadReportsExecutor.execute() should not throw exception!");
        }
    }

    private BaseCommandArguments createBaseCommandArguments() throws IOException {
        BaseCommandArguments baseCommandArguments = new BaseCommandArguments();
        baseCommandArguments.setJavaPath("path/to/java");
        baseCommandArguments.setAgentPath("agent.jar");
        baseCommandArguments.setToken("fake-token");
        baseCommandArguments.setBuildSessionIdFile("/path/to/buildsessionid.txt");
        baseCommandArguments.setLabId("someEnv");
        baseCommandArguments.setEnvVars(new EnvVars());
        TestHelper.BuildMock build = testHelper.createBuildMock();
        baseCommandArguments.setBuild(build);
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
        expected.add("uploadReports");
        expected.add("-token");
        expected.add("fake-token");
        expected.add("-buildsessionidfile");
        expected.add("/path/to/buildsessionid.txt");
        expected.add("-labid");
        expected.add("someEnv");
        expected.add("-reportFile");
        expected.add("report1.txt");
        expected.add("-reportFile");
        expected.add("report2.txt");
        expected.add("-reportFilesFolder");
        expected.add("folders");
        expected.add("-hasMoreRequests");
        expected.add("false");
        expected.add("-source");
        expected.add("someSource");
        return expected;
    }

}
