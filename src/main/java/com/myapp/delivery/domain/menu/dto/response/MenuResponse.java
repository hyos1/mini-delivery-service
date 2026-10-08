package com.myapp.delivery.domain.menu.dto.response;

import com.myapp.delivery.domain.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MenuResponse {

    private Long id;
    private String name;
    private int price;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getDescription(), menu.getCreatedAt(), menu.getModifiedAt());
    }
}