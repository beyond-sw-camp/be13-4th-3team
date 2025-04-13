package com.beyond3.yyGang.order.repository;

import com.beyond3.yyGang.order.Order;
import com.beyond3.yyGang.order.OrderOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderOptionRepository extends JpaRepository<OrderOption, Long> {

    List<OrderOption> findByOrderId(Order orderId);


}