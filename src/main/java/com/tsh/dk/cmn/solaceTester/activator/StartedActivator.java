package com.tsh.dk.cmn.solaceTester.activator;

import com.solacesystems.jcsmp.JCSMPException;
import com.tsh.dk.cmn.solaceTester.ApplicationProperties;
import com.tsh.dk.cmn.solaceTester.SolaceProperties;
import com.tsh.dk.cmn.solaceTester.sender.MessageSendManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartedActivator implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    public SolaceProperties properties;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        String ApType = ApplicationProperties.getInstance().getApType();
        if(ApType.equals(ActivatorCode.sender.name())){
            try {
                MessageSendManager manager = new MessageSendManager();
            } catch (JCSMPException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
