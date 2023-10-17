package com.tsh.dk.cmn.solaceTester.domain.solTest.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Getter
@Setter
@Entity(name="SOL_TEST_INF")
public class SolTestInf {
    @Id
    public String messageId;
    public String testId;
    public String senderId;
    public Integer loopSeq;
    public Integer messageSeq;
    public String clientId;
    public String clientTyp;
    public String topic;
    public String queue;
    public Timestamp crtDt;
    public Timestamp fnlEvntDt;
}
