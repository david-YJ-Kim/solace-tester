package com.tsh.dk.cmn.solaceTester.sender;

import java.util.HashMap;
import java.util.Random;

import com.solacesystems.jcsmp.*;
import com.tsh.dk.cmn.solaceTester.sender.code.TestMessageCode;
import com.tsh.dk.cmn.solaceTester.sender.util.MessageSendUtil;
import com.tsh.dk.cmn.solaceTester.sender.util.SequenceManageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSendExecutor {

    private static final Logger logger = LoggerFactory.getLogger(MessageSendExecutor.class);
    public static Random random = new Random();
    private MessageSendUtil messageSendUtil;
    private final XMLMessageProducer messageProducer;

    public MessageSendExecutor(XMLMessageProducer messageProducer) {
        this.messageSendUtil = new MessageSendUtil();
        this.messageProducer = messageProducer;
    }



    public void sendIndividualMessage(String targetSystem, String eventName, String topicName, String payload) throws JCSMPException {

        logger.info("Message send to System: {}, eventName: {}, topicName:{}, payload:{} ",
                targetSystem, eventName, topicName, payload);

        Topic topic = messageSendUtil.createTopic(topicName);
        TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
        SDTMap userPMap = JCSMPFactory.onlyInstance().createMap();
        userPMap.putString("messageId", SequenceManageUtil.generateMessageID());
        userPMap.putString("cid", eventName);

        msg.setProperties(userPMap);
        msg.setText(payload);

        this.messageProducer.send(msg, topic);

        logger.info("Message Succefully send to System: {}, eventName: {}, topicName:{}, payload:{} ",
                targetSystem, eventName, topicName, payload);

        topic = null; msg = null;
    }




//		public void sendMessage(String payload) throws JCSMPException {
//	//		SequenceManager.getTargetName("MOS");
//			this.sendMessage(payload, MessageManageUtil.getTopicName("WFS", "HELLOWROLD!", "LOAD_REQ", "EQP_ID_0001"));
//
//		}


    public void sendMessage(String topicName, String payload) throws JCSMPException {


        String targetSystemName = "RTD";
        String eventName = "DSP_REQ";

        Topic topic = messageSendUtil.createTopic(topicName);
        String messageId = SequenceManageUtil.generateMessageID();

        TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
        SDTMap userPMap = JCSMPFactory.onlyInstance().createMap();
        userPMap.putString("messageId", messageId);
        userPMap.putString("cid", eventName);

        msg.setProperties(userPMap);
        msg.setText(payload);

        this.messageProducer.send(msg, topic);
//			System.out.println("[MESSAGE_SENDER] ID: " + messageId + " was sent to  topic it's name " + topicName + " with contetn : " + payload);

    }

    public void sendSolaceTestMessage(String testId, String senderId, Integer loopSeq, Integer messageSeq, String topicName, String payload) throws JCSMPException {

        for(int i=1; i < messageSeq+1; i++){

            this.messageSendUtil.sleepRandomly(5,15);

            HashMap<String, String> map = new HashMap<>();
            map.put(TestMessageCode.messageId.name(), SequenceManageUtil.generateMessageID());
            map.put(TestMessageCode.testId.name(), testId);
            map.put(TestMessageCode.senderId.name(), senderId);
            map.put(TestMessageCode.loopSeq.name(), String.valueOf(loopSeq));
            map.put(TestMessageCode.messageSeq.name(), String.valueOf(i));
            map.put(TestMessageCode.topic.name(), topicName);

            this.sendMessage(topicName, payload, this.messageSendUtil.generatePropertyMap(map));

        }
    }

    private void sendMessage(String topicName, String payload, SDTMap usrMap) throws JCSMPException {

        Topic topic = messageSendUtil.createTopic(topicName);
        if(!usrMap.containsKey(TestMessageCode.messageId.name())){
            usrMap.putString(TestMessageCode.messageId.name(), SequenceManageUtil.generateMessageID());
        }

        TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);

        msg.setProperties(usrMap);
        msg.setText(payload);
        msg.setDeliveryMode(DeliveryMode.PERSISTENT);

        this.messageProducer.send(msg, topic);
//			System.out.println("[MESSAGE_SENDER] ID: " + usrMap.getString(TestMessageCode.messageId.name()) + " was sent to  topic it's name " + topicName + " with contetn : " + payload);

    }

    /**
     *
     * @param loopCount
     * @param sendLatencyPerLoop: 10,30 ms
     * @param payload
     * @param topicName
     * @throws JCSMPException
     */
    public void sendLoopMessage(int loopCount, String sendLatencyPerLoop, String payload, String topicName) throws JCSMPException {



        int minLatency = Integer.parseInt(sendLatencyPerLoop.split(",")[0]);
        int maxLatency = Integer.parseInt(sendLatencyPerLoop.split(",")[1]);

        for (int i=0; i < loopCount; i++) {

            // 100~500 사이의 임의의 숫자 생성
            int sleepTime = random.nextInt(minLatency) + maxLatency;

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            this.sendMessage("topicName", "payload");

            Topic topic = messageSendUtil.createTopic(topicName);
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);

            String msgId = topicName + "-" + String.valueOf(System.currentTimeMillis()) + "-" + String.valueOf(i);
            msg.setApplicationMessageId(msgId);

            String msgContent = "[MESSAGE_SENDER] ID: " + msgId + " Message to " + topicName + " with contents : " + payload;
            msg.setText(msgContent);
            //			msg.setUserData(new JSONObject("{}"));

            this.messageProducer.send(msg, topic);

            topic = null; msg = null;
        }

    }
}
