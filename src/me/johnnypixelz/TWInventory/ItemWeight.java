package me.johnnypixelz.TWInventory;

import org.bukkit.inventory.ItemStack;

public class ItemWeight extends ItemStack {
	private double weight;

	public ItemWeight(ItemStack i) {
		super(i);
		this.weight = (getConfigWeight() * getAmount());
	}

	public ItemWeight() {
		this.weight = 0.0D;
	}

	public static double getItemWeight(ItemStack i) {
		if (i == null) {
			return 0.0D;
		}
		return new ItemWeight(i).getWeight();
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getMaterial() {
		short durability = getDurability();
		if ((durability > 0) && (getType().getMaxDurability() <= 0)) {
			return getType().toString() + "," + durability;
		}
		return getType().toString();
	}

	private double getConfigWeight() {
		return PlayerWeight.plugin.getConfig().getDouble(getMaterial());
	}
}
