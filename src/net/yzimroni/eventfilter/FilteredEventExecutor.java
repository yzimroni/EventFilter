package net.yzimroni.eventfilter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerEvent;
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
		if (!filter.check(e)) {
			return false;
		}
		if (e instanceof PlayerEvent) {
			return filter.check(((PlayerEvent) e).getPlayer());
		}
		if (e instanceof BlockEvent) {
			return filter.check(((BlockEvent) e).getBlock().getLocation());
		}
		if (e instanceof EntityEvent) {
			return filter.check(((EntityEvent) e).getEntity());
		}
		if (e instanceof InventoryEvent) {
			return filter.check(((InventoryEvent) e).getInventory());
		}
		if (e instanceof WorldEvent) {
			return filter.check(((WorldEvent) e).getWorld());
		}
		return false;
	}

}
