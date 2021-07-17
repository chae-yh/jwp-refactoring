package kitchenpos.tableGroup.dto;

import java.time.LocalDateTime;

import kitchenpos.tableGroup.domain.TableGroup;

public class TableGroupResponse {
	private Long id;
	private LocalDateTime createdDate;

	public TableGroupResponse() {
	}

	public TableGroupResponse(Long id, LocalDateTime createdDate) {
		this.id = id;
		this.createdDate = createdDate;
	}

	public static TableGroupResponse of(TableGroup tableGroup) {
		return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate());
	}
}