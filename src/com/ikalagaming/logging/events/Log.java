package com.ikalagaming.logging.events;

import com.ikalagaming.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A message or error was logged and needs to be recorded. Dispatched by the
 * logging system after it constructs a message from logs if the logs are of
 * high enough level.
 *
 * @author Ches Burks
 *
 */
@AllArgsConstructor
public class Log extends Event {

	/**
	 * The info to store or display
	 */
	@Getter
	private final String message;

}
