package com.tsh.dk.cmn.solaceTester.sender;

import com.solacesystems.jcsmp.*;
import com.tsh.dk.cmn.solaceTester.ApplicationProperties;
import com.tsh.dk.cmn.solaceTester.SolaceProperties;
import com.tsh.dk.cmn.solaceTester.sender.util.MessageSendUtil;
import com.tsh.dk.cmn.solaceTester.util.importQueue.ImportQueueList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class MessageSendManager {

    public static void main(String[] args) {
    }

    private JCSMPProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(MessageSendManager.class);
    private final JCSMPSession session;
    private final XMLMessageProducer messageProducer;
    private final MessageSendExecutor executor;
    private final SolaceProperties solaceProperties = SolaceProperties.getInstance();
    private final ApplicationProperties applicationProperties = ApplicationProperties.getInstance();
    private MessageSendUtil messageSendUtil;
    /**
     * Constructor for global instance.
     * @throws JCSMPException
     */
    public MessageSendManager() throws JCSMPException {

        this.properties = this.generateProperties();
        this.session = JCSMPFactory.onlyInstance().createSession(this.properties);
        this.session.connect();

        this.messageProducer = session.getMessageProducer(new JCSMPStreamingPublishEventHandler() {

            public void responseReceived(String messageID) {
                logger.debug("Producer received response for msgId:{} ", messageID);
            }
            public void handleError(String messageID, JCSMPException e, long timestamp) {
                logger.error("Producer received error for msgId:{}. timstamp:{}, error:{}",
                        messageID,timestamp,e);
            }
        });

        this.executor = new MessageSendExecutor(messageProducer);
        this.messageSendUtil = new MessageSendUtil();

        System.out.println(solaceProperties.toString());
        System.out.println(applicationProperties.toString());


        logger.info("Start Test");
        // 안정성 테스트
//		this.invokeStableTest("STABLE-TEST-12HR");

        // Sender 테스트
//		this.invokeMultiSenderTest("SENDER-TEST-0");
//		this.invokeMultiSenderTest("SENDER-TEST-1");
//		this.invokeMultiSenderTest("SENDER-TEST-2");

        // Sender 테스트로 가용성 테스트 진행 부하수준 : SENDER-TEST-2
//		this.invokeMultiSenderTest("AVAI-PROC-STOP-0");
//		this.invokeMultiSenderTest("AVAI-PROC-STOP-1");
//		this.invokeMultiSenderTest("AVAI-PROC-STOP-2");

        // 가용성 -KIll 부하수준 : SENDER-TEST-2
//		this.invokeMultiSenderTest("AVAI-PROC-KILL-0");
//		this.invokeMultiSenderTest("AVAI-PROC-KILL-1");

        // KIll 부하 수준 향상을 위한 테스트
        // thread-per-queue: 2 -> 6
        // maxMessageSeq: 50 -> 100
        // latency per message : true -> no use
//		this.invokeMultiSenderTest("SENDER-TEST-3");

        // 가용성 - KILL 부하수준 : SENDER-TEST-3
//		this.invokeMultiSenderTest("AVAI-PROC-KILL-2");

        // 가용성 Server Reboot  부하수준: SENDER-TEST-3
//		this.invokeMultiSenderTest("AVAI-SERV-REBOOT-0");
//		this.invokeMultiSenderTest("AVAI-SERV-REBOOT-1");
//		this.invokeMultiSenderTest("AVAI-SERV-REBOOT-2");
//		this.invokeMultiSenderTest("AVAI-SERV-REBOOT-3");
//		this.invokeMultiSenderTest("AVAI-SERV-REBOOT-4");
//		this.invokeMultiSenderTest("AVAI-SERV-REBOOT-5");

        // Check For performance
//		this.invokeMultiSenderTest("SENDER-TEST-4"); // 삭제 대상
//		this.invokeMultiSenderTest("SENDER-TEST-5"); // 삭제 대상 (threadCountPerQueue 6 -> 1)
//		this.invokeMultiSenderTest("SENDER-TEST-6"); // 삭제 대상 (threadCountPerQueue 6 -> 1)


        // 네트워크 단절 테스트 부하수준 : SENDER-TEST-2
        // thread-per-queue: 2
        // maxMessageSeq: 50
        // latency per message : true
//		this.invokeMultiSenderTest("SENDER-AVAI-NET-DOWN-0");
//		this.invokeMultiSenderTest("SENDER-AVAI-NET-DOWN-1");
//		this.invokeMultiSenderTest("SENDER-AVAI-NET-DOWN-2");
//		this.invokeMultiSenderTest("SENDER-AVAI-NET-DOWN-5");

//		this.invokeMultiSenderTest("AVAI-NET-DOWN-0");
        this.invokeMultiSenderTest("AVAI-NET-DOWN-1");


    }

    public void invokeMultiSenderTest(String testId){

        int threadCountPerQueue = 2;

        ImportQueueList iql = new ImportQueueList(solaceProperties.getHost(),
                solaceProperties.getMsgVpn(), solaceProperties.getClientUserName(),
                solaceProperties.getClientPassWord(),
                applicationProperties.getReceiverQueuePrefix()
        );
        ArrayList<String> topicList = new ArrayList<>();
        for(String queue: iql.getQueueList()){
            String topicName = this.messageSendUtil.convertQueueIntoTopic(queue);
            topicList.add(topicName);

            for(int i=0; i<threadCountPerQueue; i++){
                Thread thread = new Thread(new MessageSender(this.executor, testId, topicName));
                thread.start();
            }
        }

    }

    public void invokeStableTest(String testId){

        ImportQueueList iql = new ImportQueueList(solaceProperties.getHost(),
                solaceProperties.getMsgVpn(), solaceProperties.getClientUserName(),
                solaceProperties.getClientPassWord(),
                applicationProperties.getReceiverQueuePrefix()
        );
        ArrayList<String> topicList = new ArrayList<>();
        for(String queue: iql.getQueueList()){
            String topicName = this.messageSendUtil.convertQueueIntoTopic(queue);
            topicList.add(topicName);
            Thread thread = new Thread(new MessageSender(this.executor, testId, topicName));
            thread.start();
        }

    }


    private JCSMPProperties generateProperties(){

        JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, solaceProperties.getHost());
        //solace msgVpn명
        properties.setProperty(JCSMPProperties.VPN_NAME, solaceProperties.getMsgVpn());
        //solace msgVpn에 접속할 클라이언트사용자명
        properties.setProperty(JCSMPProperties.USERNAME, solaceProperties.getClientUserName());
        //solace msgVpn에 접속할 클라이언트사용자 패스워드(생략 가능)
        properties.setProperty(JCSMPProperties.PASSWORD, solaceProperties.getClientPassWord());
        //Allication client name 설정 - 동일 msgVpn 내에서 uniq 해야 함
        properties.setProperty(JCSMPProperties.CLIENT_NAME, solaceProperties.getClientName() + "-"  + "receiver");
        //endpoint에 등록되어 있는 subscription으로 인해 발생하는 에러 무시
        properties.setProperty(JCSMPProperties.IGNORE_DUPLICATE_SUBSCRIPTION_ERROR, true);
        return properties;
    }


    public void restSendMessage(String targetSystem, String eventName, String topicName, String payload) {
        try {
            this.executor.sendIndividualMessage(targetSystem, eventName, topicName, payload);
        } catch (JCSMPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendMessage(String topicName, String paylod) {
        try {
            this.executor.sendMessage(topicName, paylod);
        } catch (JCSMPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean cleanUp() {

        if(!this.messageProducer.isClosed()) {
            this.messageProducer.close();
        }

        if(!this.session.isClosed()) {
            this.session.closeSession();
        }
        return true;
    }




}
