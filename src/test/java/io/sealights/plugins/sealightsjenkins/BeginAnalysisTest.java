package io.sealights.plugins.sealightsjenkins;

import hudson.EnvVars;
import io.sealights.agents.infra.integration.SeaLightsPluginInfo;
import io.sealights.plugins.sealightsjenkins.utils.NullLogger;
import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;


public class BeginAnalysisTest {
    @Test
    public void resolveBSID_fromEnvVar_bsidClassMemberShouldNotChange() {
        String buildSessionIdKey = "SL_BUILD_SESSION_ID";
        String buildSessionIdArg = "${" + buildSessionIdKey + "}";
        String buildSessionId = "123";
        BeginAnalysis beginAnalysis = new BeginAnalysis();
        SeaLightsPluginInfo slInfo = new SeaLightsPluginInfo();
        Properties additionalProps = new Properties();
        EnvVars envVars = new EnvVars();
        envVars.put(buildSessionIdKey, buildSessionId);
        beginAnalysis.setBuildSessionId(buildSessionIdArg);

        String resolveBuildSessionId = beginAnalysis.resolveBuildSessionId(new NullLogger(), slInfo, additionalProps, envVars);

        Assert.assertEquals("buildSessionId filed shouldn't change", buildSessionIdArg,
         beginAnalysis.getBuildSessionId());
        Assert.assertEquals("actual buildSessionId should be resolved from env vars", buildSessionId,
         resolveBuildSessionId);
    }
}
