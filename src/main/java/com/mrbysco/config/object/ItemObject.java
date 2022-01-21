package com.mrbysco.config.object;

import java.util.Objects;

public final class ItemObject {
	private final int slot;
	private final String slotName;
	private final String itemLocation;
	private final String tag;
	private final int count;

	public ItemObject(int slot, String slotName, String resourceLocation, String tag, int count) {
		this.slot = slot;
		this.slotName = slotName;
		this.itemLocation = resourceLocation;
		this.tag = tag;
		this.count = count;
	}

	public ItemObject(int slot, String resourceLocation, String tag, int count) {
		this.slot = slot;
		this.slotName = null;
		this.itemLocation = resourceLocation;
		this.tag = tag;
		this.count = count;
	}

	public int slot() {
		return slot;
	}

	public String slotName() {
		return itemLocation;
	}

	public String itemLocation() {
		return itemLocation;
	}

	public String tag() {
		return tag;
	}

	public int count() {
		return count;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (ItemObject) obj;
		return this.slot == that.slot &&
				Objects.equals(this.itemLocation, that.itemLocation) &&
				Objects.equals(this.tag, that.tag) &&
				this.count == that.count;
	}

	@Override
	public int hashCode() {
		return Objects.hash(slot, itemLocation, tag, count);
	}

	@Override
	public String toString() {
		return "ItemObject[" +
				"slot=" + slot + ", " +
				"resourceLocation=" + itemLocation + ", " +
				"tag=" + tag + ", " +
				"count=" + count + ']';
	}
}
