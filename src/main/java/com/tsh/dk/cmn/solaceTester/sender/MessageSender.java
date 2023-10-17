package com.tsh.dk.cmn.solaceTester.sender;

import com.solacesystems.jcsmp.JCSMPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class MessageSender implements Runnable{

    public static final Logger logger = LoggerFactory.getLogger(MessageSender.class);
    public String senderId;
    public String topicName;
    public String testId;
    private MessageSendExecutor executor;
    public MessageSender(MessageSendExecutor executor, String testId, String topicName){

        System.out.println("MessageSender Start :" + topicName);

        this.executor = executor;
        this.testId = testId;
        this.topicName = topicName;
    }

    @Override
    public void run() {
//        this.startStableTest(this.testId);

        int maxLoopCount = 60; // 5분: 300
        this.startSenderTest(testId, maxLoopCount);
    }

    public void startSenderTest(String testId, int maxLoopCount){


        int maxMessageSeq = 50;

        this.senderId = topicName.concat("-" + Thread.currentThread().getName());
        int crnLoopCount = 0;
        while (crnLoopCount < maxLoopCount){

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            crnLoopCount ++;

            // 1초 마다 100회 순회를 도는데,

            try {
                this.executor.sendSolaceTestMessage(testId, senderId, crnLoopCount, maxMessageSeq, topicName, "Hellow World");
            } catch (JCSMPException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info(senderId + " Complete to Send");

    }


    public void startStableTest(String testId){


        this.senderId = topicName.concat("-" + Thread.currentThread().getName());
        int i = 0;
        while (true){

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            i ++;

            // 1초 마다 100회 순회를 도는데,

            try {
                this.executor.sendSolaceTestMessage(testId, senderId, i, 100, topicName, "Hellow World");
            } catch (JCSMPException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
