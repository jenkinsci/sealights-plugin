package io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities;

import hudson.util.DescribableList;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.CommandMode;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptions;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies.TechnologyOptionsDescriptor;

/**
 * Created by shahar on 12/26/2016.
 */
public class ConfigCommandArguments extends AbstractCommandArgument {
    private DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> techOptions ;

    public ConfigCommandArguments(DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> techOptions ) {
        this.techOptions =techOptions;
    }

    public DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> getTechOptions() {
        return techOptions;
    }

    public void setTechOptions(DescribableList<TechnologyOptions, TechnologyOptionsDescriptor> techOptions) {
        this.techOptions = techOptions;
    }

    @Override
    public CommandModes getMode() {
        return CommandModes.Config;
    }
}
