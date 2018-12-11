package io.sealights.plugins.sealightsjenkins.integration;

import hudson.FilePath;
import hudson.model.Computer;
import hudson.remoting.VirtualChannel;
import io.sealights.agents.infra.pomIntegration.entities.PomFile;
import io.sealights.onpremise.agents.java.agent.infra.logging.ILogger;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Nadav on 5/21/2016.
 */
public class JenkinsPomFile extends PomFile {
    public JenkinsPomFile(String filename, ILogger log) {
        super(filename);
    }

    @Override
    protected void saveInternal(String filename, Transformer transformer, DOMSource domSource) throws TransformerException, IOException, InterruptedException {
        VirtualChannel channel = Computer.currentComputer().getChannel();
        FilePath filePath = new FilePath(channel, filename);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(outputStream);
        transformer.transform(domSource, streamResult);

        String str = new String( outputStream.toByteArray(), "UTF-8");

        String result = filePath.act(new SaveFileCallable(str));
        if (!StringUtils.isNullOrEmpty(result))
            LOGGER.info("save - Result:" + result);
    }

    @Override
    protected Document getDocumentInternal() throws IOException, InterruptedException, SAXException, ParserConfigurationException {

        VirtualChannel channel = Computer.currentComputer().getChannel();
        FilePath fp = new FilePath(channel, this.filename);
        Document doc = fp.act(new GetDocumentFileCallable());
        return doc;
    }

    @Override
    public void backup() throws Exception {
        String backupFile = this.filename + BACKUP_EXTENSION;
        LOGGER.info("JenkinsPomFile - creating a back up file: " + backupFile);
        VirtualChannel channel = Computer.currentComputer().getChannel();
        FilePath sourceFile = new FilePath(channel, this.filename);
        FilePath targetFile = new FilePath(channel, backupFile);
        sourceFile.copyTo(targetFile);
    }
}
