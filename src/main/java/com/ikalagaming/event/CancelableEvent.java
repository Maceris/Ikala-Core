package com.ikalagaming.event;

import lombok.Getter;
import lombok.Setter;

/** An abstract event that can be canceled. */
@Getter
@Setter
public abstract class CancelableEvent extends Event {
    /**
     * Whether the event has been canceled. If it has, it will not be processed.
     *
     * @param canceled Whether we want to cancel the event.
     * @return Whether the event is canceled.
     */
    private boolean canceled = false;
}
