package kitchenpos.tableGroup.ui;

import kitchenpos.tableGroup.application.TableGroupService;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupRequest> create(@RequestBody final TableGroupRequest tableGroup) {
        final TableGroupRequest created = tableGroupService.create(tableGroup);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity handleRuntimeException(IllegalArgumentException illegalArgumentException) {
		return ResponseEntity.badRequest().build();
	}
}
