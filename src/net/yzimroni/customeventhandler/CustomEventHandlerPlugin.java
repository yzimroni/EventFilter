package net.yzimroni.customeventhandler;

import org.bukkit.plugin.java.JavaPlugin;

import net.yzimroni.customeventhandler.test.Test;

public class CustomEventHandlerPlugin extends JavaPlugin {
	
	
	@Override
	public void onEnable() {
		new Test(this);
	}

}
