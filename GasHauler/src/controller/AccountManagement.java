
package controller;

import model.GameDBCreate;
import model.PlayerDB;

import java.io.File;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Class : AccountManagement
 * @author Richard Park, David Flores
 * Version 1.2
 * Course: ITEC 3860, Fall 2024
 * Written: November 2, 2024
 *
 */
public class AccountManagement {
    private final Scanner sc = new Scanner(System.in);
    private String currentUsername;

    public void loginCommand() throws GameException, SQLException, ClassNotFoundException {
        while (true) {
            System.out.print("Enter Command: ");
            String input = sc.nextLine().trim();
            String[] commandParts = input.split(" ");
            String command = commandParts[0].toLowerCase();
            //changes made here are to avoid ArrayIndexOutOfBoundException
            if ((commandParts.length < 1) || (input.equalsIgnoreCase("register") || input.equalsIgnoreCase("login")) || input.equalsIgnoreCase("recover")) {
                System.out.println("Usage: <command> <username> <password>");
                continue;
            }

            String username = commandParts.length > 1 ? commandParts[1] : null;
            String password = commandParts.length > 2 ? commandParts[2] : null;

            switch (command) {
                case "register":
                    System.out.println(registerAccount(username, password));
                    break;
                case "login":
                    loginAccount(username, password);
                    return;
                case "recover":
                    recoverAccount(username);
                    break;
                case "exit":
                    System.out.println("Exiting the game.");
                    return;
                default:
                    System.out.println("Unrecognized command.");
            }
        }
    }

    public String registerAccount(String username, String password) throws GameException, SQLException, ClassNotFoundException {
        if (!validateUsername(username)) {
            return "Username must have at least one character.";
        }
        if (!validatePassword(password)) {
            return "Password must be at least 8 characters long, with uppercase, lowercase, and number.";
        }

        String[] securityQA = getSecurityQA();
        String question = securityQA[0];
        String answer = securityQA[1];


        String dbName = "src/" + username + ".db";
        GameDBCreate gdbc = new GameDBCreate(dbName);
        gdbc.buildTables();
        gdbc.insertPlayer(username, password, question, answer);

        return "Account successfully created. Please log in to your new account.";
    }

    public void loginAccount(String username, String password) throws GameException {
        String dbName = "src/" + username + ".db";
        File dbFile = new File(dbName);
        if (!dbFile.exists()) {
            System.out.println("Invalid username or password.");
            return;
        }

        PlayerDB playerDB = new PlayerDB(dbName);
        if (playerDB.playerAccountExists(username, password)) {
            currentUsername = username;
            System.out.println("Login successful. Welcome back, " + username + "!\n");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public void recoverAccount(String username) throws GameException {
        String dbName = "src/" +  username + ".db";
        PlayerDB playerDB = new PlayerDB(dbName);
        String[] recoveryData = playerDB.getPlayerRecoveryData(username);
        System.out.println("Security Question: " + recoveryData[0]);
        System.out.print("Answer: ");
        String answer = sc.nextLine();

        if (answer.equals(recoveryData[1])) {
            System.out.println("Your password is: " + recoveryData[2]);
        } else {
            System.out.println("Incorrect answer. Unable to recover account.");
        }
    }

    private String[] getSecurityQA() {
        String question;
        String answer;

        while (true) {
            System.out.print("Enter a security question: ");
            question = sc.nextLine();
            if (!validateSecurityQuestion(question)) {
                System.out.println("Security question cannot be blank. Please enter a valid question.");
                continue;
            }

            System.out.print("Enter the answer to your security question: ");
            answer = sc.nextLine();
            if (!validateSecurityAnswer(answer)) {
                System.out.println("Security answer cannot be blank. Please enter a valid answer.");
                continue;
            }

            break;
        }
        return new String[] {question, answer};
    }

    private boolean validateUsername(String username) {
        return !username.isEmpty();
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8 &&
                password.chars().anyMatch(Character::isUpperCase) &&
                password.chars().anyMatch(Character::isLowerCase) &&
                password.chars().anyMatch(Character::isDigit);
    }

    private boolean validateSecurityQuestion(String question) {
        return !question.trim().isEmpty();
    }

    private boolean validateSecurityAnswer(String answer) {
        return !answer.trim().isEmpty();
    }

    public String getCurrentUsername() {
        return currentUsername;
    }
}