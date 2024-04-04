package battleship;

public class Coordinate {
    final int row;
    final int column;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public boolean equals(Coordinate o) {
        return this.row == o.row && this.column == o.column;
    }
}
