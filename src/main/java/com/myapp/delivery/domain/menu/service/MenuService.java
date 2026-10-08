package com.myapp.delivery.domain.menu.service;

import com.myapp.delivery.domain.menu.dto.response.MenuResponse;
import com.myapp.delivery.domain.menu.entity.Menu;
import com.myapp.delivery.domain.menu.repository.MenuRepository;
import com.myapp.delivery.domain.user.entity.User;
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
public class MenuService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    // 메뉴 추가
    @Transactional
    public MenuResponse addMenu(Long userId, String name, int price, String description) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ClientException(ErrorCode.USER_NOT_FOUND)
        );

        Menu menu = Menu.create(user, name, price, description);
        menuRepository.save(menu);

        return MenuResponse.from(menu);
    }

    // 메뉴 목록 조회
    public List<MenuResponse> findAll() {
        List<MenuResponse> menuResponses = new ArrayList<>();
        List<Menu> menus = menuRepository.findAll();
        for (Menu menu : menus) {
            menuResponses.add(MenuResponse.from(menu));
        }

        return menuResponses;
    }

    // 메뉴 단건 조회
    public MenuResponse findById(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new ClientException(ErrorCode.MENU_NOT_FOUND)
        );
        return MenuResponse.from(menu);
    }

    // 메뉴 수정
    @Transactional
    public MenuResponse update(Long loginUserId, Long id, String name, int price, String description) {
        Menu menu = menuRepository.findById(id).orElseThrow(
                () -> new ClientException(ErrorCode.MENU_NOT_FOUND)
        );
        if (!menu.getUser().getId().equals(loginUserId)) {
            throw new ClientException(ErrorCode.NOT_MENU_OWNER);
        }
        menu.update(name, price, description);
        // 수정된 시간이 반영되지 않으므로 직접 flush()를 날려 DB에 반영
        menuRepository.flush();

        return MenuResponse.from(menu);
    }


    // 메뉴 삭제
    @Transactional
    public void deleteById(Long loginUserId, Long id) {
        Menu menu = menuRepository.findById(id).orElseThrow(
                () -> new ClientException(ErrorCode.MENU_NOT_FOUND)
        );

        // 본인 메뉴만 삭제 가능
        if (!menu.getUser().getId().equals(loginUserId)) {
            throw new ClientException(ErrorCode.NOT_MENU_OWNER);
        }

        // delete(menu) 실행 시 Menu 엔티티 @SQLDelete에 적힌 쿼리 실행
        menuRepository.delete(menu);
    }
}