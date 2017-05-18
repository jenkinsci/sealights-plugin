package io.sealights.plugins.sealightsjenkins.buildsteps.cli;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.ConfigTechnologies;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;

/**
 * Created by shahar on 5/18/2017.
 */
public class ConfigTechnologyOptions implements Describable<ConfigTechnologyOptions>, ExtensionPoint, Serializable {


    @Override
    public Descriptor<ConfigTechnologyOptions> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }

    public static class ConfigTechnologyOptionsDescriptor extends Descriptor<ConfigTechnologyOptions> {

        private String selectedTechnology;

        protected ConfigTechnologyOptionsDescriptor(
                final Class<? extends ConfigTechnologyOptions> clazz, final String selectedTechnology) {
            super(clazz);
            this.selectedTechnology = selectedTechnology;
        }

        public boolean isDefault() {
            return false;
        }

        @Override
        public String getDisplayName() {
            return selectedTechnology;
        }

        public DescriptorExtensionList<ConfigTechnologyOptions, ConfigTechnologyOptionsDescriptor> getRepositoryLocationDescriptors() {
            return Hudson.getInstance().getDescriptorList(ConfigTechnologyOptions.class);
        }
    }

    public static class JavaOptions extends ConfigTechnologyOptions {

        @DataBoundConstructor
        public JavaOptions() {
            super();
        }

        @Extension
        public static class JavaOptionsDescriptor extends ConfigTechnologyOptionsDescriptor {

            public boolean isDefault() {
                return true;
            }

            public JavaOptionsDescriptor() {
                super(JavaOptions.class, ConfigTechnologies.Java.getDisplayName());
            }
        }

    }

    public static class DotNetOptions extends ConfigTechnologyOptions {

        @DataBoundConstructor
        public DotNetOptions() {
            super();
        }

        @Extension
        public static class DotNetOptionsDescriptor extends ConfigTechnologyOptionsDescriptor {

            public DotNetOptionsDescriptor() {
                super(DotNetOptions.class, ConfigTechnologies.DotNet.getDisplayName());
            }
        }
    }
}
