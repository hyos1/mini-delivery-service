package com.myapp.delivery.domain.order.repository;

import com.myapp.delivery.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long id);

    // 사장이 만든 메뉴들 중 주문이 들어온 주문 목록
//    @Query("SELECT o FROM Order o where o.menu.user.id = :loginUserId") //직접 JPQL 짠다면 이렇게
    List<Order> findByMenu_User_Id(@Param("loginUserId") Long loginUserId);
}