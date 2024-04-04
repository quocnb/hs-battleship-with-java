package battleship;

import java.util.ArrayList;

public class DataManagement {
    public final static int FIELD_SIZE = 10;
    final static String SHIP_SYMBOL = "O";
    final static String SEA_SYMBOL = "~";
    final static String HIT_SYMBOL = "X";
    final static String MISS_SYMBOL = "M";

    private static DataManagement _sharedInstance;
    private static String[][] field;
    private static ArrayList<String> rowIndexes;
    private static ArrayList<ArrayList<Coordinate>> ships;
    private HitStatus lastHitStatus;

    public static DataManagement shared() {
        if (_sharedInstance == null) {
            _sharedInstance = new DataManagement();
        }
        return _sharedInstance;
    }

    public Coordinate generateCoordinate(String coordinate) throws Exception {
        String rowIndex = coordinate.substring(0, 1);
        int column = Integer.parseInt(coordinate.substring(1));
        // Check valid coordinate or not
        if (!rowIndexes.contains(rowIndex) || column >= field[0].length) {
            throw new Exception("Error! You entered the wrong coordinates! Try again:");
        }
        int row = rowIndexes.indexOf(rowIndex) + 1;
        return new Coordinate(row, column);
    }

    public void displayField() {
        displayField(false);
    }

    public void displayField(boolean hideShip) {
        System.out.println();
        for (String[] strings : field) {
            String rowData = String.join(" ", strings);
            if (hideShip) {
                rowData = rowData.replace(SHIP_SYMBOL, SEA_SYMBOL);
            }
            System.out.println(rowData);
        }
        System.out.println();
    }

    public void addShip(Ship ship, Coordinate front, Coordinate back) throws Exception {
        if (front.row != back.row && front.column != back.column) {
            throw new Exception("Error! Wrong ship location! Try again:");
        }
        if (Math.abs(front.row - back.row) + Math.abs(front.column - back.column) + 1 != ship.getCell()) {
            String message = String.format("Error! Wrong length of the %s! Try again:", ship.getName());
            throw new Exception(message);
        }
        checkConflict(front, back);
        addShip(front, back);
    }

    public HitStatus hit(Coordinate shot) {
        if (field[shot.row][shot.column].equals(HIT_SYMBOL)) {
            return HitStatus.HIT_AGAIN;
        }
        boolean hit = field[shot.row][shot.column].equals(SHIP_SYMBOL);
        field[shot.row][shot.column] = hit ? HIT_SYMBOL : MISS_SYMBOL;
        if (!hit) {
            return HitStatus.MISS;
        }
        boolean sankAShip = false;
        for (var ship: ships) {
            boolean removeHitPart = ship.removeIf(c -> c.equals(shot));
            if (!removeHitPart) {
                continue;
            }
            if (ship.isEmpty()) {
                ships.remove(ship);
                if (ships.isEmpty()) {
                    return HitStatus.FINISH;
                }
                return HitStatus.SANK;
            }
        }
        return HitStatus.HIT;
    }

    private void checkConflict(Coordinate front, Coordinate back) throws Exception {
        int minRow = Math.max(1, Math.min(front.row, back.row) - 1);
        int maxRow = Math.min(field.length - 1, Math.max(front.row, back.row) + 1);
        int minColumn = Math.max(1, Math.min(front.column, back.column) - 1);
        int maxColumn = Math.min(field[0].length - 1, Math.max(front.column, back.column) + 1);
        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minColumn; j <= maxColumn; j++) {
                if (field[i][j].equals(SHIP_SYMBOL)) {
                    throw new Exception("Error! You placed it too close to another one. Try again:\n");
                }
            }
        }
    }

    private void addShip(Coordinate front, Coordinate back) {
        int minRow = Math.min(front.row, back.row);
        int maxRow = Math.max(front.row, back.row);
        int minColumn = Math.min(front.column, back.column);
        int maxColumn = Math.max(front.column, back.column);
        ArrayList<Coordinate> shipCoordinate = new ArrayList<>();
        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minColumn; j <= maxColumn; j++) {
                field[i][j] = SHIP_SYMBOL;
                shipCoordinate.add(new Coordinate(i, j));
            }
        }
        ships.add(shipCoordinate);
    }

    private DataManagement() {
        rowIndexes = createFieldColumnIndexes(FIELD_SIZE);
        field = createField(rowIndexes);
        ships = new ArrayList<>();
    }

    private ArrayList<String> createFieldColumnIndexes(int size) {
        ArrayList<String> columnIndexes = new ArrayList<>();
        for (char c = 'A'; c < 'A' + size; c++) {
            columnIndexes.add(String.valueOf(c));
        }
        return columnIndexes;
    }

    private String[][] createField(ArrayList<String> columnIndexes) {
        int size = columnIndexes.size() + 1;
        String[][] field = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == 0) {
                    field[0][j] = j == 0 ? " " : String.valueOf(j);
                } else if (j == 0) {
                    field[i][0] = columnIndexes.get(i-1);
                } else {
                    field[i][j] = SEA_SYMBOL;
                }
            }
        }
        return field;
    }
}
