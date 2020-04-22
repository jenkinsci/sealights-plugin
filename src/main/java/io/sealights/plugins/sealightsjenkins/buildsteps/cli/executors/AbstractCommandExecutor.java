package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;


import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.PathUtils;
import io.sealights.plugins.sealightsjenkins.utils.StringUtils;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for command executors.
 */
public abstract class AbstractCommandExecutor implements ICommandExecutor {
    public static final String SL_TAGS = "sl.tags";
    public static final String JENKINS = "jenkins";

    protected Logger logger;
    protected BaseCommandArguments baseArgs;
    private ProcessExecutor processExecutor;

    public AbstractCommandExecutor(Logger logger, BaseCommandArguments baseArgs) {
        this.logger = logger;
        this.baseArgs = baseArgs;
        this.processExecutor = new ProcessExecutor();
    }

    public boolean execute() {
        try {
            ProcessResult result = processExecutor.command(createExecutionCommand()).readOutput(true).execute();
            String output = result.outputUTF8();
            logger.info(output);
            return result.getExitValue() == 0;
        } catch (Exception e) {
            logger.error("Unable to perform '" + getCommandName() + "' command. Error: ", e);
        }
        return false;
    }


    public List<String> createExecutionCommand() {
        List<String> commands = new ArrayList<>();

        String javaPath = resolvedJavaPath();
        commands.add(javaPath);
        commands.add(formatTagProp());
        commands.addAll(baseArgs.getLogConfiguration().toSystemProperties());
        commands.add("-jar");
        commands.add(baseArgs.getAgentPath());
        commands.add(getCommandName());

        addBaseArgumentsLine(commands);
        addAdditionalArguments(commands);

        return commands;
    }

    public static String formatTagProp(){
        return String.format("-D%s=%s", SL_TAGS, JENKINS);
    }

    protected abstract String getCommandName();

    public abstract void addAdditionalArguments(List<String> commands);

    protected void addBaseArgumentsLine(List<String> commandsList) {
        if (baseArgs.getTokenData() != null) {
            addArgumentKeyVal("token", baseArgs.getTokenData().getToken(), commandsList);
        } else {
            addArgumentKeyVal("token", baseArgs.getToken(), commandsList);
            addArgumentKeyVal("tokenfile", baseArgs.getTokenFile(), commandsList);
            addArgumentKeyVal("customerid", baseArgs.getCustomerId(), commandsList);
            addArgumentKeyVal("server", baseArgs.getUrl(), commandsList);
        }

        addArgumentKeyVal("buildsessionid", baseArgs.getBuildSessionId(), commandsList);
        addArgumentKeyVal("buildsessionidfile", baseArgs.getBuildSessionIdFile(), commandsList);
        addArgumentKeyVal("appname", baseArgs.getAppName(), commandsList);
        addArgumentKeyVal("buildname", baseArgs.getBuildName(), commandsList);
        addArgumentKeyVal("branchname", baseArgs.getBranchName(), commandsList);

        addArgumentKeyVal("labid", baseArgs.getLabId(), commandsList);
        addArgumentKeyVal("proxy", baseArgs.getProxy(), commandsList);
    }

    protected void addArgumentKeyVal(String key, String val, List<String> commandsList) {
        if (StringUtils.isNullOrEmpty(val)) {
            return;
        }
        commandsList.add("-" + key);
        commandsList.add(val);
    }

    protected String resolvedJavaPath() {
        if (!StringUtils.isNullOrEmpty(baseArgs.getJavaPath()))
            return baseArgs.getJavaPath();

        String localJava = PathUtils.join(System.getProperty("java.home"), "bin", "java");
        return localJava;
    }

    public void setProcessExecutor(ProcessExecutor processExecutor) {
        this.processExecutor = processExecutor;
    }
    public String[] prettifyToken(String[] commands){
        String[] commandsClone = commands.clone();
          for (int i =0;i<=commandsClone.length;i++){
            if (commandsClone[i].equals("-token")){
                commandsClone[i+1] = StringUtils.trimStart(commandsClone[i+1]);
                break;
            }
        }
           return commandsClone;
       }
}
