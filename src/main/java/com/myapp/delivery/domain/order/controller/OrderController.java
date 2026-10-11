package com.myapp.delivery.domain.order.controller;

import com.myapp.delivery.domain.order.dto.request.CreateOrderRequest;
import com.myapp.delivery.domain.order.dto.response.OrderResponse;
import com.myapp.delivery.domain.order.service.OrderService;
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
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @AuthenticationPrincipal Long userId, @Valid @RequestBody CreateOrderRequest request
    ) {
        OrderResponse response = orderService.createOrder(userId, request.getMenuId(), request.getQuantity(), request.getAddress().toAddress());
        ApiResponse<OrderResponse> apiResponse = ApiResponse.ok(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> findAll(@AuthenticationPrincipal Long userId) {
        List<OrderResponse> response = orderService.findAll(userId);
        ApiResponse<List<OrderResponse>> apiResponse = ApiResponse.ok(response);
        return ResponseEntity.ok(apiResponse);
    }

    // 주문 취소 (유저만 가능)
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @AuthenticationPrincipal Long userId, @PathVariable Long id) {
        OrderResponse orderResponse = orderService.cancelOrder(userId, id);
        ApiResponse<OrderResponse> apiResponse = ApiResponse.ok(orderResponse);
        return ResponseEntity.ok(apiResponse);
    }

    // OWNER - 주문상태 변경 [결제 완료 -> 주문 수락]
    @PatchMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<OrderResponse>> accept(
            @AuthenticationPrincipal Long loginUserId, @PathVariable Long id
    ) {
        OrderResponse orderResponse = orderService.accept(loginUserId, id);
        ApiResponse<OrderResponse> apiResponse = ApiResponse.ok(orderResponse);
        return ResponseEntity.ok(apiResponse);
    }

    // OWNER - 주문상태 변경 [주문 수락 -> 배달 완료]
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<OrderResponse>> complete(
            @AuthenticationPrincipal Long loginUserId, @PathVariable Long id
    ) {
        OrderResponse orderResponse = orderService.complete(loginUserId, id);
        ApiResponse<OrderResponse> apiResponse = ApiResponse.ok(orderResponse);
        return ResponseEntity.ok(apiResponse);
    }

}
