package net.yzimroni.customeventhandler.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.event.EventPriority;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomEventHandler {
	
	EventPriority priority() default EventPriority.NORMAL;
	boolean ignoreCancelled() default false;

}
