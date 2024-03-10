package com.ikalagaming.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An event created for unit tests.
 *
 * @author Ches Burks
 */
@AllArgsConstructor
@Getter
public class CustomEvent extends Event {
    private final String testString;
}
