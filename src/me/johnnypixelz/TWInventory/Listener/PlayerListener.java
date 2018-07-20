package me.johnnypixelz.TWInventory.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.johnnypixelz.TWInventory.PlayerWeight;
import me.johnnypixelz.TWInventory.WeightManager;

public class PlayerListener implements Listener {
	private PlayerWeight plugin;
	public WeightManager wM;

	public PlayerListener(PlayerWeight plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	boolean task = false;

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		delay(p, 3L);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if ((event.getWhoClicked() instanceof Player)) {
			Player p = (Player) event.getWhoClicked();
			delay(p, 1L);
		}
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		this.plugin.wM.handler(p);
	}

	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent event) {
		Player p = event.getPlayer();
		delay(p, 1L);
	}

	@EventHandler
	public void onPlayerExpGain(PlayerExpChangeEvent event) {
		if (!TWIConfig.USE_XP_BAR) {
			return;
		}
		event.setAmount(0);
	}

	public void delay(final Player p, long ticks) {
		this.plugin.getServer().getScheduler().runTaskLater(this.plugin, new Runnable() {
			public void run() {
				PlayerListener.this.plugin.wM.handler(p);
			}
		}, ticks);
	}
}
