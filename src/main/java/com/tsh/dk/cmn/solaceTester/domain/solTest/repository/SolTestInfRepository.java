package com.tsh.dk.cmn.solaceTester.domain.solTest.repository;

import com.tsh.dk.cmn.solaceTester.domain.solTest.model.SolTestInf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin
public interface SolTestInfRepository extends JpaRepository<SolTestInf, Long> {
}

