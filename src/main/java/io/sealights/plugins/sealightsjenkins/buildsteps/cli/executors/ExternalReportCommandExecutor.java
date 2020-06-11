package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.ExternalReportCommandArguments;
import io.sealights.plugins.sealightsjenkins.utils.JenkinsUtils;
import io.sealights.plugins.sealightsjenkins.utils.Logger;

import java.util.List;

/**
 * Executor for the 'externalReport' command.
 */
public class ExternalReportCommandExecutor extends AbstractCommandExecutor {
    public static final String DEPRECATION_MESSAGE = "externalReport step is deprecated, this data is not supported " +
     "anymore. will be removed soon";

    private ExternalReportCommandArguments externalReportArguments;

    public ExternalReportCommandExecutor(
            Logger logger, BaseCommandArguments baseCommandArguments, ExternalReportCommandArguments externalReportArguments) {
        super(logger, baseCommandArguments);
        this.externalReportArguments = externalReportArguments;
    }

    @Override
    public void addAdditionalArguments(List<String> commandsList) {
    }

    @Override
    protected String getCommandName() {
        return "externalReport";
    }

    @Override
    public boolean execute() {
        logger.info(DEPRECATION_MESSAGE);
        return true;
    }
}
