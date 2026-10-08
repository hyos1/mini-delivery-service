package com.myapp.delivery.global;

import com.myapp.delivery.global.exception.ClientException;
import com.myapp.delivery.global.exception.ErrorCode;
import com.myapp.delivery.global.exception.ServerException;
import com.myapp.delivery.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> clientExHandler(ClientException e) {
        log.info("클라이언트 예외 발생: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> apiResponse = ApiResponse.fail(e.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> serverExHandler(ServerException e) {
        log.info("서버 예외 발생: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> apiResponse = ApiResponse.fail(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Map<String, String>>> MethodArgumentNotValidExHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiResponse<Map<String, String>> apiResponse = ApiResponse.fail("입력값 검증에 실패했습니다.", errors);
        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }
    // todo 지원하지 않는 Http Method도 처리하기 - HttpRequestMethodNotSupportedException

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> exHandler(Exception e) {
        log.error("해결할 수 없는 예외 발생: ", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }
}