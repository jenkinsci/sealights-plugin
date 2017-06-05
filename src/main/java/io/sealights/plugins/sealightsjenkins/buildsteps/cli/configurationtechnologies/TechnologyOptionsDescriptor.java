package io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies;

import hudson.DescriptorExtensionList;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

/**
 * Created by shahar on 5/18/2017.
 */
public abstract class TechnologyOptionsDescriptor extends Descriptor<TechnologyOptions> {

    public boolean isApplicable(Class<? extends TechnologyOptions> type) {
        return true;
    }

    public static DescriptorExtensionList<TechnologyOptions, TechnologyOptionsDescriptor> all() {
        return Jenkins.getInstance().getDescriptorList(TechnologyOptions.class);
    }
}
