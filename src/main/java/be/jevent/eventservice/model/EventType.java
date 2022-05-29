package be.jevent.eventservice.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EventType {

    CONCERT("Concert"), NIGHTLIFE("Nightlife"), SPORTS("Sports"),
    GAMING("Gaming"), CULTURE("Culture");

    private static final Map<String, EventType> nameToValueMap =
            new HashMap<>();

    static {
        for (EventType value : EnumSet.allOf(EventType.class)) {
            nameToValueMap.put(value.name(), value);
        }
    }

    private final String type;

    private EventType(String type) {
        this.type = type;
    }

    public static EventType forName(String name) {
        return nameToValueMap.get(name);
    }

    public String getType() {
        return type;
    }

}
