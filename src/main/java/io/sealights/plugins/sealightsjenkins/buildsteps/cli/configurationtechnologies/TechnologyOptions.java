package io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies;

import hudson.model.AbstractDescribableImpl;

/**
 * Created by shahar on 5/18/2017.
 */
public abstract class TechnologyOptions extends AbstractDescribableImpl<TechnologyOptions> {

    @Override
    public TechnologyOptionsDescriptor getDescriptor() {
        return (TechnologyOptionsDescriptor) super.getDescriptor();
    }

}
