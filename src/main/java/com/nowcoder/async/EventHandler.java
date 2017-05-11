package com.nowcoder.async;

import java.util.List;

/**
 * Created by Chen on 10/05/2017.
 */
public interface EventHandler {
    void doHandle(EventModel event);
    List<EventType> getSupportEventTypes();
}
