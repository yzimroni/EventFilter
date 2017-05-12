package net.yzimroni.eventfilter.plugin.test;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.yzimroni.eventfilter.EventFilter;
import net.yzimroni.eventfilter.FilteredEventHandler;
import net.yzimroni.eventfilter.FilteredHandler;
import net.yzimroni.eventfilter.plugin.EventFilterPlugin;

public class Test implements Listener {
	
	private EventFilterPlugin plugin;
	
	public Test(EventFilterPlugin plugin) {
		this.plugin = plugin;
		FilteredHandler.registerEvents(this, plugin, new EventFilter() {
			@Override
			public boolean player(Player p) {
				return p.getLocation().getY() < 70;
			}
		});
	}
	
	@FilteredEventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Bukkit.broadcastMessage(e.getPlayer() + " say something on y < 70");
	}

}
