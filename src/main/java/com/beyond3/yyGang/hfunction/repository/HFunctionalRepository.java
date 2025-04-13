package com.beyond3.yyGang.hfunction.repository;

import com.beyond3.yyGang.hfunction.HFunctionName;
import com.beyond3.yyGang.hfunction.HFunctionalItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HFunctionalRepository extends JpaRepository<HFunctionalItem, Long> {

    Optional<HFunctionalItem> findByHealthName(HFunctionName hFunctionName);
}
