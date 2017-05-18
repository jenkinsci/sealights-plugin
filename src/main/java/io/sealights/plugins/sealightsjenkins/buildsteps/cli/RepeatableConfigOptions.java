package io.sealights.plugins.sealightsjenkins.buildsteps.cli;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created by shahar on 5/18/2017.
 */
public class RepeatableConfigOptions extends Builder {

    private ConfigTechnologyOptions configTechnologyOptions;

    @DataBoundConstructor
    public RepeatableConfigOptions(ConfigTechnologyOptions configTechnologyOptions) {
        this.configTechnologyOptions = configTechnologyOptions;
    }

    public ConfigTechnologyOptions getConfigTechnologyOptions() {
        return configTechnologyOptions;
    }

    public void setConfigTechnologyOptions(ConfigTechnologyOptions configTechnologyOptions) {
        this.configTechnologyOptions = configTechnologyOptions;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public DescriptorImpl() {
            load();
        }

        public DescriptorExtensionList<ConfigTechnologyOptions, ConfigTechnologyOptions.ConfigTechnologyOptionsDescriptor> getConfigTechnologyOptionsDescriptorList() {
            System.out.println("");
            return Jenkins.getInstance().getDescriptorList(ConfigTechnologyOptions.class);
        }

        public void getMagic(){
            System.out.println("");
        }

        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return false;
        }

        public String getDisplayName() {
            return "";
        }
    }
}

