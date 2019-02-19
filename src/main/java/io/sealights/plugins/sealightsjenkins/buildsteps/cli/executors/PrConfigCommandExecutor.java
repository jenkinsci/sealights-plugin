package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.PrConfigCommandArguments;
import io.sealights.plugins.sealightsjenkins.utils.Logger;

import java.util.List;

/**
 * Executes the build scanner with prConfig command, extends from ConfigCommandExecutor
 */
public class PrConfigCommandExecutor extends ConfigCommandExecutor{
    private PrConfigCommandArguments prConfigCommandArguments;
    public PrConfigCommandExecutor(Logger logger, BaseCommandArguments baseCommandArguments,
     PrConfigCommandArguments prConfigCommandArguments) {
        super(logger, baseCommandArguments, prConfigCommandArguments);
        this.prConfigCommandArguments = prConfigCommandArguments;
    }

    @Override
    protected String getCommandName() {
        return "-prConfig";
    }

    @Override
    protected void addBaseArgumentsLine(List<String> commandsList) {
        if (baseArgs.getTokenData() != null) {
            addArgumentKeyVal("token", baseArgs.getTokenData().getToken(), commandsList);
        } else {
            addArgumentKeyVal("tokenfile", baseArgs.getTokenFile(), commandsList);
        }
        addArgumentKeyVal("appname", baseArgs.getAppName(), commandsList);
        addArgumentKeyVal("latestCommit", prConfigCommandArguments.getLatestCommit(), commandsList);
        addArgumentKeyVal("pullRequestNumber", prConfigCommandArguments.getPullRequestNumber(), commandsList);
        addArgumentKeyVal("repoUrl", prConfigCommandArguments.getRepoUrl(), commandsList);
        addArgumentKeyVal("targetBranch", prConfigCommandArguments.getTargetBranch(), commandsList);

    }

    @Override
    public void addAdditionalArguments(List<String> commandsList) {
        if(prConfigCommandArguments.getTechOptions().size() > 0){
           addPackagesIncluded(commandsList);
        }
        addArgumentKeyVal("buildsessionidfile", this.buildSessionIdFileOnMaster, commandsList);
        commandsList.add("-enableNoneZeroErrorCode");
    }
}
