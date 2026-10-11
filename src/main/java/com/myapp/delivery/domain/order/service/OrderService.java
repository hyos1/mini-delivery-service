package com.myapp.delivery.domain.order.service;

import com.myapp.delivery.domain.menu.entity.Menu;
import com.myapp.delivery.domain.menu.repository.MenuRepository;
import com.myapp.delivery.domain.order.dto.response.OrderResponse;
import com.myapp.delivery.domain.order.entity.Address;
import com.myapp.delivery.domain.order.entity.Order;
import com.myapp.delivery.domain.order.repository.OrderRepository;
import com.myapp.delivery.domain.payment.dto.response.PaymentResponse;
import com.myapp.delivery.domain.payment.entity.Payment;
import com.myapp.delivery.domain.payment.enums.PayMethod;
import com.myapp.delivery.domain.payment.repository.PaymentRepository;
import com.myapp.delivery.domain.user.entity.User;
import com.myapp.delivery.domain.user.enums.UserRole;
import com.myapp.delivery.domain.user.repository.UserRepository;
import com.myapp.delivery.global.exception.ClientException;
import com.myapp.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public OrderResponse createOrder(Long userId, Long menuId, int quantity, Address address) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND)
        );
        Menu menu = menuRepository.findByIdAndIsDeletedFalse(menuId).orElseThrow(
                () -> new ClientException(ErrorCode.MENU_NOT_FOUND)
        );

        // 주문 당시 메뉴 이름, 가격 데이터까지 포함 - 스냅샷
        Order order = Order.create(user, menu, quantity, address);
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    // 주문 목록 조회
    public List<OrderResponse> findAll(Long loginUserId) {
        User user = userRepository.findById(loginUserId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND)
        );

        List<Order> orders = new ArrayList<>();
        // 회원인 경우 본인이 한 주문만 조회
        if (user.getRole() == UserRole.CUSTOMER) {
            orders = orderRepository.findByUserId(user.getId());
        } else if (user.getRole() == UserRole.OWNER) {
            // 사장인 경우 본인 메뉴에 들어온 주문만 조회
            orders = orderRepository.findByMenu_User_Id(loginUserId);
        }
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            orderResponses.add(OrderResponse.from(order));
        }
        return orderResponses;
    }

    // 주문상태 변경 [결제 완료 -> 주문 수락]
    @Transactional
    public OrderResponse accept(Long loginUserId, Long orderId) {

        User user = userRepository.findById(loginUserId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND)
        );
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ClientException(ErrorCode.ORDER_NOT_FOUND)
        );

        // (OWNER 기능) - 자기 메뉴에 들어온 주문만 수정/삭제 가능
        if (!order.getMenu().getUser().getId().equals(loginUserId)) {
            throw new ClientException(ErrorCode.INVALID_MENU_ACCESS);
        }

        // 엔티티 내부 자체 검증 (결제완료 -> 주문수락)
        order.accept(); // 더티채킹

        return OrderResponse.from(order);
    }

    // 주문상태 변경 [주문수락 -> 배달완료]
    @Transactional
    public OrderResponse complete(Long loginUserId, Long orderId) {
        if (!userRepository.existsById(loginUserId)) {
            throw new ClientException(ErrorCode.USER_NOT_FOUND);
        }
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ClientException(ErrorCode.ORDER_NOT_FOUND)
        );

        // (OWNER 기능) - 자기 메뉴에 들어온 주문만 수정/삭제 가능
        if (!order.getMenu().getUser().getId().equals(loginUserId)) {
            throw new ClientException(ErrorCode.INVALID_MENU_ACCESS);
        }

        // 엔티티 내부 자체 검증 ("주문요청"상태일 때만 변경가능)
        order.complete();
        return OrderResponse.from(order);
    }

    // 주문 취소
    @Transactional
    public OrderResponse cancelOrder(Long loginUserId, Long orderId) {
        if (!userRepository.existsById(loginUserId)) {
            throw new ClientException(ErrorCode.USER_NOT_FOUND);
        }
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ClientException(ErrorCode.ORDER_NOT_FOUND)
        );

        // 본인 주문만 취소/수정 가능
        if (!order.getUser().getId().equals(loginUserId)) {
            throw new ClientException(ErrorCode.INVALID_ORDER_ACCESS);
        }

        // 엔티티 자체 검증 후 ORDERED -> CANCELED로 변경 ("주문요청"인 경우만 취소 가능)
        order.cancel();
        return OrderResponse.from(order);
    }

    @Transactional
    public PaymentResponse payment(Long loginUserId, Long orderId, PayMethod payMethod) {
        if (!userRepository.existsById(loginUserId)) {
            throw new ClientException(ErrorCode.USER_NOT_FOUND);
        }
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ClientException(ErrorCode.ORDER_NOT_FOUND)
        );

        // 본인 주문만 결제 가능
        if (!order.getUser().getId().equals(loginUserId)) {
            throw new ClientException(ErrorCode.INVALID_PAYMENT_ACCESS);
        }
        // 결제수단 CARD만 가능, "주문요청"일때만 가능
        Payment payment = Payment.create(order, payMethod);
        paymentRepository.save(payment);

        return PaymentResponse.from(payment);
    }
}