package com.ikalagaming.event;

/**
 * A command, different from an event in a couple ways. These refer to active operations, "do X"
 * rather than "X happened". Commands are intended to be "processed" by a single handler, though
 * they can be listened to by any interested party as if they were regular events, who may wish to
 * cancel (or even un-cancel) them.
 *
 * <p>The official handler is called after all event handlers <b>except</b> {@link Order#MONITOR}
 * (which are intended to never modify events), and it is <b>only</b> called if the command has not
 * been canceled.
 */
public abstract class Command extends CancelableEvent {
    @Override
    public void fire() {
        EventManager.getInstance().fireCommand(this);
    }
}
