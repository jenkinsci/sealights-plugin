package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import hudson.model.Saveable;
import hudson.util.DescribableList;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.LogConfiguration;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.JavaOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptionsDescriptor;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.PrConfigCommandArguments;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.mock;

public class PrConfigTest extends ConfigTest {
    private final String LATEST_COMMIT = "LATEST_COMMIT";
    private final String PULL_REQUEST_NUMBER = "1";
    private final String REPO_URL = "http://repo.url";
    private final String TARGET_BRANCH = "master";
    private DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> techOptions;

    @Before
    public void initTechOptions(){
        techOptions = new DescribableList<>(Saveable.NOOP);
    }

    @Test
    public void execute_noTechOptions_shouldNotAdd() throws IOException {
        //Arrange
        PrConfigCommandExecutor executor = createTestedExecutor(techOptions);

        //Act
        List<String> executionCommand = executor.createExecutionCommand();

        //Assert
        String actualLatestCommit = findValueInList(executionCommand, "-latestCommit");
        String actualPullRequestNumber = findValueInList(executionCommand, "-pullRequestNumber");
        String actualRepoUrl = findValueInList(executionCommand, "-repoUrl");
        String actualTargetBranch = findValueInList(executionCommand, "-targetBranch");
        Assert.assertEquals(LATEST_COMMIT, actualLatestCommit);
        Assert.assertEquals(PULL_REQUEST_NUMBER, actualPullRequestNumber);
        Assert.assertEquals(REPO_URL, actualRepoUrl);
        Assert.assertEquals(TARGET_BRANCH, actualTargetBranch);

    }

    @Test
    public void execute_hasTechOptions_shouldAdd() throws IOException {
        //Arrange
        String packagesIncluded = "io.include.*";
        String packagesExcluded = "io.exclude.*";
        techOptions.add(new JavaOptions(packagesIncluded, packagesExcluded));
        PrConfigCommandExecutor executor = createTestedExecutor(techOptions);

        //Act
        List<String> executionCommand = executor.createExecutionCommand();

        // Assert
        String actualIncluded = findValueInList(executionCommand, "-packagesincluded");
        String actualExcluded = findValueInList(executionCommand, "-packagesexcluded");
        Assert.assertEquals(packagesIncluded, actualIncluded);
        Assert.assertEquals(packagesExcluded, actualExcluded);

    }

    @Test
    public void execute_hasProxy_shouldAdd() throws IOException {
        //Arrange
        String proxy = "http://localhost:8888";
        BaseCommandArguments baseCommandArguments = new BaseCommandArguments();
        baseCommandArguments.setProxy(proxy);
        baseCommandArguments.setLogConfiguration(new LogConfiguration());
        PrConfigCommandExecutor executor = new PrConfigCommandExecutor(nullLogger,
                baseCommandArguments, createPrConfigArguments(techOptions));


        //Act
        List<String> executionCommand = executor.createExecutionCommand();

        // Assert
        String actualProxy = findValueInList(executionCommand, "-proxy");
        Assert.assertEquals(proxy, actualProxy);
    }

    private PrConfigCommandExecutor createTestedExecutor(DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> technologyOptions) throws IOException {
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        PrConfigCommandArguments prConfigCommandArguments = createPrConfigArguments(technologyOptions);
        PrConfigCommandExecutor executor = new PrConfigCommandExecutor(nullLogger, baseCommandArguments,
                prConfigCommandArguments);
        executor.setJenkinsUtils(createMockJenkinsUtils());
        ProcessExecutor execMock = mock(ProcessExecutor.class);

        //Act
        executor.setProcessExecutor(execMock);
        return executor;
    }

    private PrConfigCommandArguments createPrConfigArguments(DescribableList<TechnologyOptions,
     TechnologyOptionsDescriptor> technologyOptions) {
        PrConfigCommandArguments configArguments = new PrConfigCommandArguments(technologyOptions, LATEST_COMMIT,
                PULL_REQUEST_NUMBER, REPO_URL, TARGET_BRANCH);

        return configArguments;
    }

    private String findValueInList(List<String> list, String key){
        int index = list.indexOf(key);
        if(index == -1){
            return null;
        }
        return list.get(index + 1);
    }

}
