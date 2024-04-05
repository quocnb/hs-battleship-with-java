package battleship;

import java.util.Scanner;

public class Main {

    final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Player 1
        DataManagement player1Data = new DataManagement();
        System.out.println("Player 1, place your ships on the game field");
        setupField(player1Data);

        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();

        // Player 2
        DataManagement player2Data = new DataManagement();
        System.out.println("Player 2, place your ships to the game field");
        setupField(player2Data);
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();

        boolean play1Turn = true;
        HitStatus hitStatus;
        while (true) {
            if (play1Turn) {
                player2Data.displayField(true);
                System.out.println("-".repeat(21));
                player1Data.displayField();
                System.out.println();
                System.out.println("Player 1, it's your turn:\n");
                hitStatus = hit(player2Data);
            } else {
                player1Data.displayField(true);
                System.out.println("-".repeat(21));
                player2Data.displayField();
                System.out.println();
                System.out.println("Player 2, it's your turn:\n");
                hitStatus = hit(player1Data);
            }

            if (hitStatus == HitStatus.FINISH) {
                break;
            }
            System.out.println("Press Enter and pass the move to another player");
            scanner.nextLine();
            play1Turn = !play1Turn;
        }
    }

    static HitStatus hit(DataManagement player) {
        HitStatus hitStatus;
        while (true) {
            try {
                Coordinate shot = player.generateCoordinate(scanner.next());
                hitStatus = player.hit(shot);
                String message = switch (hitStatus) {
                    case MISS -> "You missed!";
                    case HIT, HIT_AGAIN -> "You hit a ship!";
                    case SANK -> "You sank a ship!";
                    case FINISH -> "You sank the last ship. You won. Congratulations!";
                };
                System.out.println();
                System.out.println(message);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.nextLine();
        return hitStatus;
    }

    static void setupField(DataManagement dataManagement) {
        // Print field
        System.out.println();
        dataManagement.displayField();

        // Get input
        for (Ship ship : Ship.values()) {
            System.out.printf(
                    "\nEnter the coordinates of the %s (%d cells):\n\n",
                    ship.getName(),
                    ship.getCell()
            );
            while (true) {
                try {
                    Coordinate front = dataManagement.generateCoordinate(scanner.next());
                    Coordinate back = dataManagement.generateCoordinate(scanner.next());
                    dataManagement.addShip(ship, front, back);
                    System.out.println();
                    dataManagement.displayField();
                    break;
                } catch (Exception e) {
                    System.out.println();
                    System.out.println(e.getMessage());
                    System.out.println();
                }
            }
        }
        System.out.println();
        scanner.nextLine();
    }
}
