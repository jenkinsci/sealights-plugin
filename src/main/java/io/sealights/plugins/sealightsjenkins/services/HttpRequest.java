package io.sealights.plugins.sealightsjenkins.services;

/**
 * Created by Ronis on 8/2/2017.
 */
public class HttpRequest {
    private String url;
    private String proxy;
    private String token;

    public HttpRequest(String url, String proxy, String token) {
        this.url = url;
        this.proxy = proxy;
        this.token = token;
    }

    public HttpRequest(String url, String proxy) {
        this.url = url;
        this.proxy = proxy;
    }

    public String getUrl() {
        return url;
    }

    public String getProxy() {
        return proxy;
    }

    public String getToken() {
        return token;
    }
}
