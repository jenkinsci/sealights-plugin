package io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies;

import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;

public class DotNetOptions extends TechnologyOptions {
    private String namespaacesIncluded;
    private String namespaacesExcluded;

    @DataBoundConstructor
    public DotNetOptions(String namespaacesIncluded, String namespaacesExcluded) {
        this.namespaacesIncluded = namespaacesIncluded;
        this.namespaacesExcluded = namespaacesExcluded;
    }

    public String getNamespaacesIncluded() {
        return namespaacesIncluded;
    }

    public void setNamespaacesIncluded(String namespaacesIncluded) {
        this.namespaacesIncluded = namespaacesIncluded;
    }

    public String getNamespaacesExcluded() {
        return namespaacesExcluded;
    }

    public void setNamespaacesExcluded(String namespaacesExcluded) {
        this.namespaacesExcluded = namespaacesExcluded;
    }

    @Extension
    public static class DescriptorImpl extends TechnologyOptionsDescriptor {
        @Override
        public String getDisplayName() {
            return "DotNet Options";
        }
    }
}
