package battleship;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DataManagement dataManagement = DataManagement.shared();

        // Print field
        dataManagement.displayField();

        // Get input
        Scanner scanner = new Scanner(System.in);
        for (Ship ship : Ship.values()) {
            System.out.printf(
                    "Enter the coordinates of the %s (%d cells):\n",
                    ship.getName(),
                    ship.getCell()
            );
            while (true) {
                try {
                    Coordinate front = dataManagement.generateCoordinate(scanner.next());
                    Coordinate back = dataManagement.generateCoordinate(scanner.next());
                    dataManagement.addShip(ship, front, back);
                    dataManagement.displayField();
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("The game starts!");
        dataManagement.displayField(true);
        System.out.println("Take a shot!");

        while (true) {
            try {
                Coordinate shot = dataManagement.generateCoordinate(scanner.next());
                HitStatus hitStatus = dataManagement.hit(shot);
                dataManagement.displayField(true);
                String message = switch (hitStatus) {
                    case MISS -> "You missed. Try again:";
                    case HIT, HIT_AGAIN -> "You hit a ship! Try again:";
                    case SANK -> "You sank a ship! Specify a new target:";
                    case FINISH -> "You sank the last ship. You won. Congratulations!";
                };
                System.out.println(message);
                if (hitStatus == HitStatus.FINISH) {
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
