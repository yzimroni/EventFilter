package net.yzimroni.eventfilter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.plugin.EventExecutor;

public class FilteredEventExecutor implements EventExecutor {

	private Method method;
	private Class<?> eventClass;
	private EventFilter filter;

	public FilteredEventExecutor(Method method, Class<?> eventClass, EventFilter filter) {
		super();
		this.method = method;
		this.eventClass = eventClass;
		this.filter = filter;
	}

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		try {
			if (!eventClass.isAssignableFrom(event.getClass())) {
				return;
			}
			if (!checkEvent(event)) {
				return;
			}
			method.invoke(listener, event);
		} catch (InvocationTargetException ex) {
			throw new EventException(ex.getCause());
		} catch (Throwable t) {
			throw new EventException(t);
		}
	}
	
	private boolean checkEvent(Event e) {
		if (!filter.event(e)) {
			return false;
		}
		if (!checkPlayer(e)) {
			return false;
		}
		if (!checkBlock(e)) {
			return false;
		}
		if (e instanceof EntityEvent) {
			return filter.entity(((EntityEvent) e).getEntity());
		}
		if (!checkInventory(e)) {
			return false;
		}
		if (!checkWorld(e)) {
			return false;
		}
		return true;
	}
	
	private boolean checkPlayer(Event e) {
		if (e instanceof PlayerEvent) {
			return filter.player(((PlayerEvent) e).getPlayer());
		}
		if (e instanceof BlockPlaceEvent) {
			return filter.player(((BlockPlaceEvent) e).getPlayer());
		}
		if (e instanceof BlockBreakEvent) {
			return filter.player(((BlockBreakEvent) e).getPlayer());
		}
		if (e instanceof BlockDamageEvent) {
			return filter.player(((BlockDamageEvent) e).getPlayer());
		}
		if (e instanceof BlockIgniteEvent) {
			return filter.player(((BlockIgniteEvent) e).getPlayer());
		}
		if (e instanceof SignChangeEvent) {
			return filter.player(((SignChangeEvent) e).getPlayer());
		}
		if (e instanceof HangingPlaceEvent) {
			return filter.player(((HangingPlaceEvent) e).getPlayer());
		}
		if (e instanceof FurnaceExtractEvent) {
			return filter.player(((FurnaceExtractEvent) e).getPlayer());
		}
		if (e instanceof InventoryInteractEvent) {
			if (((InventoryInteractEvent) e).getWhoClicked() instanceof Player) {
				return filter.player((Player) ((InventoryInteractEvent) e).getWhoClicked());
			}
		}
		//TODO
		return true;
	}
	
	private boolean checkBlock(Event e) {
		if (e instanceof BlockEvent) {
			return filter.location(((BlockEvent) e).getBlock().getLocation());
		}
		if (e instanceof EntityChangeBlockEvent) {
			return filter.location(((EntityChangeBlockEvent) e).getBlock().getLocation());
		}
		//TODO
		return true;
	}
	
	private boolean checkInventory(Event e) {
		if (e instanceof InventoryEvent) {
			return filter.inventory(((InventoryEvent) e).getInventory());
		}
		if (e instanceof InventoryPickupItemEvent) {
			return filter.inventory(((InventoryPickupItemEvent) e).getInventory());
		}
		//TODO
		return true;
	}
	
	private boolean checkWorld(Event e) {
		if (e instanceof WorldEvent) {
			return filter.world(((WorldEvent) e).getWorld());
		}
		if (e instanceof WeatherEvent) {
			return filter.world(((WeatherEvent) e).getWorld());
		}
		//TODO
		return true;
	}

}
