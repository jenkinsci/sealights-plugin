package io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities;

import hudson.util.DescribableList;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptionsDescriptor;

public class PrConfigCommandArguments extends ConfigCommandArguments {
    private String latestCommit;
    private String pullRequestNumber;
    private String repoUrl;
    private String targetBranch;

    public PrConfigCommandArguments(DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> techOptions,
     String latestCommit, String pullRequestNumber, String repoUrl, String targetBranch) {
        super(techOptions);
        this.latestCommit = latestCommit;
        this.pullRequestNumber = pullRequestNumber;
        this.repoUrl = repoUrl;
        this.targetBranch = targetBranch;
    }

    public String getLatestCommit() {
        return latestCommit;
    }

    public void setLatestCommit(String latestCommit) {
        this.latestCommit = latestCommit;
    }

    public String getPullRequestNumber() {
        return pullRequestNumber;
    }

    public void setPullRequestNumber(String pullRequestNumber) {
        this.pullRequestNumber = pullRequestNumber;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

    @Override
    public CommandModes getMode() {
        return CommandModes.PrConfig;
    }
}
