package io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities;

import hudson.EnvVars;
import hudson.util.DescribableList;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptionsDescriptor;
import io.sealights.plugins.sealightsjenkins.utils.JenkinsUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * Extra arguments for build scanner that relevant to PR buildSessionId creation.
 */
public class PrConfigCommandArguments extends ConfigCommandArguments {
    @Getter @Setter private String latestCommit;
    @Getter @Setter private String pullRequestNumber;
    @Getter @Setter private String repoUrl;
    @Getter @Setter private String targetBranch;

    public PrConfigCommandArguments(DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> techOptions,
     String latestCommit, String pullRequestNumber, String repoUrl, String targetBranch) {
        super(techOptions);
        this.latestCommit = latestCommit;
        this.pullRequestNumber = pullRequestNumber;
        this.repoUrl = repoUrl;
        this.targetBranch = targetBranch;
    }

    @Override
    public CommandModes getMode() {
        return CommandModes.PrConfig;
    }

    /**
     * Resolves raw args data from env vars (if exists)
     * //TODO: Consider do it once for all args in AbstractCommandExecutor.addArgumentKeyVal()
     * @param envVars
     */
    public void resolveValuesFromEnvVars(EnvVars envVars){
        latestCommit = JenkinsUtils.resolveEnvVarsInString(envVars, latestCommit);
        pullRequestNumber = JenkinsUtils.resolveEnvVarsInString(envVars, pullRequestNumber);
        repoUrl = JenkinsUtils.resolveEnvVarsInString(envVars, repoUrl);
        targetBranch = JenkinsUtils.resolveEnvVarsInString(envVars, targetBranch);
    }
}
