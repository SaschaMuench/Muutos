package de.sonnmatt.muutos.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class HierarchyListener {

	@PrePersist
	@PreUpdate
	public void setLevelAndTop(IHierarchyElement entity) {

		final IHierarchyElement parent = entity.getParent();

		// set level
		if (parent == null) {
			entity.setLevel(0);
		} else {
			entity.setLevel(parent.getLevel() + 1);
		}

		// set top
		if (parent == null) {
			entity.setTop(entity);
		} else {
			if (entity.isTop()) {
				entity.setTop(entity);
			} else {
				entity.setTop(parent.getTop());
			}
		}

		// set stucture
		List<String> struct = new ArrayList<>();
		if (parent != null) {
			struct.addAll(parent.getStructure());
		}
		struct.add(entity.getId());
		entity.setStructure(struct);
	}
}