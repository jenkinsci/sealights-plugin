package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.util.DescribableList;
import io.sealights.plugins.sealightsjenkins.TestHelper;
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

import java.io.File;
import java.io.IOException;

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

        Runtime runtimeMock = mock(Runtime.class);

        final ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);

        //Act
        configExecutor.setRuntime(runtimeMock);
        configExecutor.execute();
        verify(runtimeMock).exec(captor.capture());
        final String[] actualCommandLine = captor.getValue();
        String[] expectedCommandLine = {"path/to/java", AbstractCommandExecutor.formatTagProp(), "-jar", "agent.jar",
        "-config", "-token", "fake-token", "-buildsessionidfile", "/path/to/buildsessionid.txt", "-appname", "demoApp", "-buildname", "1", "-branchname", "branchy", "-packagesincluded", "io.include.*", "-packagesexcluded", "io.exclude.*", "-buildsessionidfile", "/path/to/workspace" + File.separator + "buildSessionId.txt", "-enableNoneZeroErrorCode"};

        // Assert
        Assert.assertArrayEquals(
                "The command line that was executed for the 'start' executor is not as expected",
                expectedCommandLine, actualCommandLine);
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
        ConfigCommandExecutor configExecutor = new ConfigCommandExecutor(nullLogger, baseCommandArguments, configArguments);

        Runtime runtimeMock = mock(Runtime.class);
        when(runtimeMock.exec(any(String.class))).thenThrow(new IOException());

        //Act
        configExecutor.setRuntime(runtimeMock);
        try {
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
        baseCommandArguments.setBuildSessionIdFile("/path/to/buildsessionid.txt");
        baseCommandArguments.setEnvVars(new EnvVars());

        TestHelper.BuildMock build = testHelper.createBuildMock();
        baseCommandArguments.setBuild(build);

        return baseCommandArguments;
    }
}
