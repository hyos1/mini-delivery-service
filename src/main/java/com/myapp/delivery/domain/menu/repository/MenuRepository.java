package com.myapp.delivery.domain.menu.repository;

import com.myapp.delivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    // is_deleted 직접 쿼리에 붙여서 조회
    List<Menu> findAllByIsDeletedFalse();
    Optional<Menu> findByIdAndIsDeletedFalse(Long id);
}