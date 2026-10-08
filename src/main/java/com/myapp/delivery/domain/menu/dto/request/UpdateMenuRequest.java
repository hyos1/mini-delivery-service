package com.myapp.delivery.domain.menu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateMenuRequest {
    @NotBlank(message = "메뉴 이름은 필수입니다.")
    private String name;
    @Min(value = 1, message = "가격은 1원 이상이여야 합니다.")
    private int price;
    private String description;
}