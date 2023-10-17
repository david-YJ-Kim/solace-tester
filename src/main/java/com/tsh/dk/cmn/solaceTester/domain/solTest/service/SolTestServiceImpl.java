package com.tsh.dk.cmn.solaceTester.domain.solTest.service;

import com.tsh.dk.cmn.solaceTester.domain.solTest.model.SolTestInf;
import com.tsh.dk.cmn.solaceTester.domain.solTest.repository.SolTestInfRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;


@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class})
public class SolTestServiceImpl implements SolTestService{

    @Autowired
    private SolTestInfRepository solTestInfRepository;

    public void testInsert(){
        SolTestInf solTestInf = new SolTestInf();
        solTestInf.setMessageId("messageId");
        solTestInf.setTestId("testId");
        solTestInf.setSenderId("sender");
        solTestInf.setCrtDt(new Timestamp(System.currentTimeMillis()));
        solTestInf.setTopic("topic");
        solTestInf.setQueue("queue");
        solTestInf.setClientId("clinetId");
        solTestInf.setClientTyp("type");
        solTestInf.setLoopSeq(1);
        solTestInf.setMessageSeq(1);
        solTestInf.setFnlEvntDt(new Timestamp(System.currentTimeMillis()));

        solTestInfRepository.save(solTestInf);
    }
}
