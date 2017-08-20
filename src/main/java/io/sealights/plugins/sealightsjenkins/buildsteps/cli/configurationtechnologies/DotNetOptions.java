package io.sealights.plugins.sealightsjenkins.buildsteps.cli.configurationtechnologies;
/*
* This class represents the 'DotNet' item in 'TechnologyOptions' DropDown under 'Config' option.
 * currently not in used. the 'extends' to hide it from ui.
* */
import hudson.Extension;
import hudson.model.AbstractProject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;

public class DotNetOptions extends TechnologyOptions{

    private String namespacesIncluded;
    private String namespacesExcluded;
    private String includedFilePatterns;
    private String excludedFilePatterns;

    @DataBoundConstructor
    public DotNetOptions(String namespacesIncluded, String namespacesExcluded, String includedFilePatterns, String excludedFilePatterns) {
        this.namespacesIncluded = namespacesIncluded;
        this.namespacesExcluded = namespacesExcluded;
        this.includedFilePatterns = includedFilePatterns;
        this.excludedFilePatterns = excludedFilePatterns;
    }

    public String getNamespacesIncluded() {
        return namespacesIncluded;
    }

    public void setNamespacesIncluded(String namespacesIncluded) {
        this.namespacesIncluded = namespacesIncluded;
    }

    public String getNamespacesExcluded() {
        return namespacesExcluded;
    }

    public void setNamespacesExcluded(String namespacesExcluded) {
        this.namespacesExcluded = namespacesExcluded;
    }

    public String getIncludedFilePatterns() {
        return includedFilePatterns;
    }

    public void setIncludedFilePatterns(String includedFilePatterns) {
        this.includedFilePatterns = includedFilePatterns;
    }

    public String getExcludedFilePatterns() {
        return excludedFilePatterns;
    }

    public void setExcludedFilePatterns(String excludedFilePatterns) {
        this.excludedFilePatterns = excludedFilePatterns;
    }

    @Extension
    public static class DescriptorImpl extends TechnologyOptionsDescriptor {
        @Override
        public String getDisplayName() {
            return "DotNet options";
        }
    }

}
