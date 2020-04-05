package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import hudson.model.Saveable;
import hudson.util.DescribableList;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.JavaOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptionsDescriptor;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.PrConfigCommandArguments;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
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
        String[] executionCommand = executor.createExecutionCommand();
        List<String> commandAsList = Arrays.asList(executionCommand);

        //Assert
        String actualLatestCommit = findValueInArray(commandAsList, "-latestCommit");
        String actualPullRequestNumber = findValueInArray(commandAsList, "-pullRequestNumber");
        String actualRepoUrl = findValueInArray(commandAsList, "-repoUrl");
        String actualTargetBranch = findValueInArray(commandAsList, "-targetBranch");
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
        String[] executionCommand = executor.createExecutionCommand();
        List<String> commandAsList = Arrays.asList(executionCommand);

        // Assert
        String actualIncluded = findValueInArray(commandAsList, "-packagesincluded");
        String actualExcluded = findValueInArray(commandAsList, "-packagesexcluded");
        Assert.assertEquals(packagesIncluded, actualIncluded);
        Assert.assertEquals(packagesExcluded, actualExcluded);

    }

    @Test
    public void execute_hasProxy_shouldAdd() throws IOException {
        //Arrange
        String proxy = "http://localhost:8888";
        BaseCommandArguments baseCommandArguments = new BaseCommandArguments();
        baseCommandArguments.setProxy(proxy);
        PrConfigCommandExecutor executor = new PrConfigCommandExecutor(nullLogger,
                baseCommandArguments, createPrConfigArguments(techOptions));


        //Act
        String[] executionCommand = executor.createExecutionCommand();
        List<String> commandAsList = Arrays.asList(executionCommand);

        // Assert
        String actualProxy = findValueInArray(commandAsList, "-proxy");
        Assert.assertEquals(proxy, actualProxy);
    }

    private PrConfigCommandExecutor createTestedExecutor(DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> technologyOptions) throws IOException {
        BaseCommandArguments baseCommandArguments = createBaseCommandArguments();
        PrConfigCommandArguments prConfigCommandArguments = createPrConfigArguments(technologyOptions);
        PrConfigCommandExecutor executor = new PrConfigCommandExecutor(nullLogger, baseCommandArguments,
                prConfigCommandArguments);
        executor.setJenkinsUtils(createMockJenkinsUtils());
        Runtime runtimeMock = mock(Runtime.class);

        //Act
        executor.setRuntime(runtimeMock);
        return executor;
    }

    private PrConfigCommandArguments createPrConfigArguments(DescribableList<TechnologyOptions,
     TechnologyOptionsDescriptor> technologyOptions) {
        PrConfigCommandArguments configArguments = new PrConfigCommandArguments(technologyOptions, LATEST_COMMIT,
                PULL_REQUEST_NUMBER, REPO_URL, TARGET_BRANCH);

        return configArguments;
    }

    private String findValueInArray(List<String> list, String key){
        int index = list.indexOf(key);
        if(index == -1){
            return null;
        }
        return list.get(index + 1);
    }

}
