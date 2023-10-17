package com.tsh.dk.cmn.solaceTester;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Data
@Component
public class ApplicationProperties {

    private static ApplicationProperties properties;
    Environment env;
    @Value("${ap.running-type}")
    private String apType;
    @Value("${ap.sender.queue-prefix}")
    private String senderQueuePrefix;
    @Value("${ap.sender.sender-count}")
    private String senderThreadCount;
    @Value("${ap.sender.tps-per-sender}")
    private String senderThreadTPS;
    @Value("${ap.sender.retention-per-sender-sec}")
    private String senderThreadRetentionSec;

    @Value("${ap.receiver.queue-prefix}")
    private String receiverQueuePrefix;
    @Value("${ap.receiver.receiver-mode}")
    private String receiverMode;
    @Value("${ap.receiver.receiver-latency-ms}")
    private String receiverLatencyRangeMs;


    private ApplicationProperties(Environment env) {
        this.env = env;
        properties = this;

    }
    public static ApplicationProperties getInstance() {
        return properties;
    }
}
