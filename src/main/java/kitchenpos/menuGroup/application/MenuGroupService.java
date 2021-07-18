package kitchenpos.menuGroup.application;

import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
		MenuGroup menuGroup = menuGroupRepository.save(menuGroupRequest.toMenuGroup());

    	return MenuGroupResponse.of(menuGroup);
    }

    public List<MenuGroupResponse> list() {
    	List<MenuGroup> menuGroups = menuGroupRepository.findAll();

    	return menuGroups.stream()
			.map(menuGroup -> MenuGroupResponse.of(menuGroup))
			.collect(Collectors.toList());
    }
}
