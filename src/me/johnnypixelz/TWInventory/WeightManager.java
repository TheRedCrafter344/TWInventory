package me.johnnypixelz.TWInventory;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class WeightManager {
	private PlayerWeight plugin;
	private int maxWeight;
	private double lessThan;
	private double between1;
	private double between1_1;
	private double between2;
	private double between2_1;
	private double biggerThan;
	private double speedPercent;
	private double speedPercent1;
	private double speedPercent2;
	private double speedPercent3;
	private HashMap<String, Integer> previousWeight = new HashMap<>();

	public WeightManager(PlayerWeight plugin) {
		this.plugin = plugin;
		loadConfigVariables();
	}

	public double getWeight(Player p) {
		double weight = 0.0D;
		ItemStack[] arrayOfItemStack;
		int i = (arrayOfItemStack = p.getInventory().getContents()).length;
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
		int previousSector = 0;
		int presentSector = getSector(calculateWeightPercentage(getWeight(p), p).floatValue());
		if (!this.plugin.getConfig().getBoolean("Disable Messages", false)) {
			if (this.previousWeight.containsKey(p.getName())) {
				previousSector = ((Integer) this.previousWeight.put(p.getName(), Integer.valueOf(presentSector)))
						.intValue();
			} else {
				this.previousWeight.put(p.getName(), Integer.valueOf(presentSector));
			}
			if ((presentSector > previousSector) || ((previousSector > 1) && (presentSector == 1))) {
				p.sendMessage(announce(presentSector));
			}
		}
		calculateSpeed(presentSector, p);
	}

	private String announce(int sector) {
		switch (sector) {
		case 1:

			return ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Prefix"))
					+ this.plugin.translateColor(this.plugin.getConfig().getString("Less And Equal To.Message"));
		case 2:
			return ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Prefix"))
					+ this.plugin.translateColor(this.plugin.getConfig().getString("Between.Message"));
		case 3:
			return ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Prefix"))
					+ this.plugin.translateColor(this.plugin.getConfig().getString("Between1.Message"));
		case 4:
			return ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("Prefix"))
					+ this.plugin.translateColor(this.plugin.getConfig().getString("Bigger Than.Message"));
		}
		return null;
	}

	public Float calculateWeightPercentage(double weight, Player p) {
		float weightPercent = (float) weight / getMaxW();
		if (this.plugin.getConfig().getBoolean("Enable XP Bar", true)) {
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

	public void calculateSpeed(int sector, Player p) {
		switch (sector) {
		case 1:
			p.setWalkSpeed(speed(this.speedPercent));
			p.setFlySpeed(speed(this.speedPercent));
			p.removePotionEffect(PotionEffectType.JUMP);
			break;
		case 2:
			p.setWalkSpeed(speed(this.speedPercent1));
			p.setFlySpeed(speed(this.speedPercent1));
			p.removePotionEffect(PotionEffectType.JUMP);
			break;
		case 3:
			p.setWalkSpeed(speed(this.speedPercent2));
			p.setFlySpeed(speed(this.speedPercent2));
			p.removePotionEffect(PotionEffectType.JUMP);
			break;
		case 4:
			p.setWalkSpeed(speed(this.speedPercent3));
			p.setFlySpeed(speed(this.speedPercent3));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
		}
	}

	private int getSector(float weightPercent) {
		if (weightPercent <= this.lessThan) {
			return 1;
		}
		if ((weightPercent >= this.between1) && (weightPercent <= this.between1_1)) {
			return 2;
		}
		if ((weightPercent >= this.between2) && (weightPercent <= this.between2_1)) {
			return 3;
		}
		if (weightPercent > this.biggerThan) {
			return 4;
		}
		return 0;
	}

	public int getMaxW() {
		return this.maxWeight;
	}

	public float speed(double percent) {
		return (float) (0.2D * percent);
	}

	public void loadConfigVariables() {
		this.maxWeight = this.plugin.getConfig().getInt("Max Weight");
		this.lessThan = (this.plugin.getConfig().getDouble("Less And Equal To.Percentage") / 100.0D);
		this.speedPercent = (this.plugin.getConfig().getDouble("Less And Equal To.SpeedPercent") / 100.0D);
		splitBetween(this.plugin.getConfig().getString("Between.Percentage"), 1);
		this.speedPercent1 = (this.plugin.getConfig().getDouble("Between.SpeedPercent") / 100.0D);
		splitBetween(this.plugin.getConfig().getString("Between1.Percentage"), 2);
		this.speedPercent2 = (this.plugin.getConfig().getDouble("Between1.SpeedPercent") / 100.0D);
		this.biggerThan = (this.plugin.getConfig().getDouble("Bigger Than.Percentage") / 100.0D);
		this.speedPercent3 = (this.plugin.getConfig().getDouble("Bigger Than.SpeedPercent") / 100.0D);
	}

	public void splitBetween(String a, int p) {
		String[] between = a.split(",");
		if (p == 1) {
			this.between1 = (Double.parseDouble(between[0]) / 100.0D);
			this.between1_1 = (Double.parseDouble(between[1]) / 100.0D);
		}
		if (p == 2) {
			this.between2 = (Double.parseDouble(between[0]) / 100.0D);
			this.between2_1 = (Double.parseDouble(between[1]) / 100.0D);
		}
	}
}
