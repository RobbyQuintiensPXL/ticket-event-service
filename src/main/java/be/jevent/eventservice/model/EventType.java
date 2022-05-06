package be.jevent.eventservice.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EventType {

    CONCERT("Concert"), NIGHTLIFE("Nightlife"), SPORTS("Sports"),
    GAMING("Gaming"), CULTURE("Culture");

    private final String type;

    private EventType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    private static final Map<String, EventType> nameToValueMap =
            new HashMap<String, EventType>();

    static {
        for (EventType value : EnumSet.allOf(EventType.class)) {
            nameToValueMap.put(value.name(), value);
        }
    }

    public static EventType forName(String name) {
        return nameToValueMap.get(name);
    }

}
