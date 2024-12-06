package me.mchiappinam.pdghx1;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class Listeners implements Listener {

	private Main plugin;
	public Listeners(Main main) {
		plugin=main;
	}
	
	@EventHandler//(priority=EventPriority.MONITOR)
	public void onCraft(CraftItemEvent e) {
		
	}
}
