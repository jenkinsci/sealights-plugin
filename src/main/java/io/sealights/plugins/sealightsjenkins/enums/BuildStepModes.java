package io.sealights.plugins.sealightsjenkins.enums;

/**
 * Created by Nadav on 6/7/2016.
 */
public enum BuildStepModes {
    Off() {
        @Override public String getDisplayName() {
            return "Disable this step";
        }
    },
    InvokeMavenCommand() {
        @Override public String getDisplayName() {
            return "Invoke top-level Maven targets without SeaLights";
        }
    },
    InvokeMavenCommandWithSealights() {
        @Override public String getDisplayName() {
            return "Invoke top-level Maven targets with Sealights Continuous Testing";
        }
    },
    PrepareSealights() {
        @Override public String getDisplayName() {
            return "Integrate Sealights into POM files";
        }
    };

    public abstract String getDisplayName();
}
