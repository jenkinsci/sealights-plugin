package io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities;

/**
 * Arguments for the 'externalReport' command.
 */
public class ExternalReportCommandArguments extends AbstractCommandArgument {

    private String report;

    public ExternalReportCommandArguments() {
    }

    public String getReport() {
        return report;
    }

    @Override
    public CommandModes getMode() {
        return CommandModes.ExternalReport;
    }
}
