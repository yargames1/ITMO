import java.util.Random;

public enum Location {
    SPACE_SHUTTLE_MODEL, MISSION_CONTROL_ROOM, PLANETARY_RESEARCH_LAB, ASTRONAUT_LIVING_MODULE, SPACEWALK_PREPARATION_TUNNEL;

    private static final Location[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();

    public static Location getRandomLocation() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}