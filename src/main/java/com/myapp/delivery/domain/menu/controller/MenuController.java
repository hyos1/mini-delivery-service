package com.myapp.delivery.domain.menu.controller;

import com.myapp.delivery.domain.menu.dto.request.CreateMenuRequest;
import com.myapp.delivery.domain.menu.dto.request.UpdateMenuRequest;
import com.myapp.delivery.domain.menu.dto.response.MenuResponse;
import com.myapp.delivery.domain.menu.service.MenuService;
import com.myapp.delivery.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 메뉴 생성 OWNER만 가능
    @PostMapping
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @AuthenticationPrincipal Long userId, @Valid @RequestBody CreateMenuRequest request) {
        MenuResponse menuResponse = menuService.addMenu(userId, request.getName(), request.getPrice(), request.getDescription());
        ApiResponse<MenuResponse> apiResponse = ApiResponse.ok(menuResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // 메뉴 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuResponse>>> findAll() {
        List<MenuResponse> menuResponses = menuService.findAll();
        ApiResponse<List<MenuResponse>> apiResponse = ApiResponse.ok(menuResponses);
        return ResponseEntity.ok(apiResponse);
    }

    // 메뉴 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuResponse>> findById(@PathVariable Long id) {
        MenuResponse menuResponses = menuService.findById(id);
        ApiResponse<MenuResponse> apiResponse = ApiResponse.ok(menuResponses);
        return ResponseEntity.ok(apiResponse);
    }

    // 메뉴 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(
            @PathVariable Long id,
            @AuthenticationPrincipal Long loginUserId,
            @Valid @RequestBody UpdateMenuRequest request) {
        MenuResponse menuResponses = menuService.update(loginUserId, id, request.getName(), request.getPrice(), request.getDescription());
        ApiResponse<MenuResponse> apiResponse = ApiResponse.ok(menuResponses);
        return ResponseEntity.ok(apiResponse);
    }

    // 메뉴 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, @AuthenticationPrincipal Long loginUserId) {
        log.info("컨트롤러에 넘어온 인증된 유저 정보 userId= {}", loginUserId);
        menuService.deleteById(loginUserId, id);
        ApiResponse<Void> apiResponse = ApiResponse.ok(null);
        return ResponseEntity.ok(apiResponse);
    }
}