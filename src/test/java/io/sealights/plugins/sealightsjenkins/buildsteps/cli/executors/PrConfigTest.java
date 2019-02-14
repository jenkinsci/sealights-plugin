package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import hudson.model.Saveable;
import hudson.util.DescribableList;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.JavaOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptionsDescriptor;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.PrConfigCommandArguments;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

public class PrConfigTest extends ConfigTest {
    private String latestCommit = "latestCommit";
    private String pullRequestNumber = "1";
    private String repoUrl = "http://repo.url";
    private String targetBranch = "master";

    @Test
    public void execute_noTechOptions_shouldNotAdd() throws IOException {
        //Arrange
        PrConfigCommandExecutor executor = createTestedExecutor(new DescribableList<TechnologyOptions,
         TechnologyOptionsDescriptor>(Saveable.NOOP));

        //Act
        String[] executionCommand = executor.createExecutionCommand();
        List<String> commandAsList = Arrays.asList(executionCommand);

        //Assert
        String actualLatestCommit = findValueInArray(commandAsList, "-latestCommit");
        String actualPullRequestNumber = findValueInArray(commandAsList, "-pullRequestNumber");
        String actualRepoUrl = findValueInArray(commandAsList, "-repoUrl");
        String actualTargetBranch = findValueInArray(commandAsList, "-targetBranch");
        Assert.assertEquals(latestCommit, actualLatestCommit);
        Assert.assertEquals(pullRequestNumber, actualPullRequestNumber);
        Assert.assertEquals(repoUrl, actualRepoUrl);
        Assert.assertEquals(targetBranch, actualTargetBranch);

    }

    @Test
    public void execute_hasTechOptions_shouldAdd() throws IOException {
        //Arrange
        String packagesIncluded = "io.include.*";
        String packagesExcluded = "io.exclude.*";
        DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> technologyOptions = new DescribableList<>(JavaOptions.DescriptorImpl.NOOP);
        technologyOptions.add(new JavaOptions(packagesIncluded, packagesExcluded));
        PrConfigCommandExecutor executor = createTestedExecutor(technologyOptions);

        //Act
        String[] executionCommand = executor.createExecutionCommand();
        List<String> commandAsList = Arrays.asList(executionCommand);

        // Assert
        String actualIncluded = findValueInArray(commandAsList, "-packagesincluded");
        String actualExcluded = findValueInArray(commandAsList, "-packagesexcluded");
        Assert.assertEquals(packagesIncluded, actualIncluded);
        Assert.assertEquals(packagesExcluded, actualExcluded);

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
        PrConfigCommandArguments configArguments = new PrConfigCommandArguments(technologyOptions, latestCommit,
                pullRequestNumber, repoUrl, targetBranch);

        return configArguments;
    }

    private DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> createTechnologyOptions() {
        DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> technologyOptions =
         new DescribableList<>(JavaOptions.DescriptorImpl.NOOP);
        technologyOptions.add(new JavaOptions("io.include.*", "io.exclude.*"));
        return technologyOptions;
    }

    private String findValueInArray(List<String> list, String key){
        int index = list.indexOf(key);
        if(index == -1){
            return null;
        }
        return list.get(index + 1);
    }

}
