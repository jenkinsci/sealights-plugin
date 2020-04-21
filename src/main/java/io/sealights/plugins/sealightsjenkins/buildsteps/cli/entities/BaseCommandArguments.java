package io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities;

import hudson.EnvVars;
import hudson.model.AbstractBuild;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.LogConfiguration;
import io.sealights.plugins.sealightsjenkins.entities.TokenData;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.StringUtils;
import lombok.Data;

/**
 * Basic arguments that is needed for the executors
 */
 @Data
public class BaseCommandArguments {

    private String appName;
    private String branchName;
    private String buildName;

    private String token;
    private String tokenFile;
    private TokenData tokenData;

    private String customerId;
    private String url;
    private String proxy;
    private String labId;
    private String agentPath;
    private String javaPath;

    private String buildSessionId;
    private String buildSessionIdFile;

    private AbstractBuild<?, ?> build;
    private EnvVars envVars;
    private Logger logger;
    private LogConfiguration logConfiguration;

    @Override
    public String toString() {
        return "BaseCommandArguments{" +
                "appName='" + appName + '\'' +
                ", branchName='" + branchName + '\'' +
                ", buildName='" + buildName + '\'' +
                ", token='" + StringUtils.trimStart(token) + '\'' +
                ", tokenFile='" + tokenFile + '\'' +
                ", tokenData=" + tokenData +
                ", customerId='" + customerId + '\'' +
                ", url='" + url + '\'' +
                ", proxy='" + proxy + '\'' +
                ", labId='" + labId + '\'' +
                ", agentPath='" + agentPath + '\'' +
                ", javaPath='" + javaPath + '\'' +
                ", buildSessionId='" + buildSessionId + '\'' +
                ", buildSessionIdFile='" + buildSessionIdFile + '\'' +
                '}';
    }
}
