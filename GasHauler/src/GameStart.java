import controller.GameController;
import controller.GameException;
import view.Console;

import java.sql.SQLException;

/**
 *Class: GameStart
 * @author Richard Park, David Flores
 * Version 1.2
 * Course: ITEC 3860, Fall 2024
 * Written: November 2, 2024
 * This class is the launch point for Gas Hauler
 */
public class GameStart {
    /**
     * Method: main
     * Purpose: Runs the program
     * @param args
     * @throws GameException
     */
    public static void main(String[] args) {
        GameController gc = new GameController();
        //String playerName = "DefaultPlayer";

        try {
            //gc.start(playerName);
            Console console = new Console();
            console.startGame();
        } catch (GameException ge) {
            System.out.println("An error occurred: " + ge.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}