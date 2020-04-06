package io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities;

import hudson.EnvVars;
import hudson.util.DescribableList;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class PrConfigCommandArgumentsTest {
    private DescribableList mockTechOption = mock(DescribableList.class);
    private String latestCommitValue = "sha1";
    private String pullRequestNumberValue = "1";
    private String targetBranchValue = "master";
    private String repoUrlValue = "http://repo.com/foo";
    private String latestCommitKey = "latest_commit";
    private String pullRequestNumberKey = "pr_number";
    private String targetBranchKey = "target_branch";
    private String repoUrlKey = "repo_url";

    @Test
    public void resolveValuesFromEnvVars_hasEnvVars_shouldResolveToRealValue() {
        PrConfigCommandArguments prConfigCommandArguments = new PrConfigCommandArguments(mockTechOption,
                createRawArg(latestCommitKey), createRawArg(pullRequestNumberKey), createRawArg(repoUrlKey),
                createRawArg(targetBranchKey));

        prConfigCommandArguments.resolveValuesFromEnvVars(createEnvVars());

        Assert.assertEquals(latestCommitValue, prConfigCommandArguments.getLatestCommit());
        Assert.assertEquals(repoUrlValue, prConfigCommandArguments.getRepoUrl());
        Assert.assertEquals(pullRequestNumberValue, prConfigCommandArguments.getPullRequestNumber());
        Assert.assertEquals(targetBranchValue, prConfigCommandArguments.getTargetBranch());
    }

    @Test
    public void resolveValuesFromEnvVars_hasNoEnvVars_shouldUseGivenValues() {
        String latestCommit = "sha2";
        String pullRequestNumber = "2";
        String targetBranch = "dev";
        String repoUrl = "http://repo.com/bar";
        PrConfigCommandArguments prConfigCommandArguments = new PrConfigCommandArguments(mockTechOption,
                latestCommit, pullRequestNumber, repoUrl, targetBranch);

        prConfigCommandArguments.resolveValuesFromEnvVars(createEnvVars());

        Assert.assertEquals(latestCommit, prConfigCommandArguments.getLatestCommit());
        Assert.assertEquals(repoUrl, prConfigCommandArguments.getRepoUrl());
        Assert.assertEquals(pullRequestNumber, prConfigCommandArguments.getPullRequestNumber());
        Assert.assertEquals(targetBranch, prConfigCommandArguments.getTargetBranch());
    }

    private String createRawArg(String key) {
        return "${" + key + "}";
    }

    private EnvVars createEnvVars(){
        EnvVars envVars = new EnvVars();
        envVars.put(latestCommitKey, latestCommitValue);
        envVars.put(pullRequestNumberKey, pullRequestNumberValue);
        envVars.put(targetBranchKey, targetBranchValue);
        envVars.put(repoUrlKey, repoUrlValue);
        return envVars;
    }
}
