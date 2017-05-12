package net.yzimroni.customeventhandler;

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
		return false;
	}
	
	public boolean check(Location l) {
		return false;
	}
	
	public boolean check(Entity e) {
		return false;
	}
	
	public boolean check(Inventory i) {
		return false;
	}
	
	public boolean check(World w) {
		return false;
	}

}
