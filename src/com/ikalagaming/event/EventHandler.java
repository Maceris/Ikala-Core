package com.ikalagaming.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark methods as being event handler methods. Based off
 * lahwran's fevents.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

	/**
	 * Define the order of the event handler relative to the default.
	 * <p>
	 * Order of execution from first to last:
	 * <ol>
	 * <li>EARLIEST
	 * <li>EARLY
	 * <li>DEFAULT
	 * <li>LATE
	 * <li>LATEST
	 * <li>MONITOR
	 * </ol>
	 *
	 * @return Returns this handler's priority
	 */
	Order order() default Order.DEFAULT;
}
