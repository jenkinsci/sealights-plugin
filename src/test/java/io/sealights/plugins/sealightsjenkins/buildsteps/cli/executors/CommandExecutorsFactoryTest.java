package io.sealights.plugins.sealightsjenkins.buildsteps.cli.executors;

import io.sealights.plugins.sealightsjenkins.buildsteps.cli.CommandMode;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.AbstractCommandArgument;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.BaseCommandArguments;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.entities.CommandModes;
import io.sealights.plugins.sealightsjenkins.buildsteps.cli.utils.ModeToArgumentsConverter;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.NullLogger;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

public class CommandExecutorsFactoryTest {

    private Logger nullLogger = new NullLogger();

    @Test
    public void createExecutor_withStartMode_shouldGetStartCommandExecutor() {
        //Arrange
        CommandExecutorsFactory factory = new CommandExecutorsFactory();
        CommandMode mode = createStartView();
        //Act
        ICommandExecutor executor = factory.createExecutor(nullLogger, createBaseCommandArguments(), createAbstractCommandArguments(mode));
        //Assert
        boolean isStartExecutor = executor instanceof StartCommandExecutor;
        Assert.assertTrue("The created executor is not an instance of 'StartCommandExecutor'", isStartExecutor);
    }

    @Test
    public void createExecutor_withEndMode_shouldGetEndCommandExecutor() {
        //Arrange
        CommandExecutorsFactory factory = new CommandExecutorsFactory();
        CommandMode mode = createEndView();
        //Act
        ICommandExecutor executor = factory.createExecutor(nullLogger, createBaseCommandArguments(), createAbstractCommandArguments(mode));
        //Assert
        boolean isEndExecutor = executor instanceof EndCommandExecutor;
        Assert.assertTrue("The created executor is not an instance of 'EndCommandExecutor'", isEndExecutor);
    }

    @Test
    public void createExecutor_withUploadReportsMode_shouldGetUploadReportCommandExecutor() {
        //Arrange
        CommandExecutorsFactory factory = new CommandExecutorsFactory();
        CommandMode mode = new CommandMode.UploadReportsView("", "", "", "","");
        //Act
        ICommandExecutor executor = factory.createExecutor(nullLogger, createBaseCommandArguments(), createAbstractCommandArguments(mode));
        //Assert
        boolean isUploadReportsExecutor = executor instanceof UploadReportsCommandExecutor;
        Assert.assertTrue("The created executor is not an instance of 'UploadReportsCommandExecutor'", isUploadReportsExecutor);
    }

    @Test
    public void createExecutor_withExternalReportMode_shouldGetUploadReportCommandExecutor() {
        //Arrange
        CommandExecutorsFactory factory = new CommandExecutorsFactory();
        CommandMode mode = new CommandMode.ExternalReportView("fake-report","","");
        //Act
        ICommandExecutor executor = factory.createExecutor(nullLogger, createBaseCommandArguments(), createAbstractCommandArguments(mode));
        //Assert
        boolean isUploadReportsExecutor = executor instanceof ExternalReportCommandExecutor;
        Assert.assertTrue("The created executor is not an instance of 'ExternalReportCommandExecutor'", isUploadReportsExecutor);
    }

    @Test
    public void createExecutor_withConfigMode_shouldGetUploadReportCommandExecutor() {
        //Arrange
        CommandExecutorsFactory factory = new CommandExecutorsFactory();
        CommandMode mode = mock(CommandMode.ConfigView.class);
        when(mode.getCurrentMode()).thenReturn(CommandModes.Config);

        //Act
        ICommandExecutor executor = factory.createExecutor(nullLogger, createBaseCommandArguments(), createAbstractCommandArguments(mode));
        //Assert
        boolean isUploadReportsExecutor = executor instanceof ConfigCommandExecutor;
        Assert.assertTrue("The created executor is not an instance of 'ConfigCommandExecutor'", isUploadReportsExecutor);
    }

    @Test
    public void createExecutor_withNullBaseArguments_shouldGetNullCommandExecutor() {
        //Arrange
        CommandExecutorsFactory factory = new CommandExecutorsFactory();
        //Act
        ICommandExecutor executor = factory.createExecutor(nullLogger, null, createAbstractCommandArguments());
        //Assert
        boolean isNullExecutor = executor instanceof NullCommandExecutor;
        Assert.assertTrue("The created executor is not an instance of 'NullCommandExecutor'", isNullExecutor);
    }

    @Test
    public void createExecutor_withNullCommandArgument_shouldGetNullCommandExecutor() {
        //Arrange
        CommandExecutorsFactory factory = new CommandExecutorsFactory();
        //Act
        ICommandExecutor executor = factory.createExecutor(nullLogger, createBaseCommandArguments(), null);
        //Assert
        boolean isNullExecutor = executor instanceof NullCommandExecutor;
        Assert.assertTrue("The created executor is not an instance of 'NullCommandExecutor'", isNullExecutor);
    }

    @Test
    public void createExecutor_withNullMode_shouldGetNullCommandExecutor() {
        //Arrange
        CommandExecutorsFactory factory = new CommandExecutorsFactory();
        //Act
        ICommandExecutor executor = factory.createExecutor(nullLogger, createBaseCommandArguments(), createAbstractCommandArguments(null));
        //Assert
        boolean isNullExecutor = executor instanceof NullCommandExecutor;
        Assert.assertTrue("The created executor is not an instance of 'NullCommandExecutor'", isNullExecutor);
    }


    private BaseCommandArguments createBaseCommandArguments() {
        BaseCommandArguments baseArgs = new BaseCommandArguments();
        return baseArgs;
    }

    private AbstractCommandArgument createAbstractCommandArguments() {
        CommandMode mode = createStartView();
        return createAbstractCommandArguments(mode);
    }

    private AbstractCommandArgument createAbstractCommandArguments(CommandMode mode) {
        ModeToArgumentsConverter converter = new ModeToArgumentsConverter();
        return converter.convert(mode);
    }
    
    private CommandMode.StartView createStartView(){
        String testStage="";
        String buildSessionId ="fake-buildSessionId";
        String additionalArguments ="key=value";
        return new CommandMode.StartView(testStage,buildSessionId,additionalArguments);
    }

    private CommandMode.EndView createEndView(){
        String buildSesionId = "fake-build-session";
        String additionalArga = "key=value";
        return new CommandMode.EndView(buildSesionId,additionalArga);
    }
}
