package io.sealights.plugins.sealightsjenkins.utils;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Util;
import hudson.model.*;
import jenkins.model.Jenkins;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Nadav on 6/7/2016.
 */
public class JenkinsUtils {
    public String expandPathVariable(AbstractBuild<?, ?> build, String path) {
        String[] tokens = path.split(Pattern.quote(File.separator.toString()));
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = expandVariable(build, tokens[i]);
        }
        path = StringUtils.join(tokens, File.separatorChar);

        return path;
    }

    public String expandVariable(AbstractBuild<?, ?> build, String variable) {
        return Util.replaceMacro(variable, build.getBuildVariables());
    }

    public static Map<String, String> createMetadataFromEnvVars(EnvVars envVars) {
        Map<String, String> metadata = new HashMap<>();

        String logsUrl = envVars.get("PROMOTED_URL");
        if (StringUtils.isNullOrEmpty(logsUrl)) {
            logsUrl = envVars.get("BUILD_URL");
        }
        metadata.put("logsUrl", logsUrl + "console");


        if (!StringUtils.isNullOrEmpty(envVars.get("PROMOTED_JOB_NAME"))) {
            metadata.put("jobName", envVars.get("PROMOTED_JOB_NAME"));
        } else {
            metadata.put("jobName", envVars.get("JOB_NAME"));
        }

        return metadata;
    }

    public static String resolveEnvVarsInString(EnvVars envVars, String envVarKey) {
        if (envVarKey == null || envVars == null){
            return "";
        }
        return envVars.expand(envVarKey);
    }

    public static String getUpstreamBuildName(AbstractBuild<?, ?> build, String upstreamProjectName, Logger logger) {
        String finalBuildName = getBuildNumberFromUpstreamBuild(build.getCauses(), upstreamProjectName);
        if (StringUtils.isNullOrEmpty(finalBuildName)) {
            logger.warning("Couldn't find build number for " + upstreamProjectName + ". Using this job's build name.");
            return null;
        }

        logger.info("Upstream project: " + upstreamProjectName + " # " + finalBuildName);
        return finalBuildName;
    }

    private static String getBuildNumberFromUpstreamBuild(List<Cause> causes, String trigger) {
        String buildNum = null;
        for (Cause c : causes) {
            if (c instanceof Cause.UpstreamCause) {
                buildNum = checkCauseRecursivelyForBuildNumber((Cause.UpstreamCause) c, trigger);
                if (!StringUtils.isNullOrEmpty(buildNum)) {
                    break;
                }
            }
        }
        return buildNum;
    }

    private static String checkCauseRecursivelyForBuildNumber(Cause.UpstreamCause cause, String trigger) {
        if (trigger.equals(cause.getUpstreamProject())) {
            return String.valueOf(cause.getUpstreamBuild());
        }

        return getBuildNumberFromUpstreamBuild(cause.getUpstreamCauses(), trigger);
    }

    public String getWorkspace(AbstractBuild<?, ?> build) {
        FilePath ws = build.getWorkspace();
        if (ws == null) {
            throw new RuntimeException("Got 'null' as this build workspace");
        }
        String workingDir = ws.getRemote();
        return workingDir;
    }

    public String resolveCurrentJobName(Descriptor descriptor) throws UnsupportedEncodingException {
        String descFullUrl = descriptor.getDescriptorFullUrl();
        URLDecoder.decode(descFullUrl,"UTF-8");
        String resolvedJobName = descFullUrl.substring(0, descFullUrl.lastIndexOf("/descriptor"));
        resolvedJobName = resolvedJobName.substring(resolvedJobName.lastIndexOf("/") + 1);
        return resolvedJobName;
    }

    /*
    * this code currently not used we save the logic only because its was hard to find out how to get the job name
    * */


    public Job getCurrentJob(Descriptor descriptor){
        try {
            List<Job> items = Jenkins.getInstance().getItems(Job.class);
            String currentJobName = resolveCurrentJobName(descriptor);
            for (Job j : items) {
                if (j.getName().equals(currentJobName)) {
                    return j;
                }
            }
        }catch (Exception e){

        }
        return null;
    }
}
