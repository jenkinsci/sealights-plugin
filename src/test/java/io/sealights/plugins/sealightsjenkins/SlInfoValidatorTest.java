package io.sealights.plugins.sealightsjenkins;

import io.sealights.agents.infra.integration.SeaLightsPluginInfo;
import io.sealights.agents.infra.integration.enums.ExecutionType;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.NullLogger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SlInfoValidatorTest {
    private SlInfoValidator validator;
    private SeaLightsPluginInfo pluginInfo;
    private Logger nullLogger = new NullLogger();

    @Before
    public void setUp() throws Exception {
        validator = new SlInfoValidator(nullLogger);
        pluginInfo = new SeaLightsPluginInfo();
    }

    @Test
    public void validate_notCreatingBSidAndHasOneNoBuildArgs_shouldReturnTrue() {
        pluginInfo.setCreateBuildSessionId(false);
        pluginInfo.setBuildSessionId("bsid1");

        assertTrue("Should not validate app branch and build", validator.validate(pluginInfo));
    }

    @Test
    public void validate_runTestOnlyNoBuildArgsHasLabId_shouldReturnTrue() {
        pluginInfo.setExecutionType(ExecutionType.TESTS_ONLY);
        pluginInfo.setLabId("lab1");

        assertTrue("Should not validate app branch and build", validator.validate(pluginInfo));
    }

    @Test
    public void validate_runTestOnlyNoBuildArgsNoLabId_shouldReturnFalse() {
        pluginInfo.setExecutionType(ExecutionType.TESTS_ONLY);

        assertFalse("Should validate app branch and build", validator.validate(pluginInfo));
    }

    @Test
    public void validate_hasBsidNoBuildArgs_shouldReturnTrue() {
        pluginInfo.setBuildSessionId("bsid1");

        assertTrue("Should not validate app branch and build", validator.validate(pluginInfo));
    }

    @Test
    public void validate_createBsidTrueNoBuildArgs_shouldReturnFalse() {
        pluginInfo.setCreateBuildSessionId(true);

        assertFalse("Should validate app branch and build", validator.validate(pluginInfo));
    }

    @Test
    public void validate_emptyTrueNoBuildArgs_shouldReturnFalse() {
        pluginInfo.setBuildSessionId("");

        assertFalse("Should validate app branch and build", validator.validate(pluginInfo));
    }

    @Test
    public void validate_hasBuildArgs_shouldReturnTrue() {
        pluginInfo.setAppName("App1");
        pluginInfo.setBranchName("master");
        pluginInfo.setBuildName("build1");
        pluginInfo.setPackagesIncluded("com.*");

        Assert.assertTrue("Should validate app branch and build", validator.validate(pluginInfo));
    }
}
