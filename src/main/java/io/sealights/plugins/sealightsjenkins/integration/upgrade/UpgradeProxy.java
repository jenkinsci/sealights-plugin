package io.sealights.plugins.sealightsjenkins.integration.upgrade;

import io.sealights.onpremise.agents.java.agent.infra.logging.ILogger;
import io.sealights.plugins.sealightsjenkins.integration.JarsHelper;
import io.sealights.plugins.sealightsjenkins.integration.upgrade.entities.UpgradeConfiguration;
import io.sealights.plugins.sealightsjenkins.integration.upgrade.entities.UpgradeResponse;
import io.sealights.plugins.sealightsjenkins.services.ApacheHttpClient;
import io.sealights.plugins.sealightsjenkins.services.HttpRequest;
import io.sealights.plugins.sealightsjenkins.services.HttpResponse;
import io.sealights.plugins.sealightsjenkins.utils.JsonSerializer;
import io.sealights.plugins.sealightsjenkins.utils.Logger;
import io.sealights.plugins.sealightsjenkins.utils.StreamUtils;
import io.sealights.plugins.sealightsjenkins.utils.UrlBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class UpgradeProxy {

    protected UpgradeConfiguration upgradeConfiguration;
    private ILogger logger;

    public UpgradeProxy(UpgradeConfiguration upgradeConfiguration, ILogger logger) {
        this.upgradeConfiguration = upgradeConfiguration;
        this.logger = logger;
    }

    public UpgradeResponse getRecommendedVersion(String componentName) throws IOException {
        String serverUrl = createUrlToGetRecommendedVersion(componentName);
        logger.info("Trying to get recommended version. Url: '" + serverUrl + "'");
        ApacheHttpClient client = new ApacheHttpClient();
        HttpRequest request = new HttpRequest(serverUrl, upgradeConfiguration.getProxy(), upgradeConfiguration.getToken());
        HttpResponse httpResponse = client.getJson(request);
        String jsonOrServerError = StreamUtils.toString(httpResponse.getResponseStream());
        UpgradeResponse upgradeResponse = JsonSerializer.deserialize(jsonOrServerError, UpgradeResponse.class);
        return upgradeResponse;
    }

    private String createUrlToGetRecommendedVersion(String componentName) throws MalformedURLException {
        UrlBuilder urlBuilder = new UrlBuilder();
        String token = this.upgradeConfiguration.getToken();
        String apiVersion = (token != null) ? "v2" : "v1";
        return urlBuilder.withHost(upgradeConfiguration.getServer())
                .withPath(apiVersion, "agents", componentName, "recommended")
                .withQueryParam("customerId", upgradeConfiguration.getCustomerId())
                .withQueryParam("appName", upgradeConfiguration.getAppName())
                .withQueryParam("branch", upgradeConfiguration.getBranchName())
                .withQueryParam("envName", upgradeConfiguration.getEnvironmentName())
                .toString();
    }

    public boolean downloadAgent(String urlToAgent, String destFile) throws IOException {
        boolean isSuccess = true;
        logger.info("Trying to download agent from url '" + urlToAgent + "' to folder '" + destFile + "'.");
        try {
            File agentDestination = new File(destFile);
            ApacheHttpClient client = new ApacheHttpClient();
            HttpRequest request = new HttpRequest(urlToAgent, upgradeConfiguration.getProxy());
            HttpResponse response = client.getFile(request);
            InputStream responseStream = response.getResponseStream();
            JarsHelper.copyInputStreamToFile(responseStream, new File(destFile));

            if (!agentDestination.exists()) {
                logger.error("Failed to download recommended agent.");
                isSuccess = false;
            }
        } catch (Exception e) {
            logger.error("Error while trying to download recommended agent. Error: " + e.getMessage());
            isSuccess = false;
        }

        return isSuccess;
    }
}
