package io.sealights.plugins.sealightsjenkins.integration;

import hudson.EnvVars;
import io.sealights.agents.infra.integration.SeaLightsPluginInfo;
import io.sealights.plugins.sealightsjenkins.utils.JenkinsUtils;

import java.util.Properties;

/**
 * Created by Nadav on 4/19/2016.
 */
public class JenkinsSeaLightsPluginInfo extends SeaLightsPluginInfo {

    public void resolveFromAdditionalProperties(Properties additionalProps, EnvVars envVars){
        setPackagesIncluded(JenkinsUtils.resolveEnvVarsInString(envVars, additionalProps.getProperty("packagesincluded")));
        setPackagesExcluded(JenkinsUtils.resolveEnvVarsInString(envVars, additionalProps.getProperty("packagesexcluded")));
        setListenerJar(JenkinsUtils.resolveEnvVarsInString(envVars, additionalProps.getProperty("testlistenerjar")));
        setScannerJar(JenkinsUtils.resolveEnvVarsInString(envVars, additionalProps.getProperty("buildscannerjar")));
        setAppName(JenkinsUtils.resolveEnvVarsInString(envVars, additionalProps.getProperty("appname")));
        setBranchName(JenkinsUtils.resolveEnvVarsInString(envVars, additionalProps.getProperty("branch")));
    }
}

