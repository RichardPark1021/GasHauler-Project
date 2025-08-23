package view;

import controller.AccountManagement;
import controller.GameException;
import controller.RoomController;

import java.sql.SQLException;

/**
 * Class : Console
 * @author Richard Park
 * Version 1.
 * Course: ITEC 3860, Fall 2024
 * Written: November 2, 2024
 *
 */
public class Console {
    private AccountManagement am;
    private RoomController rc;
    /**
     * Method: startGame
     * Purpose: Starts the game
     * @throws GameException
     */
    public void startGame() throws GameException, SQLException, ClassNotFoundException {
        loginScreen();
        gameScreen();
    }
    /**
     * Method: loginScreen
     * Purpose: Display's the Interface for the login screen
     * @throws GameException
     */
    public void loginScreen() throws GameException, SQLException, ClassNotFoundException {
        am = new AccountManagement();
        System.out.println("Welcome to The Lonely Gas Hauler. If you already have an account you can login, otherwise please create an account to play. If you forgot your password, you can also recover an account by correctly answering your security question.\n" +
                "Available Commands:\n" +
                "Register <username> <password> - Creates a new account with the given username and password, and security question and answer. The password must be at least 8 characters long, and consist of at least 1 uppercase letter, 1 lowercase letter, and 1 number.\n" +
                "Login <username> <password> - Logs in to the account with the given username and password.\n" +
                "Recover <username> - Recover an account if you forgot its password. Prompts for answer to security question.\n" +
                "Exit - Exits the game.\n");
        am.loginCommand();
    }
    /**
     * Method: gameScreen
     * Purpose: Display's the Interface for the game
     * @throws GameException
     */
    public void gameScreen() throws GameException, SQLException, ClassNotFoundException {
        rc = new RoomController(am.getCurrentUsername());
        System.out.println("THE LONELY GAS HAULER\n" +
                "A text-based science-fiction adventure game\n" +
                "(Type ‘help’ to view a list of commands)\n");
        rc.displayRoom();
    }
}
