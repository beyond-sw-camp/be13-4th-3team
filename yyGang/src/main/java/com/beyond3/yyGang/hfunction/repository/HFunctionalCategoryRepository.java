package com.beyond3.yyGang.hfunction.repository;

import com.beyond3.yyGang.hfunction.HFunctionalCategory;
import com.beyond3.yyGang.nsupplement.NSupplement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HFunctionalCategoryRepository extends JpaRepository<HFunctionalCategory, Long> {

    List<HFunctionalCategory> findBynSupplement(NSupplement nSupplement);
}
