package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.util.DescribableList;
import io.sealights.plugins.sealightsjenkins.TestHelper;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.LogConfiguration;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.JavaOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptionsDescriptor;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.ConfigCommandArguments;
import io.sealights.plugins.sealightsjenkins.utils.JenkinsUtils;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.NullLogger;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by shahar on 2/5/2017.
 */
public class ConfigTest {

    private TestHelper testHelper = new TestHelper();
    protected Logger nullLogger = new NullLogger();

    @Test
    public void execute_giveValidConfigArguments_shouldExecuteCorrectCommand() throws IOException {
        //Arrange
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        ConfigCommandArguments configArguments = createConfigArguments();
        ConfigCommandExecutor configExecutor = new ConfigCommandExecutor(nullLogger, baseCommandArguments, configArguments);
        configExecutor.setJenkinsUtils(createMockJenkinsUtils());

        ProcessExecutor execMock = mock(ProcessExecutor.class);

        final ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        //Act
        configExecutor.setProcessExecutor(execMock);
        configExecutor.execute();
        verify(execMock).command(captor.capture());
        final List<String > actualCommandLine = captor.getValue();

        // Assert
        Assert.assertEquals(
                "The command line that was executed for the 'start' executor is not as expected",
                createExpectedCommand(baseCommandArguments), actualCommandLine);
    }

    protected JenkinsUtils createMockJenkinsUtils() {
        JenkinsUtils jenkinsUtilsMock = mock(JenkinsUtils.class);
        when(jenkinsUtilsMock.getWorkspace((AbstractBuild<?, ?>) any(Object.class))).thenReturn("/path/to/workspace");
        return jenkinsUtilsMock;
    }

    @Test
    public void execute_runtimeProcessThrowsException_shouldEndQuietly() throws IOException {
        //Arrange
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        ConfigCommandArguments configArguments = createConfigArguments();
        ConfigCommandExecutor configExecutor = new ConfigCommandExecutor(nullLogger, baseCommandArguments,
         configArguments);

        ProcessExecutor execMock = mock(ProcessExecutor.class);
        try {
            when(execMock.execute()).thenThrow(new IOException());

            //Act
            configExecutor.setProcessExecutor(execMock);
            boolean result = configExecutor.execute();
            Assert.assertFalse("configExecutor.execute() should be false!", result);
        } catch (Exception e) {
            Assert.fail("configExecutor.execute() should not throw exception!");
        }
    }

    private ConfigCommandArguments createConfigArguments() {
        DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> technologyOptions = new DescribableList<>(JavaOptions.DescriptorImpl.NOOP);
        technologyOptions.add(new JavaOptions("io.include.*", "io.exclude.*"));
        ConfigCommandArguments configArguments = new ConfigCommandArguments(technologyOptions);

        return configArguments;
    }

    protected BaseCommandArguments createBaseCommandArguments() throws IOException {
        BaseCommandArguments baseCommandArguments = new BaseCommandArguments();
        baseCommandArguments.setJavaPath("path/to/java");
        baseCommandArguments.setAgentPath("agent.jar");
        baseCommandArguments.setToken("fake-token");
        baseCommandArguments.setAppName("demoApp");
        baseCommandArguments.setBuildName("1");
        baseCommandArguments.setBranchName("branchy");
        baseCommandArguments.setEnvVars(new EnvVars());
        baseCommandArguments.setLogConfiguration(new LogConfiguration());
        TestHelper.BuildMock build = testHelper.createBuildMock();
        baseCommandArguments.setBuild(build);

        return baseCommandArguments;
    }

    private List<String> createExpectedCommand(BaseCommandArguments baseCommandArguments) {
        List<String> expected = new ArrayList<>();
        expected.add("path/to/java");
        expected.add(AbstractCommandExecutor.formatTagProp());
        expected.addAll(baseCommandArguments.getLogConfiguration().toSystemProperties());
        expected.add("-jar");
        expected.add("agent.jar");
        expected.add("-config");
        expected.add("-token");
        expected.add("fake-token");
        expected.add("-appname");
        expected.add("demoApp");
        expected.add("-buildname");
        expected.add("1");
        expected.add("-branchname");
        expected.add("branchy");
        expected.add("-packagesincluded");
        expected.add("io.include.*");
        expected.add("-packagesexcluded");
        expected.add("io.exclude.*");
        expected.add("-buildsessionidfile");
        expected.add("/path/to/workspace" + File.separator + "buildSessionId.txt");
        expected.add("-enableNoneZeroErrorCode");
        return expected;
    }
}
