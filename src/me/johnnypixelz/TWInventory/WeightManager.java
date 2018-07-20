package me.johnnypixelz.TWInventory;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WeightManager {
	private PlayerWeight plugin;
	private float newspeed;
	
	public WeightManager(PlayerWeight plugin) {
		this.plugin = plugin;
	}

	public double getWeight(Player p) {
		double weight = 0.0D;
		ItemStack[] arrayOfItemStack;
		int i = (arrayOfItemStack = p.getInventory().getContents()).length;
		for (int j = 0; j < i; j++) {
			ItemStack o = arrayOfItemStack[j];
			weight += ItemWeight.getItemWeight(o);
		}
		i = (arrayOfItemStack = p.getInventory().getArmorContents()).length;
		for (int j = 0; j < i; j++) {
			ItemStack o = arrayOfItemStack[j];
			weight += ItemWeight.getItemWeight(o);
		}
		return weight;
	}

	public void handler(Player p) {
		if (p.hasPermission("twi.bypass")) {
			return;
		}
		
		List<String> weightlist = TWIConfig.WEIGHT_LEVELS;
		
		Double weight = getWeight(p);
		
		for (String t : weightlist) {
			String[] parts = t.split(" ");
			Integer configweight = Integer.parseInt(parts[0]);
			
			if (configweight <= weight) {
				setNewSpeed(Float.parseFloat(parts[1]));
			} else {
				p.setWalkSpeed(getNewSpeed()/5);
				calculateWeightPercentage(weight, p);
				break;
			}
		}
	}
	private void setNewSpeed(Float a) {
		newspeed = a;
	}
	private float getNewSpeed() {
		return newspeed;
	}

	public Float calculateWeightPercentage(double weight, Player p) {
		float weightPercent = (float) weight / getMaxW();
		if (TWIConfig.USE_XP_BAR) {
			setExp(Float.valueOf(weightPercent), p);
		}
		return Float.valueOf(weightPercent);
	}

	public void setExp(Float weightPercent, Player p) {
		if (weightPercent.floatValue() > 1.0F) {
			p.setExp(1.0F);
		} else {
			p.setExp(weightPercent.floatValue());
		}
	}

	public int getMaxW() {
		return (int) TWIConfig.MAX_WEIGHT;
	}

	public float speed(double percent) {
		return (float) (0.2D * percent);
	}

}
