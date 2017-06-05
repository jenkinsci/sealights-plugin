package io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies;
/*
* This class represents the 'Java' item in 'TechnologyOptions' DropDown under 'Config' option
* */
import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;

public class JavaOptions extends TechnologyOptions{

    private String packagesIncluded;
    private String packagesExcluded;

    @DataBoundConstructor
    public JavaOptions(String packagesIncluded, String packagesExcluded) {
        this.packagesIncluded = packagesIncluded;
        this.packagesExcluded = packagesExcluded;
    }

    public String getPackagesIncluded() {
        return packagesIncluded;
    }

    public void setPackagesIncluded(String packagesIncluded) {
        this.packagesIncluded = packagesIncluded;
    }

    public String getPackagesExcluded() {
        return packagesExcluded;
    }

    public void setPackagesExcluded(String packagesExcluded) {
        this.packagesExcluded = packagesExcluded;
    }

    @Extension
    public static class DescriptorImpl extends TechnologyOptionsDescriptor {
        @Override
        public String getDisplayName() {
            return "Java Options";
        }
    }

}
