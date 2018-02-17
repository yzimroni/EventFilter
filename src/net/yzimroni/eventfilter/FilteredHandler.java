package net.yzimroni.eventfilter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.TimedRegisteredListener;

/*
 * Methods are from org.bukkit.plugin.SimplePluginManager
 */
public class FilteredHandler implements Listener {

	private FilteredHandler() {

	}

	public static void registerEvents(Listener listener, Plugin plugin, EventFilter filter) {
		if (!plugin.isEnabled()) {
			throw new IllegalPluginAccessException("Plugin attempted to register " + listener + " while not enabled");
		}

		for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : createRegisteredListeners(listener,
				plugin, filter).entrySet()) {
			getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
		}
	}

	private static Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener,
			final Plugin plugin, EventFilter filter) {
		Validate.notNull(plugin, "Plugin can not be null");
		Validate.notNull(listener, "Listener can not be null");

		boolean useTimings = Bukkit.getServer().getPluginManager().useTimings();
		Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<Class<? extends Event>, Set<RegisteredListener>>();
		Set<Method> methods;
		try {
			Method[] publicMethods = listener.getClass().getMethods();
			Method[] privateMethods = listener.getClass().getDeclaredMethods();
			methods = new HashSet<Method>(publicMethods.length + privateMethods.length, 1.0f);
			for (Method method : publicMethods) {
				methods.add(method);
			}
			for (Method method : privateMethods) {
				methods.add(method);
			}
		} catch (NoClassDefFoundError e) {
			plugin.getLogger()
					.severe("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for "
							+ listener.getClass() + " because " + e.getMessage() + " does not exist.");
			return ret;
		}

		for (final Method method : methods) {
			final FilteredEventHandler eh = method.getAnnotation(FilteredEventHandler.class);
			if (eh == null)
				continue;
			// Do not register bridge or synthetic methods to avoid event duplication
			// Fixes SPIGOT-893
			if (method.isBridge() || method.isSynthetic()) {
				continue;
			}
			final Class<?> checkClass;
			if (method.getParameterTypes().length != 1
					|| !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
				plugin.getLogger()
						.severe(plugin.getDescription().getFullName()
								+ " attempted to register an invalid FilteredEventHandler method signature \""
								+ method.toGenericString() + "\" in " + listener.getClass());
				continue;
			}
			final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
			method.setAccessible(true);
			Set<RegisteredListener> eventSet = ret.get(eventClass);
			if (eventSet == null) {
				eventSet = new HashSet<RegisteredListener>();
				ret.put(eventClass, eventSet);
			}

			FilteredEventExecutor executor = new FilteredEventExecutor(method, eventClass, filter);
			if (useTimings) {
				eventSet.add(
						new TimedRegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
			} else {
				eventSet.add(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
			}
		}

		return ret;

	}

	private static HandlerList getEventListeners(Class<? extends Event> type) {
		try {
			Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
			method.setAccessible(true);
			return (HandlerList) method.invoke(null);
		} catch (Exception e) {
			throw new IllegalPluginAccessException(e.toString());
		}
	}

	private static Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
		try {
			clazz.getDeclaredMethod("getHandlerList");
			return clazz;
		} catch (NoSuchMethodException e) {
			if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class)
					&& Event.class.isAssignableFrom(clazz.getSuperclass())) {
				return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
			} else {
				throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName()
						+ ". Static getHandlerList method required!");
			}
		}
	}

}
