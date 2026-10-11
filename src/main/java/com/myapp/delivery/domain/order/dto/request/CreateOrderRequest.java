package com.myapp.delivery.domain.order.dto.request;

import com.myapp.delivery.domain.order.entity.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateOrderRequest {

    @NotNull
    private Long menuId;
    @Min(value = 1, message = "1개 이상부터 주문해주세요")
    private int quantity;
    @Valid @NotNull(message = "배송지는 필수입니다.")
    private AddressRequest address; // city, street, zipCode

    @Getter
    @ToString
    public static class AddressRequest {

        @NotBlank(message = "city는 필수입니다.")
        private String city;
        @NotBlank(message = "street는 필수입니다.")
        private String street;
        @NotBlank(message = "zipCode는 필수입니다.")
        private String zipCode;

        public Address toAddress() {
            return Address.of(this.city, this.street, this.zipCode);
        }
    }
}