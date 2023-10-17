package com.tsh.dk.cmn.solaceTester.receiver;
//package com.abs.cmn.test.receiver;
//
//import com.abs.cmn.test.ApplicationProperties;
//import com.abs.cmn.test.SolaceProperties;
//import com.abs.cmn.test.util.importQueue.ImportQueueList;
//import com.solacesystems.jcsmp.JCSMPFactory;
//import com.solacesystems.jcsmp.JCSMPProperties;
//import com.solacesystems.jcsmp.JCSMPSession;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.ArrayList;
//import java.util.concurrent.CountDownLatch;
//
//@Slf4j
//public class ReceiverManager implements Runnable {
//	private JCSMPProperties properties = new JCSMPProperties();
//
//    private JCSMPSession session;
//
//    private int thread_count = 2;
//    private CountDownLatch latch = new CountDownLatch(thread_count);
//	private long timeout = 1000;
//
//	private ImportQueueList iql;
//	private ArrayList<String> queueList;
//
//	//Exclusive Queue
//	private String queue_name;
//
//	private SolaceProperties solaceProperties = SolaceProperties.getInstance();
//    private ApplicationProperties applicationProperties = ApplicationProperties.getInstance();
//
//    @Override
//	public void run() {
//    	log.info("ReceiverMain Thread Start");
//
//    	try {
//    		iql = new ImportQueueList(solaceProperties.getHost(),
//                                    solaceProperties.getMsgVpn(), solaceProperties.getClientUserName(),
//					                solaceProperties.getClientPassWord(),
//                                    applicationProperties.getReceiverQueuePrefix()
//                                    );
//    		queueList = iql.getQueueList();
//    		queue_name = queueList.get(0);
//
//    		//실행 모듈명이 들어간 QList만 받아옴
//    		for(String queue_name : queueList) {
//    			log.info("Queue # "+queue_name);
//    		}
//
//    		//JCSMP Property 설정
//    		properties.setProperty(JCSMPProperties.HOST, solaceProperties.getHost());
//    		//solace msgVpn명
//			properties.setProperty(JCSMPProperties.VPN_NAME, solaceProperties.getMsgVpn());
//			//solace msgVpn에 접속할 클라이언트사용자명
//			properties.setProperty(JCSMPProperties.USERNAME, solaceProperties.getClientUserName());
//			//solace msgVpn에 접속할 클라이언트사용자 패스워드(생략 가능)
//			properties.setProperty(JCSMPProperties.PASSWORD, solaceProperties.getClientPassWord());
//			//Allication client name 설정 - 동일 msgVpn 내에서 uniq 해야 함
//			properties.setProperty(JCSMPProperties.CLIENT_NAME, solaceProperties.getClientName() + "-"  + "receiver");
//			//endpoint에 등록되어 있는 subscription으로 인해 발생하는 에러 무시
//			properties.setProperty(JCSMPProperties.IGNORE_DUPLICATE_SUBSCRIPTION_ERROR, true);
//
//			//SpringJCSMPFactory를 이용한 JCSMPSession 생성(JCSMPFactory 사용하는 것과 동일 -> session = JCSMPFactory.onlyInstance().createSession(properties);)
//			session = JCSMPFactory.onlyInstance().createSession(properties);
//			//session 연결 - Application별로 최소 연결 권장(쓰레드를 사용할 경우 공유 사용 권장)
//			session.connect();
//
////			//Queue - SolAdmin에서 생성한 queue에 접속, SolAdmin에 생성되지 않은 경우 Application에서 생성
////			final Queue queue = JCSMPFactory.onlyInstance().createQueue(queue_name);
////
////			/*
////			 * EndPoint 설정
////			 * - SolAdmin에서 설정이 되어 있는 경우 Applicaiton에서는 사용하지 않아도 됨(사용할 경우 SolAdmin 화면과 동일하게 구성)
////			 * - SolAdmin에 설정이 없는 경우 Application에서 설정한 값으로 설정됨
////			 */
////			final EndpointProperties endpointProps = new EndpointProperties();
////			/* Endpoint(queue, topic) 설정 - solAdmin 화면에서 설정한 값과 동일해야 함 */
////			//Endpoint(Queue) 권한 설정
////	        endpointProps.setPermission(EndpointProperties.PERMISSION_CONSUME);
////	        //Endpoint(Queue) accesstype 설정
////	        endpointProps.setAccessType(EndpointProperties.ACCESSTYPE_NONEXCLUSIVE);
////	        //Endpoint(Queue) 용량 설정
//////			endpointProps.setQuota(100);
////	        //Endpoint provisioning - solAdmin 에 생성된 Endpoint 가 있으므로 "FLAG_IGNORE_ALREADY_EXISTS" 사용)
////	        session.provision(queue, endpointProps, JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);
//
//	    	for(int i=0; i<thread_count; i++) {
//				new Thread(new Receiver(latch, session, queueList, PropertyMain.getInstance().getModuleName(), "Receiver-"+PropertyMain.getInstance().getModuleName()+"-"+i)).start();
////				Thread.sleep(timeout);
//			}
//
////	    	latch.await();
//    	} catch(Exception e) {
//    		e.printStackTrace();
////    		if (!session.isClosed()) try { session.closeSession(); } catch (Exception e1) {}
//    	}
//    }
//}
