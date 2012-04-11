/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.dto;

import java.util.Map;

/**
 *
 * @author Ruben
 */
public class RequestInfoDTO {

    private String ip;
    private Integer count;
    private String referer;
    private String userAgent;
    private String xForwardedFor;
    private String xForwardedHost;
    private String xForwardedServer;

    private Map<String, String> headers;

    public RequestInfoDTO() {
    }

    public RequestInfoDTO(Integer count, String ip, String referer, String userAgent, String xForwardedFor, String xForwardedHost, String xForwardedServer, Map<String, String> headers) {
        this.ip = ip;
        this.count = count;
        this.referer = referer;
        this.userAgent = userAgent;
        this.xForwardedFor = xForwardedFor;
        this.xForwardedHost = xForwardedHost;
        this.xForwardedServer = xForwardedServer;
        this.headers = headers;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getxForwardedFor() {
        return xForwardedFor;
    }

    public void setxForwardedFor(String xForwardedFor) {
        this.xForwardedFor = xForwardedFor;
    }

    public String getxForwardedHost() {
        return xForwardedHost;
    }

    public void setxForwardedHost(String xForwardedHost) {
        this.xForwardedHost = xForwardedHost;
    }

    public String getxForwardedServer() {
        return xForwardedServer;
    }

    public void setxForwardedServer(String xForwardedServer) {
        this.xForwardedServer = xForwardedServer;
    }

}
