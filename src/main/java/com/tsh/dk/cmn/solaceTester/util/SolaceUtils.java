package com.tsh.dk.cmn.solaceTester.util;

import com.solacesystems.jcsmp.*;
import com.tsh.dk.cmn.solaceTester.config.SessionConfiguration;

import java.util.Map;
import java.util.Map.Entry;

public class SolaceUtils {
    public static final String xmldoc = "<sample>1</sample>";
    public static final String xmldocmeta = "<sample><metadata>1</metadata></sample>";
    public static final String attachmentText = "my attached data";

    public static JCSMPSession newSession(SessionConfiguration sc, SessionEventHandler evtHdlr, Map<String, Object> extra) {
        JCSMPProperties properties = new JCSMPProperties();

        properties.setProperty(JCSMPProperties.HOST, sc.getHost());
        properties.setProperty(JCSMPProperties.VPN_NAME, sc.getMsgVpn());
        properties.setProperty(JCSMPProperties.USERNAME, sc.getUserName());
        properties.setProperty(JCSMPProperties.PASSWORD, sc.getuserPassword());
        properties.setProperty(JCSMPProperties.MESSAGE_ACK_MODE, JCSMPProperties.SUPPORTED_MESSAGE_ACK_AUTO);

        // Disable certificate checking
        properties.setBooleanProperty(JCSMPProperties.SSL_VALIDATE_CERTIFICATE, false);
        properties.setProperty(JCSMPProperties.AUTHENTICATION_SCHEME, JCSMPProperties.AUTHENTICATION_SCHEME_BASIC);

        JCSMPChannelProperties chnnProp = new JCSMPChannelProperties();
        chnnProp.setReconnectRetries(sc.getReConnetecRetries());
        chnnProp.setConnectRetriesPerHost(sc.getRetriesPerHost());

        properties.setProperty(JCSMPProperties.CLIENT_CHANNEL_PROPERTIES, chnnProp);

        /*
         * Allow extra properties to supplement / override the above, when set
         * by a particular sample.
         */
        if (extra != null) {
            for (Entry<String, Object> extraProp : extra.entrySet()) {
                properties.setProperty(extraProp.getKey(), extraProp.getValue());
            }
        }

        JCSMPSession session = null;
        try {
            session = JCSMPFactory.onlyInstance().createSession(properties, null, evtHdlr);
            return session;
        } catch (InvalidPropertiesException ipe) {
            System.err.println("Error during session creation: ");
            ipe.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

}
