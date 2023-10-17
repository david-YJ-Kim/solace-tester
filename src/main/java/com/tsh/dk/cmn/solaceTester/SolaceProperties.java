package com.tsh.dk.cmn.solaceTester;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Data
@Component
public class SolaceProperties {

    private static SolaceProperties properties;
    Environment env;

    @Value("${solace.java.host}")
    private String host;

    @Value("${solace.java.vpn}")
    private String msgVpn;

    @Value("${solace.java.client-username}")
    private String clientUserName;

    @Value("${solace.java.client-password}")
    private String clientPassWord;

    @Value("${solace.java.client-name}")
    private String clientName;


    @Value("${solace.java.reconnect-retries}")
    private int reconnectRetries;

    @Value("${solace.java.retries-per-host}")
    private int retriesPerHost;

    private SolaceProperties(Environment env) {
        this.env = env;
        properties = this;

    }
    public static SolaceProperties getInstance() {
        return properties;
    }
}
