package kitchenpos.domain;

import java.util.Objects;

public class MenuGroupRequest {
    private Long id;
    private String name;

    public MenuGroupRequest() {
	}

    public MenuGroupRequest(String name) {
    	this.name = name;
	}

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MenuGroupRequest menuGroup = (MenuGroupRequest)o;
		return Objects.equals(id, menuGroup.id) &&
			Objects.equals(name, menuGroup.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}