package me.johnnypixelz.TWInventory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.johnnypixelz.TWInventory.Listener.DebugListener;
import me.johnnypixelz.TWInventory.Listener.PlayerListener;
import net.minecraft.server.v1_10_R1.ChatComponentText;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;

public class PlayerWeight extends JavaPlugin {
	public boolean debug;
	public WeightManager wM;
	public static PlayerWeight plugin;

	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		reloadConfig();
                TWIConfig.readConfig(this);
		if (getConfig().getBoolean("Debug")) {
			new DebugListener(this);
		}
		new PlayerListener(this);
		this.wM = new WeightManager(this);

		if (plugin.getConfig().getBoolean("Enable Action Bar")) {
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

				public void run() {
					AboveBar();
				}
			}, 100L, 40L);
		}
	}

	public void AboveBar() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			double weight = wM.getWeight(player);
			DecimalFormatSymbols symbol = new DecimalFormatSymbols();
			symbol.setDecimalSeparator('.');

			String message = ChatColor.translateAlternateColorCodes('&', TWIConfig.ACTION_BAR_MSG)
					.replace("<weight>", new DecimalFormat("#.##", symbol).format(weight))
					.replace("<maxweight>", String.valueOf(wM.getMaxW())).replace("<weightpercent>",
							String.valueOf((int) (wM.calculateWeightPercentage(weight, player).floatValue() * 100.0F)));

			sendActionText(player, message);
		}
	}

	public void sendActionText(Player player, String message) {
		PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if ((label.equalsIgnoreCase("weight")) && ((sender instanceof Player))) {

			Player p = (Player) sender;
			if (args.length == 0) {

				double weight = this.wM.getWeight(p);
				DecimalFormatSymbols symbol = new DecimalFormatSymbols();
				symbol.setDecimalSeparator('.');

				String message = ChatColor.translateAlternateColorCodes('&', TWIConfig.PREFIX)
						+ translateColor(TWIConfig.CMD_MSG)
								.replace("<weight>", new DecimalFormat("#.##", symbol).format(weight))
								.replace("<maxweight>", String.valueOf(this.wM.getMaxW()))
								.replace("<weightpercent>", String.valueOf(
										(int) (this.wM.calculateWeightPercentage(weight, p).floatValue() * 100.0F)));
				p.sendMessage(message);
				return true;
			}

			if (args.length == 1) {

				if ((args[0].equalsIgnoreCase("debug")) && (p.hasPermission("twi.debug"))) {

					if (getConfig().getBoolean("Debug")) {

						if (!this.debug) {

							this.debug = true;
						} else if (this.debug) {

							this.debug = false;
						}
						return true;
					}
					if (!getConfig().getBoolean("Debug")) {
						p.sendMessage(translateColor(TWIConfig.NODEBUG_MSG));
						return true;
					}
				}
				if ((args[0].equalsIgnoreCase("reload")) && (p.hasPermission("twi.reload"))) {

					getServer().getPluginManager().disablePlugin(this);
					getServer().getPluginManager().enablePlugin(this);

					for (Player player : getServer().getOnlinePlayers()) {

						this.wM.handler(player);
					}
					sender.sendMessage(translateColor(TWIConfig.RELOAD_MSG));
					return true;
				}
			}
		}
		sender.sendMessage(translateColor(TWIConfig.NOPERM_MSG));
		return false;
	}

	public String translateColor(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public void onDisable() {
		Bukkit.getScheduler().cancelAllTasks();
	}
}
