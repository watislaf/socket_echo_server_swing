package Server;

import java.util.HashMap;
import java.util.Map;

public enum ChangesEvent {
    test(0),
    newUserHello(1),
    smallBallCreated(2),
    bigBallCreated(3),
    bigCubeCreated(4),
    smallCubeCreated(5),
    bigBallRemoved(6),
    bigCubeRemoved(7),
    smallBallRemoved(8),
    smallCubeRemoved(9),
    boomCreated(10);

    private final int value;
    private static Map map = new HashMap<>();

    static {
        for (ChangesEvent type : ChangesEvent.values()) {
            map.put(type.value, type);
        }
    }

    public static ChangesEvent valueOf(int pageType) {
        return (ChangesEvent) map.get(pageType);
    }

    ChangesEvent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
