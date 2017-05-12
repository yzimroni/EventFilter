package net.yzimroni.eventfilter;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

public abstract class EventFilter {
	
	public boolean event(Event e) {
		return true;
	}
	
	public boolean player(Player p) {
		return true;
	}
	
	public boolean location(Location l) {
		return true;
	}
	
	public boolean entity(Entity e) {
		return true;
	}
	
	public boolean inventory(Inventory i) {
		return true;
	}
	
	public boolean world(World w) {
		return true;
	}

}
