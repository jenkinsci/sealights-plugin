package io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities;

/**
 * Created by shahar on 5/18/2017.
 */
public enum ConfigTechnologies {
    Java("Java") {
        @Override public String getDisplayName() {
            return "Java Options";
        }
    },
    DotNet("DotNet") {
        @Override public String getDisplayName() {
            return "DotNet Options";
        }
    };

    private final String name;

    ConfigTechnologies (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract String getDisplayName();
}
