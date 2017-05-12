package net.yzimroni.eventfilter;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

public abstract class EventFilter {
	
	public boolean check(Event e) {
		return true;
	}
	
	public boolean check(Player p) {
		return true;
	}
	
	public boolean check(Location l) {
		return true;
	}
	
	public boolean check(Entity e) {
		return true;
	}
	
	public boolean check(Inventory i) {
		return true;
	}
	
	public boolean check(World w) {
		return true;
	}

}
