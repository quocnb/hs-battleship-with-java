package battleship;

public enum Ship {
    AIRCRAFT_CARRIER, BATTLESHIP, SUBMARINE, CRUISER, DESTROYER;

    public int getCell() {
        return switch (this) {
            case AIRCRAFT_CARRIER -> 5;
            case BATTLESHIP -> 4;
            case SUBMARINE, CRUISER -> 3;
            case DESTROYER -> 2;
        };
    }

    public String getName() {
        return switch (this) {
            case AIRCRAFT_CARRIER -> "Aircraft Carrier";
            case BATTLESHIP -> "Battleship";
            case SUBMARINE -> "Submarine";
            case CRUISER -> "Cruiser";
            case DESTROYER -> "Destroyer";
        };
    }
}
