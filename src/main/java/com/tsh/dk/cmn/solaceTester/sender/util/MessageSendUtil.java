package com.tsh.dk.cmn.solaceTester.sender.util;

import java.util.HashMap;
import java.util.Random;

import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.SDTException;
import com.solacesystems.jcsmp.SDTMap;
import com.solacesystems.jcsmp.Topic;

public class MessageSendUtil {

    public static final Random random = new Random();
    public Topic createTopic() {
        String topicName = "Return From SEQ Library";
        return this.createTopic(topicName);
    }

    public Topic createTopic(String topicName) {
        return JCSMPFactory.onlyInstance().createTopic(topicName);

    }

    public SDTMap generatePropertyMap(HashMap<String, String> inputMap) throws SDTException {

        SDTMap userMap = JCSMPFactory.onlyInstance().createMap();

        for(String key :inputMap.keySet()) {
            userMap.putString(key, inputMap.get(key));
        }

        return userMap;

    }

    public String convertQueueIntoTopic(String queueName){
        return queueName.replaceAll("_", "/");
    }

    public void sleepRandomly(int minLatency, int maxLatency){
        // 100~500 사이의 임의의 숫자 생성
        int sleepTime = random.nextInt(minLatency) + maxLatency;

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
