package net.yzimroni.eventfilter.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import net.yzimroni.eventfilter.plugin.test.Test;

public class EventFilterPlugin extends JavaPlugin {
	
	
	@Override
	public void onEnable() {
		new Test(this);
	}

}
