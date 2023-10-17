package com.tsh.dk.cmn.solaceTester.config;

import com.solacesystems.jcsmp.DeliveryMode;

import java.util.HashMap;
import java.util.Map;

public class SessionConfiguration {
    public enum AuthenticationScheme {
        BASIC,
        CLIENT_CERTIFICATE,
        KERBEROS
    };

    // Router properties
    private String host;
    private String msgVpn;
    private String userName;
    private String userPassword;
    public int reConnectRetries; //property.RECONNECT_RETRIES);
    public int retriesPerHost;//property.RETRIES_PRE_HOST;

    private DeliveryMode delMode = DeliveryMode.PERSISTENT;

    private Map<String, String> argBag = new HashMap<String, String>();

    public Map<String, String> getArgBag() {
        return argBag;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String value) {
        host = value;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getuserPassword() {
        return userPassword;
    }

    public void setuserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getMsgVpn() {
        return msgVpn;
    }

    public void setMsgVpn(String msgVpn) {
        this.msgVpn = msgVpn;
    }

    public DeliveryMode getDeliveryMode() {
        return delMode;
    }

    public void setDeliveryMode(DeliveryMode mode) {
        this.delMode = mode;
    }

    public int getReConnetecRetries() {
        return reConnectRetries;
    }

    public void setReConnetecRetries(int reConnetecRetries) {
        this.reConnectRetries = reConnetecRetries;
    }

    public int getRetriesPerHost() {
        return retriesPerHost;
    }

    public void setRetriesPerHost(int retriesPerHost) {
        this.retriesPerHost = retriesPerHost;
    }
}
