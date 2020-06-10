package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.NullLogger;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SendExternalReportTest {

    private Logger nullLogger = new NullLogger();

    @Test
    public void execute_shouldLogForDeprecationAndReturnTrue() {
        Logger mockLogger = mock(Logger.class);
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ExternalReportCommandExecutor externalReportExecutor = new ExternalReportCommandExecutor(mockLogger, null, null);

        boolean result = externalReportExecutor.execute();

        verify(mockLogger).info(captor.capture());
        String logMessage = captor.getValue();
        Assert.assertTrue("execute commands should always return true", result);
        Assert.assertEquals(ExternalReportCommandExecutor.DEPRECATION_MESSAGE, logMessage);
    }
}
