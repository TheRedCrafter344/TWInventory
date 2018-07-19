package me.johnnypixelz.TWInventory.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.johnnypixelz.TWInventory.PlayerWeight;

public class DebugListener implements Listener {
	private PlayerWeight plugin;

	public DebugListener(PlayerWeight plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (this.plugin.debug) {
			p.sendMessage("Speed: " + p.getWalkSpeed());
			p.sendMessage(
					"Percentage: " + this.plugin.wM.calculateWeightPercentage(this.plugin.wM.getWeight(p), p) + "%");
		}
	}
}
