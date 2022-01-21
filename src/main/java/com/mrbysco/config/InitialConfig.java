package com.mrbysco.config;

import com.mrbysco.config.object.ItemObject;

import java.util.List;
import java.util.Objects;

public final class InitialConfig {
	private final List<ItemObject> initialList;

	public InitialConfig(List<ItemObject> initialList) {
		this.initialList = initialList;
	}

	public List<ItemObject> initialList() {
		return initialList;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (InitialConfig) obj;
		return Objects.equals(this.initialList, that.initialList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(initialList);
	}

	@Override
	public String toString() {
		return "InitialConfig[" +
				"initialList=" + initialList + ']';
	}

}
