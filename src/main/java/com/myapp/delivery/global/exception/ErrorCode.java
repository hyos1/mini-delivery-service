package com.myapp.delivery.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 회원
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "존재하지 않는 회원입니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // 메뉴
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴입니다."),
    NOT_MENU_OWNER(HttpStatus.FORBIDDEN, "본인의 메뉴만 수정/삭제할 수 있습니다."),
    // 메뉴 엔티티 자체 검증용
    INVALID_MENU_NAME(HttpStatus.BAD_REQUEST, "올바르지 않은 메뉴 이름입니다."),
    INVALID_MENU_PRICE(HttpStatus.BAD_REQUEST, "메뉴 가격은 1원 이상이어야 합니다."),

    // 주문
    // 주문 엔티티 자체 검증용
    INVALID_ORDER_QUANTITY(HttpStatus.BAD_REQUEST, "주문 수량은 1개 이상이어야 합니다."),

    // 결제

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 오류가 발생했습니다.")
    ;

    private final HttpStatus statusCode;
    private final String message;

    ErrorCode(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}