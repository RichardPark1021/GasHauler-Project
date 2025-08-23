package controller;

import model.*;
import view.Console;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class: RoomController
 *
 * @author: Erika Attidzah
 * @version: 1.2
 * Course: ITEC 3860 FALL 2024
 * Written: 4 Nov, 2024
 * This class manages the game's room interactions, handle user commands, control room transitions,
 * and display relevant game details based on the player's actions. It processes commands, executes actions,
 * and facilitates the progression of the player through different rooms
 */

public class RoomController {
    // Won't ever change
    private DefaultCommandDB defaultCommandDB;
    private EventDB eventDB;
    // Can change based on player's action
    private CommandDB commandDB;
    private SequenceDB sequenceDB;
    private Scanner sc;
    private String dbName;
    private String username;
    private int currentRoom;
    private TransitionDB transitionDB;
    private RoomDB roomDB;
    private MarkerDB markerDB;
    private AchievementDB achievementDB;
    private HintDB hintDB;
    private InventoryManagement inventoryManagement;
    private Items items;
    private InventoryManagerDB inventoryManagerDB;


    public RoomController(String username) throws SQLException, ClassNotFoundException, GameException {
        this.username = username;
        this.dbName = "src/" + username + ".db";
        this.commandDB = new CommandDB(this.dbName);
        this.defaultCommandDB = new DefaultCommandDB(this.dbName);
        this.eventDB = new EventDB(dbName);
        this.sequenceDB = new SequenceDB(dbName);
        this.transitionDB = new TransitionDB(dbName);
        this.roomDB = new RoomDB(dbName);
        this.currentRoom = getPlayerCurrentRoom(1);
        this.markerDB = new MarkerDB(dbName);
        this.hintDB = new HintDB(dbName);
        this.inventoryManagement = new InventoryManagement(dbName);
        this.items = new Items(dbName);
        this.inventoryManagerDB = new InventoryManagerDB(dbName);
    }

    /**
     * Method: displayRoom
     * Purpose: manage the main game loop, display the current room's details,
     * and process user commands to navigate through the game
     * @throws SQLException
     * @throws GameException
     * @throws ClassNotFoundException
     */
    public void displayRoom() throws GameException, SQLException, ClassNotFoundException {
        sc = new Scanner(System.in);
        int lastRoom = -1; // track the room before
        while (true) {
            if (currentRoom == 11) {
                room11();
            } else if (currentRoom == 12) {
                room12();
            } else if (currentRoom == 13) {
                room13();
            } else if (currentRoom == 14) {
                room14();
            } else if (currentRoom == 16) {
                room16();
            } else if (currentRoom == 17) {
                room17();
            } else if (currentRoom == 20) {
                room20();
            } else if (currentRoom == 21) {
                room21();
            } else if (currentRoom == 22) {
                room22();
            } else if (currentRoom == 23) {
                room23();
            } else if (currentRoom == 24) {
                room24();
            } else if (currentRoom == 25) {
                room25();
            } else if (currentRoom == 28) {
                room28();
            } else if (currentRoom == 31) {
                room31();
            } else if (currentRoom == 33) {
                room33();
            } else if (currentRoom == 34) {
                room34();
            } else {
                if (currentRoom != lastRoom) {
                    displayRoomDetails(currentRoom);
                    lastRoom = currentRoom;
                }
                System.out.print("Enter Command: ");
                String cmd = sc.nextLine();
                matchingCommand(currentRoom, cmd);
                System.out.println();
            }
            if (currentRoom == 2){
                getSecondDescription(2);
            }
            setPlayerCurrentRoom(currentRoom);
        }
    }

    /**
     * Method: matchingCommand
     * Purpose: process and execute user commands by matching the input against predefined command patterns
     * and handling corresponding actions or room transitions
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws GameException
     */
    public void matchingCommand(int roomID, String input) throws SQLException, ClassNotFoundException, GameException {
        // Define the regex pattern for matching commands like "Pick Up", "Drop", etc.
        String regex = "(?i)^(Pick Up|Drop|Use|Look At|Move|Feel|Inventory)(?:\\s+(.*))?$";
        Map<Integer, Command> commandMap = commandDB.getAllCommands(roomID);
        List<DefaultCommand> defaultCommands = defaultCommandDB.getAllDefaultsCommand();

        // Compile the regex pattern and match it against the input
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            String action = matcher.group(1); // default command, like "Pick Up", "Drop", etc...
            String second = matcher.group(2); // The argument, if any
            // david added
            if (action.equalsIgnoreCase("Inventory")) {
                showInventory();
                return;
            }

            if (action.equalsIgnoreCase("Look at") && second.equalsIgnoreCase("Room") && currentRoom != 1) {
                displayRoomDetails(currentRoom);
                return;
            }

            // david added
            /*if (action.equalsIgnoreCase("Use") && input.toLowerCase().contains(" on ")) {
                String use = inventoryManagement.handleUse(input, currentRoom, dbName);
                System.out.println(use);
                return;
            }*/

            if (action.equalsIgnoreCase("use") && second == null) {
                String dafault = inventoryManagement.defaultUse(currentRoom);
                System.out.println(dafault);
                return;
            }
            if (currentRoom != 1 && currentRoom != 2 && currentRoom != 6
                    && currentRoom != 7 && currentRoom != 3
                    && currentRoom != 5 && currentRoom != 4
                    && (action.equalsIgnoreCase("use") && second.equalsIgnoreCase("flashlight"))
                    && handleInventoryCommands(action, second)){
                return;
            }
            if (second == null) {
                // No argument provided
                System.out.println("Missing required argument for " + action + " command.");
            } else {
                if (currentRoom == 3) {
                    if (!second.equalsIgnoreCase("west") && !second.equalsIgnoreCase("north")) {
                        room3(action, second);
                        return;
                    }
                }
                if (currentRoom == 4) {
                    if (second.equalsIgnoreCase("east") || second.equalsIgnoreCase("north")) {
                        room4(action, second);
                        return;
                    }
                }
                if (currentRoom == 5) {
                    if (!second.equalsIgnoreCase("south") && !second.equalsIgnoreCase("east")
                            && (!action.equalsIgnoreCase("feel") && (!second.equalsIgnoreCase("shelf")
                            || !second.equalsIgnoreCase("shelves")))
                            && !(action.equalsIgnoreCase("look at") && second.equalsIgnoreCase("shape"))) {
                        room5(action, second);
                    }
                }
                if (currentRoom == 6) {
                    if (!second.equalsIgnoreCase("south") && !second.equalsIgnoreCase("east")
                            && !second.equalsIgnoreCase("airlock") && !second.equalsIgnoreCase("security droid")
                            && !second.equalsIgnoreCase("droid")) {
                        room6(action, second);
                        return;
                    }
                }
                if (currentRoom == 7) {
                    if (!second.equalsIgnoreCase("north")) {
                        room7(action, second);
                        return;
                    }
                }
                if (currentRoom == 26) {
                    if (!second.equalsIgnoreCase("west")) {
                        room26(action, second);
                        return;
                    }
                }

                boolean commandFound = false;

                for (Command command : commandMap.values()) {
                    // Check if the action (defaultID) and the command name (second) match in the same Command record
                    boolean isMatchingAction = defaultCommands.stream()
                            .anyMatch(defaultCommand -> defaultCommand.getDefaultID() == command.getDefaultID()
                                    && defaultCommand.getName().equalsIgnoreCase(action));

                    if (isMatchingAction && (command.getName().equalsIgnoreCase(second) || command.getName().equalsIgnoreCase("OBJECT_NAME"))) {
                        // Check for a transition to a destination room
                        int destinationRoomID = transitionDB.getDestinationRoom(command.getCommandID(), roomID);

                        if (destinationRoomID != -1) {
                            currentRoom = destinationRoomID;  // Update current room to destination
                        } else {
                            // No transition; execute the command sequence
                            getSequence(command.getCommandID());
                        }

                        commandFound = true;
                        break;
                    }
                }

                if (!commandFound) {
                    // Command and argument do not match any record
                    if(action.equalsIgnoreCase("move") && second != null){
                        System.out.println("You cannot go that way!");
                    }
                    else if(!(action.equalsIgnoreCase("pick up") || action.equalsIgnoreCase("drop")
                            || action.equalsIgnoreCase("use"))){
                        System.out.println("Unrecognized command. Type ‘help’ for a list of commands.");
                    }
                }

            }
        } else {
            // If the input doesn't match the regex pattern, check DefaultCommands
            boolean defaultCommandFound = false;
            for (DefaultCommand defaultCommand : defaultCommands) {
                if (defaultCommand.getDefaultAction().equalsIgnoreCase(input)) {
                    System.out.println(defaultCommand.getDefaultAction());
                    defaultCommandFound = true;
                    break;
                }
            }
            if (!defaultCommandFound) {
                commands(input);
            }
        }
    }

    private void showInventory() throws SQLException {
        System.out.println(inventoryManagement.displayInventory());
    }

    // New method to handle inventory-related commands
    private boolean handleInventoryCommands(String action, String second) throws SQLException, GameException, ClassNotFoundException {
        switch (action.toLowerCase()) {
            case "pick up":
                if (second != null) {
                    inventoryManagement.setRoomID(currentRoom);
                    int itemID = items.getItemByName(dbName, second);
                    if (itemID != -1) {
                        String pickup = inventoryManagement.pickUpItem(itemID, second);
                        System.out.println(pickup);
                        return true;
                    }
                }
                return false;

            case "drop":
                if (second != null) {
                    int itemID = items.getItemByName(dbName, second);
                    if (itemID == 1) {
                        setMarkerOff("Flashlight On");
                    }
                    if (itemID != -1) {
                        String drop = inventoryManagement.dropItem(itemID, second);
                        System.out.println(drop);
                        return true;
                    }
                }
                return false;

            case "look at":
            case "feel":
                if ((second.equalsIgnoreCase("body") || second.equalsIgnoreCase("shape")) && getMarkerActive("Flashlight On") == 0) {
                    System.out.println("You can’t make out any details without more light.");
                    return true;
                }
                String description = inventoryManagement.getRoomDescription(currentRoom);
                if (description != null && !description.isEmpty()) {
                    System.out.println(description);
                    return true;
                }
                 return false;

            case "use":
                String use;
                int itemID;
                if (action.equalsIgnoreCase("use") && second == null) {
                    String dafault = inventoryManagement.defaultUse(currentRoom);
                    System.out.println(dafault);
                }
                if (!inventoryManagement.validateUse(currentRoom, items.getItemByName(dbName, second) ) == true) {
                    System.out.println("You see no use for this item here.");
                    return false;
                }
                if (second != null) {
                    itemID = items.getItemByName(dbName, second);
                    if (itemID == 1) {
                        String marker = "Flashlight On";
                        int active = getMarkerActive(marker);
                        if (active == 0) {
                            setMarkerActive(marker);
                        } else {
                            setMarkerOff(marker);
                        }
                        use = inventoryManagement.getUse(itemID, marker, active == 0 ? 1 : 0);
                        System.out.println(use);
                        return true;
                    } else if (itemID == 2) {
                        String marker = "Freezing";
                        int active = getMarkerActive(marker);
                        if (active == 0) {
                            setMarkerActive(marker);
                            use = inventoryManagement.getUse(itemID, marker, active);
                            System.out.println(use);
                            setMarkerOff(marker);
                            return true;
                        }
                    } else if (second.equalsIgnoreCase("Terminal")){
                        System.out.println("The computer terminal wakes at your touch, presenting you with a prompt to enter a pin number or exit:");
                    }
                }
                return false;

            default:
                return false;

        }

    }

    /**
     * Method: getSequence
     * Purpose: print the event associated with the command, and calls the check and updates method
     *
     * @throws SQLException
     */
    public void getSequence(int commandID) throws SQLException {
        try {
            String event = eventDB.getEvent(commandID).toString().replace("|", "\n"); // string of the event the command is attached to
            sequenceDB.checkAndUpdate(commandID); // check if the command unlocks any other commands
            System.out.println(event);
        }
        catch (NullPointerException npe){}
    }

    /**
     * Method: hint
     * Purpose: display hints to the player depending on the states of their gameplay
     *
     * @throws GameException
     */
    private void hint() throws GameException, SQLException {
        Hints hint;
        if (currentRoom == 1 || currentRoom == 22 || currentRoom == 23
                || currentRoom == 24 || currentRoom == 25) {
            int hintID = roomDB.getRoom(currentRoom).getHintID();
            hint = hintDB.getHint(hintID);
            System.out.println(hint.getDescription());
        } else if (markerDB.getMarkerByID(1).getActive() == 1) {
            hint = hintDB.getHint(2);
            System.out.println(hint.getDescription());
        } else if (currentRoom == 3 && markerDB.getMarkerByID(2).getActive() == 0) {
            int hintID = roomDB.getRoom(currentRoom).getHintID();
            hint = hintDB.getHint(hintID);
            System.out.println(hint.getDescription());
        } else if (currentRoom == 28 && !inventoryManagerDB.getPlayerInventoryItems().contains(17)) {
            int hintID = roomDB.getRoom(currentRoom).getHintID();
            hint = hintDB.getHint(hintID);
            System.out.println(hint.getDescription());
        } else if (currentRoom == 34 && markerDB.getMarkerByID(18).getActive() == 0
                && markerDB.getMarkerByID(16).getActive() == 1
                && markerDB.getMarkerByID(4).getActive() == 0) {
            hint = hintDB.getHint(5);
            System.out.println(hint.getDescription());
        } else if (currentRoom == 34 && markerDB.getMarkerByID(18).getActive() == 0
                && markerDB.getMarkerByID(16).getActive() == 0
                && markerDB.getMarkerByID(4).getActive() == 0) {
            int hintID = roomDB.getRoom(currentRoom).getHintID();
            hint = hintDB.getHint(hintID);
            System.out.println(hint.getDescription());
        } else if (currentRoom == 34 && markerDB.getMarkerByID(18).getActive() == 0
                && markerDB.getMarkerByID(16).getActive() == 0
                && markerDB.getMarkerByID(4).getActive() == 1) {
            hint = hintDB.getHint(7);
            System.out.println(hint.getDescription());
        } else if (currentRoom == 34 && markerDB.getMarkerByID(18).getActive() == 0
                && markerDB.getMarkerByID(16).getActive() == 1
                && markerDB.getMarkerByID(4).getActive() == 1) {
            hint = hintDB.getHint(8);
            System.out.println(hint.getDescription());
        } else if (currentRoom == 17 && markerDB.getMarkerByID(12).getActive() == 0) {
            int hintID = roomDB.getRoom(currentRoom).getHintID();
            hint = hintDB.getHint(hintID);
            System.out.println(hint.getDescription());
        } else if ((currentRoom == 7 || currentRoom == 21) && !inventoryManagerDB.getPlayerInventoryItems().contains(7)
                && !inventoryManagerDB.getPlayerInventoryItems().contains(8)) {
            int hintID = roomDB.getRoom(currentRoom).getHintID();
            hint = hintDB.getHint(hintID);
            System.out.println(hint.getDescription());
        } else if (currentRoom == 11 && markerDB.getMarkerByID(14).getActive() == 0) {
            int hintID = roomDB.getRoom(currentRoom).getHintID();
            hint = hintDB.getHint(hintID);
            System.out.println(hint.getDescription());
        } else if (currentRoom == 33 && markerDB.getMarkerByID(12).getActive() == 0) {
            int hintID = roomDB.getRoom(currentRoom).getHintID();
            hint = hintDB.getHint(hintID);
            System.out.println(hint.getDescription());
        } else if ((currentRoom == 13 || currentRoom == 14 || currentRoom == 15
                || currentRoom == 16 || currentRoom == 17
                || currentRoom == 18 || currentRoom == 19
                || currentRoom == 20 || currentRoom == 21)
                && markerDB.getMarkerByID(8).getActive() == 0) {
            hint = hintDB.getHint(13);
            System.out.println(hint.getDescription());
        } else {
            hint = hintDB.getHint(15);
            System.out.println(hint.getDescription());
        }
    }

    /**
     * Method: room3
     * Purpose: handles the specific functions of room3
     *
     * @throws GameException
     * @throws SQLException
     */
    //FINISHED
    public void room3(String action, String argument) throws GameException, SQLException, ClassNotFoundException {
        if (argument.equalsIgnoreCase("south")) {
            if (markerDB.getMarkerByID(5).getActive() == 1) {
                currentRoom = 6;
            } else {
                System.out.println("You attempt to open the doors, but they refuse to open. " +
                        "It seems they have lost power.");
                return;
            }
        }
        if (argument.equalsIgnoreCase("bucket") || argument.equalsIgnoreCase("cleaning supplies")
                || argument.equalsIgnoreCase("supplies")) {
            if (inventoryManagerDB.getPlayerInventoryItems().contains(3) &&
                    inventoryManagerDB.getPlayerInventoryItems().contains(2)
                    && inventoryManagerDB.getPlayerInventoryItems().contains(4)) {
                System.out.println("Here be an empty bucket.");
                return;
            } else if (inventoryManagerDB.getPlayerInventoryItems().contains(3) && !inventoryManagerDB.getPlayerInventoryItems().contains(4)
                    && !inventoryManagerDB.getPlayerInventoryItems().contains(2)) {
                System.out.println("The bucket contains a rag and a bottle of cleaning spray.");
                return;
            } else if (inventoryManagerDB.getPlayerInventoryItems().contains(2) && !inventoryManagerDB.getPlayerInventoryItems().contains(3)
                    && !inventoryManagerDB.getPlayerInventoryItems().contains(4)) {
                System.out.println("The bucket contains a mop and a bottle of cleaning spray.");
                return;
            } else if (inventoryManagerDB.getPlayerInventoryItems().contains(4) && !inventoryManagerDB.getPlayerInventoryItems().contains(3)
                    && !inventoryManagerDB.getPlayerInventoryItems().contains(2)) {
                System.out.println("The bucket contains a mop and a rag.");
                return;
            } else if (inventoryManagerDB.getPlayerInventoryItems().contains(3)
                    && inventoryManagerDB.getPlayerInventoryItems().contains(4)) {
                System.out.println("The bucket contains a rag.");
                return;
            } else if (inventoryManagerDB.getPlayerInventoryItems().contains(2)
                    && inventoryManagerDB.getPlayerInventoryItems().contains(4)) {
                System.out.println("The bucket contains a mop.");
                return;
            } else if (inventoryManagerDB.getPlayerInventoryItems().contains(3)
                    && inventoryManagerDB.getPlayerInventoryItems().contains(2)) {
                System.out.println("The bucket contains a bottle of cleaning spray.");
                return;
            } else if (inventoryManagerDB.getPlayerInventoryItems().contains(3)
                    && inventoryManagerDB.getPlayerInventoryItems().contains(4)) {
                System.out.println("The bucket contains a rag");
                return;
            } else {
                System.out.println("The bucket contains a mop and a rag and a bottle of cleaning spray.");
                return;
            }
        }
        if (argument.equalsIgnoreCase("axe on branches") || argument.equalsIgnoreCase("axe on overgrowth")
                || argument.equalsIgnoreCase("ax on branches") || argument.equalsIgnoreCase("ax on overgrowth")) {
            if (inventoryManagerDB.getPlayerInventoryItems().contains(18)) {
                if (markerDB.getMarkerByID(2).getActive() == 0) {
                    System.out.println("You hack at the overgrowth with the axe. As the branches give way, the door begins sliding shut. " +
                            "\nWithout time to think you lunge forward through the doors to avoid being crushed.");
                    //Set marker to cut overgrowth + set second description
                    markerDB.updateMarker("Cut Overgrowth");
                    currentRoom = 6;
                    return;
                }
            }
        }
        if (argument.equalsIgnoreCase("cleaning spray") || argument.equalsIgnoreCase("spray")) {
            System.out.println("You pick up the bottle of cleaning spray. Looking at it briefly, a yellow warning label catches your eye: \n" +
                    "“Warning: Pressurized. Bottle may explode if exposed to high temperatures.” ");
        }
        if (action.equalsIgnoreCase("drop") || action.equalsIgnoreCase("use") || action.equalsIgnoreCase("pick up")) {
            handleInventoryCommands(action, argument);
        }
        else if(action.equalsIgnoreCase("move")){
            System.out.println("You cannot go that way!");
        }
        else {
            System.out.println("Too many arguments entered for " + action + " command.");
        }
    }

    /**
     * Method: room4
     * Purpose: handles the specific functions of room4
     *
     * @throws GameException
     * @throws SQLException
     */
    //FINISHED
    public void room4(String action, String argument) throws GameException, SQLException, ClassNotFoundException {
        if (argument.equalsIgnoreCase("east")) {
            if (markerDB.getMarkerByID(5).getActive() == 0) {
                System.out.println("You attempt to enter the medical bay, but the doors refuse to open. " +
                        "It seems they have lost power.");
                return;
            } else {
                currentRoom = 8;
            }
        } else if (argument.equalsIgnoreCase("north")) {
            if (markerDB.getMarkerByID(5).getActive() == 0) {
                System.out.println("You attempt to open the doors, but they refuse to open. " +
                        "It seems they have lost power.");
            } else {
                currentRoom = 9;
            }
        } else if (action.equalsIgnoreCase("drop") || action.equalsIgnoreCase("pick up") || action.equalsIgnoreCase("use")) {
            handleInventoryCommands(action, argument);
        } else if(action.equalsIgnoreCase("move")){
            System.out.println("You cannot go that way!");
        } else {
            System.out.println("Too many arguments entered for " + action + " command.");
        }
    }

    /**
     * Method: room5
     * Purpose: handles the specific functions of room5
     *
     * @throws GameException
     */
    // FINISHED
    public void room5(String action, String argument) throws GameException, SQLException, ClassNotFoundException {
        if (markerDB.getMarkerByID(7).getActive() == 1) {
            commandDB.unlockOneCommand(32);
            sequenceDB.checkAndUpdate(32);
            if (argument.equalsIgnoreCase("body")) {
                if (!inventoryManagerDB.getPlayerInventoryItems().contains(15)) {
                    System.out.println("A dead crew member. He is wearing standard maintenance tech clothing.");
                    return;
                } else if (!inventoryManagerDB.getPlayerInventoryItems().contains(19)) {
                    System.out.println("A dead crew member. He is wearing a storage pack.");
                    return;
                }
            }
            if (action.equalsIgnoreCase("look at") && argument.equalsIgnoreCase("shelves")) {
                if (inventoryManagerDB.getPlayerInventoryItems().contains(5)) {
                    System.out.println("Empty dusty shelves stand in front of you.");
                    return;
                } else {
                    System.out.println("Dusty shelves stand in front of you, all empty save for a book sitting on one shelf.");
                    return;
                }
            }
        }
        if (argument.equalsIgnoreCase("shape")) {
            System.out.println("You make your way to the back of the room toward the dark shape on the ground. As you get near it, you bump into something hard and lose your balance. \n" +
                    "You hurtle forward into the dark shape. Something sharp jams into your chest and you bleed out. \n[GAME OVER]");
            System.exit(0);
        }
        if (markerDB.getMarkerByID(7).getActive() == 0) {
            sequenceDB.checkAndLock(32);
            commandDB.lockOneCommand(32);

            if(action.equalsIgnoreCase("look at") && argument.equalsIgnoreCase("shelves")){
                System.out.println("You can’t make out any details without more light.");
                return;
            }
            //System.out.println("Too many arguments entered for " + action + " command.");
        } if (action.equalsIgnoreCase("drop") || action.equalsIgnoreCase("pick up") || action.equalsIgnoreCase("use") || action.equalsIgnoreCase("look at")) {
            handleInventoryCommands(action, argument);
        } else if(action.equalsIgnoreCase("move")){
            System.out.println("You cannot go that way!");
        } else {
            System.out.println("Too many arguments entered for " + action + " command.");
        }
    }

    /**
     * Method: room6
     * Purpose: handles the specific functions of room6
     *
     * @throws GameException
     * @throws SQLException
     */
    // FINISHED
    public void room6(String action, String argument) throws SQLException, GameException, ClassNotFoundException {
        if (inventoryManagerDB.getPlayerInventoryItems().contains(9)) {
            if (argument.equalsIgnoreCase("blowtorch on window") || argument.equalsIgnoreCase("torch on window")) {
                System.out.println("As you use the blowtorch, the glass melts away. The flames create a hole large enough to climb through.");
                markerDB.updateMarker("Blowtorch Used");
                markerDB.updateMarker("Window Melted");
            }
        }
        if (argument.equalsIgnoreCase("north")) {
            if (markerDB.getMarkerByID(5).getActive() == 1 || markerDB.getMarkerByID(6).getActive() == 1) {
                currentRoom = 3;
            } else {
                System.out.println("The door will not open.");
            }
        } else if (action.equalsIgnoreCase("drop") || action.equalsIgnoreCase("pick up") || action.equalsIgnoreCase("use")) {
            handleInventoryCommands(action, argument);
        } else if(action.equalsIgnoreCase("move")){
            System.out.println("You cannot go that way!");
        } else {
            System.out.println("Too many arguments entered for " + action + " command.");
        }
    }

    /**
     * Method: room7
     * Purpose: handles the specific functions of room7
     *
     * @throws GameException
     * @throws SQLException
     */
    // FINISHED
    public void room7(String action, String argument) throws GameException, SQLException, ClassNotFoundException {
        if (argument.equalsIgnoreCase("south")) {
            if (markerDB.getMarkerByID(10).getActive() == 0) {
                System.out.println("You open the door. \n" +
                        "Without a spacesuit on, the air is immediately sucked from your lungs, and you are killed almost instantly by the vacuum of space. \n[GAME OVER]");
                System.exit(0);
            }
            if (markerDB.getMarkerByID(11).getActive() == 0) {
                System.out.println("You float peacefully through the open airlock, soaking in the beauty of space. As you gaze upon the stars you can see the ship getting smaller in the distance. \n" +
                        "After a while your oxygen tank runs out and you suffocate to death. \n[GAME OVER]");
                System.exit(0);
            } else {
                currentRoom = 23;
            }
        } else if (action.equalsIgnoreCase("drop") || action.equalsIgnoreCase("pick up") || action.equalsIgnoreCase("use")) {
            handleInventoryCommands(action, argument);
        } else if(action.equalsIgnoreCase("move")){
            System.out.println("You cannot go that way!");
        } else {
            System.out.println("Too many arguments entered for " + action + " command.");
        }
    }

    /**
     * Method: room11
     * Purpose: Controls the room - Floor Control-03F
     *
     * @throws GameException
     */
    private void room11() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom));
        System.out.println(getRoomDescription(currentRoom));
        sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            if(cmd.equals("use terminal")) {
                System.out.println("The computer terminal wakes at your touch, presenting you with a prompt to enter a pin number or exit:\n");
                room11Terminal();
                currentRoom = 10;
                running = false;
            }
            else if(cmd.equals("use power cell on pedestal")) {
                if(inventoryManagerDB.getPlayerInventoryItems().contains(11)) {
                    System.out.println("You take out the old power cell and put the new one in place. However, nothing happens.");
                    setMarkerActive("Power Cell Swapped");
                    currentRoom = 10;
                    running = false;
                }
                else {
                    System.out.println("power cell not in inventory.");
                }
            }
            else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room11Terminal
     * Purpose: Controls the terminal in room 11
     *
     * @throws GameException
     */
    private void room11Terminal() throws GameException, SQLException, ClassNotFoundException {
        String pin = "7487";
        int invalidPin = 1;
        boolean terminalActive = true;
        while(terminalActive) {
            System.out.print("Enter Command: ");
            String input = sc.nextLine().toLowerCase();
            System.out.println();
            if(input.equals(pin)) {
                if(getMarkerActive("Power Cell Swapped") == 1) {
                    System.out.println("The terminal accepts the pin and shows the current level of power in the power cell: 100%. \nA notice also shows up saying: “Warning: Power cell swap detected. \nRun ‘reset power’ command to begin using power cell.”");
                    System.out.print("Enter Command: ");
                    String cmd = sc.nextLine().toLowerCase();
                    if(cmd.equals("reset power") && getMarkerActive("Power Cell Swapped") == 1) {
                        System.out.println("The system runs the command, and after a few seconds, a message saying, “Power reset completed.” pops up on screen. \nYou notice emergency lights around you flickering on.");
                        setMarkerActive("Floor 3 Powered");
                        currentRoom = 10;
                        terminalActive = false;
                    }
                    else if(cmd.equals("exit")) {
                        System.out.println(getRoomDescription(currentRoom));
                        terminalActive = false;
                    }
                }
                else if(getMarkerActive("Elevator Used") == 1) {
                    System.out.println("The terminal accepts the pin and shows the current level of power in the power cell: 0.01%. \nNothing else shows up on the screen.");
                    terminalActive = false;
                }
                else {
                    System.out.println("The terminal accepts the pin and shows the current level of power in the power cell: 0.04%. \nNothing else shows up on the screen.");
                    terminalActive = false;
                }
            }
            else if(!input.equals(pin)) {
                if(invalidPin == 3) {
                    System.out.println("You hear mechanical whirring from outside the room. The sound gets louder, accompanied by the sound of metal slamming against metal. \nA security droid charges through the door, coming right for you!");
                    room11Droid();
                    terminalActive = false;
                }
                else {
                    System.out.println("Invalid pin!");
                    invalidPin++;
                }
            }
            else if(!input.equals(pin)) {
                System.out.println(getRoomDescription(currentRoom));
                terminalActive = false;
            }
            else if(input.equals("exit")) {
                System.out.println(getRoomDescription(currentRoom));
                terminalActive = false;
            }
            else {
                commands(input);
            }
        }
    }

    /**
     * Method: room11Droid
     * Purpose: Controls the security droid in room 11
     *
     * @throws GameException
     */
    private void room11Droid() throws GameException, SQLException, ClassNotFoundException {
        boolean running = true;
        while (running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();
            if(cmd.equals("look at security droid") || cmd.equals("look at droid")) {
                if(getMarkerActive("Droid Killed") == 0) {
                    System.out.println("This is a standard SD-7487 model security droid. It does not seem at all friendly. \nThe magnetic actuators in its legs allow it to run in the zero-gravity environment, and it’s coming right for you! \nSome of the metal plates on its chest are damaged, showing exposed wires and its power core, \nbut it seems to be functioning just fine.");
                }
                else {
                    System.out.println("This is a standard SD-7487 model security droid. \nIt does not appear to be operational, and you doubt it ever will be again.");
                }
            }
            else if(cmd.equals("use axe on security droid") || cmd.equals("use axe on droid") || cmd.equals("use ax on security droid") || cmd.equals("use ax on droid")) {
                if(inventoryManagerDB.getPlayerInventoryItems().contains(18)){
                    System.out.println("You swing the axe desperately at the droid as it reaches you. The axe glances off the droid’s metal plating as the droid slams into you. \nYou fly across the room and slam into the far wall with a sound of snapping bones. Everything goes dark. \n[GAME OVER]");
                    System.exit(0);
                }
                else {
                    System.out.println("Axe not in inventory");
                }
            }
            else if(cmd.equals("use mop on security droid") || cmd.equals("use mop on droid")) {
                if(inventoryManagerDB.getPlayerInventoryItems().contains(3)){
                    System.out.println("You thrust desperately with the mop as the droid reaches you. \nThe shaft bounces off the droid’s metal plating and slides into a hole in the droid’s chest, where it impales the droid’s power core. \nThe droid and you both hurtle across the room, slamming against the far wall. The impact knocks you unconscious. \nWhen you awake, you are drifting around the room, as is the lifeless droid. You feel a bit disoriented, but otherwise fine. ");
                    setMarkerActive("Droid Killed");
                    setAchievementCompleted("Security Disabled");
                    running = false;
                }
                else {
                    System.out.println("Mop not in inventory");
                }
            }
            else if(cmd.equals("use railgun on security droid") || cmd.equals("use railgun on droid") || cmd.equals("use gun on security droid") || cmd.equals("use gun on droid")) {
                if(inventoryManagerDB.getPlayerInventoryItems().contains(6)){
                    System.out.println("You fire at the droid. The railgun punches large holes in the droid’s metal plating and it crumples to the ground, soft tendrils of smoke rising from it.");
                    setMarkerActive("Droid Killed");
                    setAchievementCompleted("Security Disabled");
                    running = false;
                }
                else {
                    System.out.println("Railgun not in inventory");
                }
            }
            else if (cmd.startsWith("use")) {
                String object = cmd.substring(3).trim();
                if (object.isEmpty()) {
                    System.out.println("Unrecognized command. Type ‘help’ for a list of commands.");
                }
                else {
                    System.out.println("It has no effect! The droid slams into you with astounding force. \nYou fly across the room and slam into the far wall with a sound of snapping bones. Everything goes dark. [GAME OVER]");
                    System.exit(0);
                }
            }
            else {
                commands(cmd);
            }
        }
    }

    /**
     * Method: room12
     * Purpose: Controls the room - Elevator
     *
     * @throws GameException
     */
    private void room12() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom));
        if (getMarkerActive("Elevator Used") == 0) {
            System.out.println(getRoomDescription(currentRoom));
        } else if (getMarkerActive("Elevator Used") == 1) {
            System.out.println(getSecondDescription(currentRoom));
        } else {
            System.out.println(getThirdDescription(currentRoom));
        }
        sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            if (cmd.equals("move 1") && getMarkerActive("Elevator Used") == 0) {
                System.out.println("You attempt to go to floor 1, but pressing the button does nothing. \n" +
                        "You remember that floor 1 has protected access and requires a keycard to get to. \n");
            } else if (cmd.equals("move 2") || cmd.equals("move up") && getMarkerActive("Elevator Used") == 1) {
                System.out.println("The elevator whirls and slowly begins to rise to the second level. \n" +
                        "As the control panel switches to the number 2, you realize something is very wrong. \n" +
                        "You feel an immense tug as the air is pulled out of the elevator through a massive breach in the second level's exterior. " +
                        "\nThe air is ripped from your lungs as you are sucked out into space, floating away.");
                System.exit(0);
            }
            else if(cmd.equals("move 3") && getMarkerActive("Elevator Used") == 0) {
                System.out.println("The elevator groans and shakily descends to the fourth floor. The control panel flickers, showing you’re on floor 4 now as the elevator slams to a halt. \nIn the zero-gravity environment the sudden halt slams you into the floor, twisting your leg painfully. \nThe doors open right before the power in the elevator blinks out, and you push through the pain to propel yourself through the doors. ");
            }
            else if (cmd.equals("move 4") && getMarkerActive("Elevator Used") == 0) {
                System.out.println("The elevator groans and shakily descends to the fourth floor. \n" +
                        "The control panel flickers, showing you’re on floor 4 now as the elevator slams to a halt. \n" +
                        "In the zero-gravity environment the sudden halt slams you into the floor, twisting your leg painfully. \n" +
                        "The doors open right before the power in the elevator blinks out, and you push through the pain to propel yourself through the doors. ");
                setMarkerActive("Injured Leg");
                currentRoom = 13;
                running = false;
            } else if (cmd.equals("move south")) {
                currentRoom = 10;
                running = false;
            } else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room13
     * Purpose: Controls the room - Cargo Bay [Floor 4]
     *
     * @throws GameException
     */
    private void room13() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom) + "\n" );
        if(getMarkerActive("Flashlight On") == 0) {
            System.out.println(getRoomDescription(currentRoom));
        }
        else if(getMarkerActive("Flashlight On") == 1) {
            System.out.println(getSecondDescription(currentRoom));
        }
        boolean running = true;
        sc = new Scanner(System.in);
        while(running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();

            if (getMarkerActive("Flashlight On") == 0) {
                if (cmd.equals("move north")) {
                    currentRoom = 12;
                    running = false;
                }
                else if (cmd.equals("move west")) {
                    currentRoom = 15;
                    running = false;
                }
                else if (cmd.startsWith("move")) {
                    String object = cmd.substring(4).trim();
                    if (object.isEmpty()) {
                        System.out.println("Unrecognized command. Type ‘help’ for a list of commands.");
                    }
                    else {
                        System.out.println("You try to traverse the room in the dark, without gravity. \n" +
                                "As you float into the darkness you slam your head on something hard. \n" +
                                "You feel yourself spinning on complete darkness. You slam into something else, try to grab onto it, and slice your hand. \n" +
                                "Another hard protrusion meets your face. You hear objects falling and clattering. You hear a loud scraping noise. \n" +
                                "Something extremely heavy lands on your neck. Your neck does not fight back. \n" +
                                "[Game Over]");
                        System.exit(0);
                    }
                }
            }
            else if (getMarkerActive("Flashlight On") == 1) {
                if (cmd.equals("look at locker") || cmd.equals("look at lockers")) {
                    System.out.println("You can see that most of the lockers have been open and cleared out, but one remains closed.");
                    System.out.print("Enter Command: ");
                    cmd = sc.nextLine().toLowerCase();
                    if (cmd.equals("use locker")) {
                        System.out.println("Opening the locker, it appears to have belonged to one of the ship's repairmen. \n" +
                                "There are several spacesuits hanging, with a pile of clothes on the bottom.");
                        System.out.print("Enter Command: ");
                        cmd = sc.nextLine().toLowerCase();
                        if (cmd.equals("feel locker")) {
                            System.out.println("You feel around the bottom of the locker and come across some chewed gum stuck to the locker, which is really great. \n" +
                                    "You love finding that. As you push aside the jackets hanging in the locker, you reveal a large metal cylinder. You found a fuel injector!");
                            System.out.print("Enter Command: ");
                            cmd = sc.nextLine().toLowerCase();
                            if (cmd.equals("pick up injector")) {
                                inventoryManagement.pickUpItem(10, "injector");
                            }
                        } else if (cmd.equals("pick up spacesuit")) {
                            inventoryManagement.pickUpItem(7, "spacesuit");
                        }
                    }
                }
                else if (cmd.equals("move north")) {
                    currentRoom = 12;
                    running = false;
                }
                else if (cmd.equals("move west")) {
                    currentRoom = 15;
                    running = false;
                }
            }
            else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room14
     * Purpose: controls room 14 - Reactor [floor 14]
     *
     * @throws GameException
     */
    private void room14() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom) + "\n" );
        System.out.println(getRoomDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while(running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();
            if(cmd.equals("move north")) {
                currentRoom = 15;
                running = false;
            }
            else if(cmd.equals("move east")) {
                currentRoom = 17;
                running = false;
            }
            else if(cmd.equals("move south")) {
                currentRoom = 16;
                running = false;
            }
            else if(cmd.equals("move west")) {
                currentRoom = 18;
                running = false;
            }
            else if(cmd.equals("look at reactor")) {
                if(getMarkerActive("Flashlight On") == 1) {
                    System.out.println("With the light from your flashlight, you quickly notice that the space where the reactor’s fuel injector is supposed to be is empty. \nYou won’t be able to restore power without it in the reactor.");
                    System.out.print("Enter command: ");
                    String action = sc.nextLine().toLowerCase();
                    if(action.equals("use fuel injector")) {
                        if (inventoryManagerDB.getPlayerInventoryItems().contains(10)) {
                            System.out.println("You put the fuel injector in and click it into place. You check the reactor one last time and press the large blue start button on the control panel. \nIt takes some time, but the reactor eventually starts, and you see a weak glow coming from the reactor core. The reactor control panel lights up with a warning: “LOW FUEL”. \nThe lights in the room flicker before turning off again. It seems there is not enough power for the whole ship, although you hope it will at least be enough to power one of the engines. ");
                            setMarkerActive("Reactor Fixed");
                        }
                        else {
                            System.out.println("Injector not in inventory");
                        }
                    }
                    else {
                        System.out.println("You can't do that right now");
                    }
                }
                else {
                    System.out.println("You stare and stare, but you just can’t make your eyes give you the answers you want. Who made these things, anyway?!");
                }
            }
            else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room16
     * Purpose: Controls room 16 - Hallway 4 [Floor 4]
     *
     * @throws GameException
     */
    public void room16() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom) + "\n");
        System.out.println(getRoomDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while (running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();
            if (cmd.equals("move north")) {
                currentRoom = 14;
                running = false;
            }
            if (cmd.equals("move east")) {
                currentRoom = 20;
                running = false;
            } else if (cmd.equals("move south")) {
                if (getMarkerActive("Reactor Fixed") == 1) {
                    currentRoom = 21;
                    running = false;
                } else {
                    System.out.println("The door won't open! It seems to be in some sort of lockdown");
                }
            } else if (cmd.equals("move west")) {
                currentRoom = 19;
                running = false;
            } else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room17
     *
     * @throws GameException
     */
    public void room17() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom) + "\n" );
        if(getMarkerActive("Floor 4 Pirate Killed") == 1) {
            if(inventoryManagerDB.getPlayerInventoryItems().contains(8) && inventoryManagerDB.getPlayerInventoryItems().contains(6)) {
                System.out.println("You enter the workshop. The space pirate lies dead on the floor.");
            }
            else if(inventoryManagerDB.getPlayerInventoryItems().contains(8)) {
                System.out.println("You enter the workshop. The space pirate lies dead on the floor, with his melted railgun next to him.");
            }
            else if(inventoryManagerDB.getPlayerInventoryItems().contains(6)) {
                System.out.println("You enter the workshop. The space pirate lies dead on the floor, wearing a pair of high-tech metal boots.");
            }
            else {
                System.out.println("You enter the workshop. The space pirate lies dead on the floor, wearing a pair of high-tech metal boots. \nHis railgun sits next to him on the floor.");
            }
        }
        else if(getMarkerActive("Flashlight On") == 0) {
            System.out.println("You move into the workshop, but have barely entered the room when you see a figure near a desk, observing something. \nTheir headlight reflects off the object on the desk and reveals a familiar logo on their suit. It’s a space pirate! \nThey don’t appear to have noticed you yet, and you could probably avoid a confrontation if you go back the way you came.");
        }
        else {
            System.out.println("You move into the workshop but have barely entered the room when you see a figure near a desk, observing something. \nThey notice the light coming from your flashlight and spin around to face you. You notice a familiar logo on their suit. It’s a space pirate! \nThey raise their railgun and prepare to shoot at you.");
        }
        room17Pirate();
        System.out.println();
    }

    /**
     * Method: room17Pirate
     * Purpose: Controls the pirate game in room 17
     * @throws GameException
     */
    private void room17Pirate() throws GameException, SQLException, ClassNotFoundException {
        boolean running = true;
        sc = new Scanner(System.in);
        while(running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();
            if(cmd.equals("move north") || cmd.equals("move east")) {
                System.out.println("You attempt to go through the door on the other side of the room, but while moving you bump into a wall and the pirate notices you. \nThey raise their railgun and prepare to shoot at you.");
            }
            else if(cmd.equals("move south")) {
                currentRoom = 20;
                running = false;
            }
            else if(cmd.equals("move west")) {
                currentRoom = 14;
                running = false;
            }
            else if(cmd.equals("use axe") || cmd.equals("use ax")) {
                if(inventoryManagerDB.getPlayerInventoryItems().contains(18)) {
                    System.out.println("You charge at the pirate with the axe, but you are too slow. He shoots his railgun, which shatters the head of the axe and sends shrapnel flying everywhere. \nHis armored suit protects him from the shrapnel. Your lack of one does not. \n[GAME OVER]");
                    System.exit(0);
                }
                else {
                    System.out.println("Axe not in inventory");
                }
            }
            else if(cmd.equals("use cleaning spray") || cmd.equals("use spray")) {
                if(inventoryManagerDB.getPlayerInventoryItems().contains(4)) {
                    System.out.println("You throw the cleaning spray at the pirate. The pirate shoots his railgun at you, but his shot hits the cleaning spray instead of you. \nThe bottle explodes in a fireball next to the pirate, throwing you backwards. Thankfully, you survive unhurt. \nThe same cannot be said for the pirate who you now see lying on the ground, his railgun lying next to him. \nHe seems to be wearing a pair of high-tech metal boots.");
                    setMarkerActive("Floor 4 Pirate Killed");
                    room17Items();
                    running = false;
                }
            }
            else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room17Items
     * Purpose: controls items in room 17
     * @throws GameException
     */
    private void room17Items() throws GameException, SQLException, ClassNotFoundException {
        boolean running = true;
        sc = new Scanner(System.in);
        while(running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();
            if(cmd.equals("look at desk")) {
                if(cmd.equals("pick up blowtorch") || cmd.equals("pick up torch")) {
                    System.out.println("You take the blowtorch. It is not full on fuel, but there seems to be enough to get some use out of it…");
                    inventoryManagement.pickUpItem(9, "blowtorch");
                    running = false;
                }
            }
            else if(cmd.equals("pick up railgun") || cmd.equals("pick up gun")) {
                if(inventoryManagerDB.getPlayerInventoryItems().contains(6)) {
                    System.out.println("You pick up the railgun. It is partially melted from the explosion, and you doubt it works anymore. \nHowever, you notice there is still an ammunition cartridge in the railgun which has been left undamaged. \nYou leave the gun but take the ammo cartridge with you.");
                    inventoryManagement.pickUpItem(6, "railgun");
                    running = false;
                }
                else {
                    System.out.println("You have a crippling moment of déjà vu as the gun slides from your limp fingers. \nWhen you finish drooling and come to your senses you remember this gun was melted by spaceship cleaning agents and is of no use to you.");
                    inventoryManagement.pickUpItem(16, "ammo");
                    running = false;
                }
            }
            else if(cmd.equals("pick up metal boots") || cmd.equals("pick up boots")) {
                System.out.println("You take the boots off the pirate and observe them. They appear to be Mag Boots, which allow you to magnetically ground to surfaces even when there is no gravity.");
                inventoryManagement.pickUpItem(8, "boots");
                running = false;
            }
            else if(cmd.equals("use boots") || cmd.equals("use mag boots") || cmd.equals("use metal boots")) {
                if(inventoryManagerDB.getPlayerInventoryItems().contains(8)) {
                    if(getMarkerActive("Boots Equipped") == 0) {
                        System.out.println("You slide the mag Boots onto your feet");
                        setMarkerActive("Boots Equipped");
                        running = false;
                    }
                }
            }
            else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room20
     * Purpose: Controls room 20 - Engine 2 [floor 4]
     *
     * @throws GameException
     */
    public void room20() throws GameException, SQLException, ClassNotFoundException {
        boolean unlockCommands = false;
        boolean unlockSecondCommands = false;
        int firstAttempt = 1;
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom) + "\n" );
        System.out.println(getRoomDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while(running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();
            if(cmd.equals("look at pipes")) {
                System.out.println("Standard engine room pipes. They line most of the walls, with small gaps between them showing a metal surface. \nLooks like someone has kept them in great condition.");
                unlockCommands = true;
            }
            else if(cmd.equals("feel pipes") && unlockCommands && firstAttempt == 1) {
                System.out.println("You feel around the pipes. There are braces from the metal walls to the pipes to help hold them in place, but nothing of interest.");
                firstAttempt = 2;
            }
            else if(cmd.equals("feel pipes" ) && unlockCommands && firstAttempt == 2) {
                System.out.println("You feel around more of the pipes. Your hands bump into something that clinks against the pipe and makes a sloshing sound. It feels like a flask.");
                unlockSecondCommands = true;
            }
            else if(cmd.equals("pick up flask") && unlockCommands && firstAttempt == 2) {
                inventoryManagement.pickUpItem(14, "flask");
            }
            else if(cmd.equals("look at flask") && unlockSecondCommands) {
                System.out.println("You found a flask");
            }
            else if(cmd.equals("use flask") && unlockSecondCommands) {
                if(inventoryManagerDB.getPlayerInventoryItems().contains(14)) {
                    System.out.println("flask used");
                }
                else {
                    System.out.println("flask not in inventory");
                }
            }
            else if(cmd.equals("move west")) {
                currentRoom = 16;
                running = false;
            }
            else if(cmd.equals("move north")) {
                currentRoom = 17;
                running = false;
            }
            else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room21
     * Purpose: Controls the room 21 - Airlock-04F [Floor 4]
     *
     * @throws GameException
     */
    private void room21() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom) + "\n");
        System.out.println(getRoomDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while (running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();
            if (cmd.equals("move north")) {
                currentRoom = 16;
                running = false;
            } else if (cmd.equals("move south")) {
                if (getMarkerActive("Spacesuit On") == 0) {
                    System.out.println("ou open the door. Without a spacesuit on, the air is immediately sucked from your lungs, and you are killed almost instantly by the vacuum of space. \n[Game Over]");
                    System.exit(0);
                } else if (getMarkerActive("Mag Boots On") == 0) {
                    System.out.println("You float peacefully through the open airlock, soaking in the beauty of space. \nAs you gaze upon the stars you can see the ship getting smaller in the distance. After a while your oxygen tank runs out and you suffocate to death. \n[GAME OVER]");
                    System.exit(0);
                } else {
                    currentRoom = 22;
                    running = false;
                }
            } else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room22
     * Purpose: Controls room 22 - Space - Outside Airlock-04F
     *
     * @throws GameException
     */
    private void room22() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom) + "\n");
        System.out.println(getRoomDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while (running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();
            if (cmd.equals("use mag boots") || cmd.equals("use boots") || cmd.equals("use magnetic boots")) {
                if (inventoryManagerDB.getPlayerInventoryItems().contains(8)) {
                    System.out.println("You click your boots  together and the magnetic focuses deactivate. You float peacefully away, soaking in the beauty of space. \nAs you gaze upon the stars you can see the ship getting smaller in the distance. \nAfter a while your oxygen tank runs out and you suffocate to death. \n[GAME OVER]");
                    System.exit(0);
                }
            } else if (cmd.startsWith("use")) {
                String object = cmd.substring(3).trim();
                if (object.isEmpty()) {
                    System.out.println("Unrecognized command. Type ‘help’ for a list of commands.");
                } else {
                    System.out.println("A standard spacesuit for maintenance work or long walks on the outer hull of a ship. \nKeeps you insulated from the low temperatures and radiation of space, and provides you with oxygen.");
                }
            } else if (cmd.startsWith("drop")) {
                String object = cmd.substring(4).trim();
                int itemID = inventoryManagerDB.getItemIDByName(object);
                inventoryManagerDB.removeItemFromInventory(itemID);
                System.out.println("You drop the item and watch as it floats away into space. Hope it wasn’t important. ");
            } else if (cmd.equals("move bow") || cmd.equals("move b")) {
                currentRoom = 23;
                running = false;
            } else if (cmd.equals("move down") || cmd.equals("move below") || cmd.equals("move d")) {
                currentRoom = 21;
                running = false;
            } else if (cmd.equals("move stern") || cmd.equals("move s")) {
                System.out.println("You can see the engines at the back of the ship, so you are fairly close to the Stern. \nYou decide there is probably nothing over there you need to investigate.");
            } else if (cmd.equals("move port") || cmd.equals("move p")) {
                System.out.println("You walk toward the Port side of the ship with no particular goal in mind. You continue walking for a while. \nEventually you end up back where you started, mildly confused and sad.");
            } else if (cmd.equals("move starboard") || cmd.equals("move sb")) {
                System.out.println("You walk toward the Starboard side of the ship with no particular goal in mind. You continue walking for a while. \nEventually you end up back where you started, mildly confused and sad.");
            } else if (cmd.equals("move hull") || cmd.equals("move h")) {
                System.out.println("You are already on the Hull (bottom) of the ship.");
            } else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room23
     * Purpose: Controls room 23 - Space - Outside Airlock-03F
     *
     * @throws GameException
     * @throws SQLException
     */
    private void room23() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom) + "\n");
        System.out.println(getRoomDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while (running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();
            if (cmd.equals("use mag boots") || cmd.equals("use boots") || cmd.equals("use magnetic boots")) {
                if (inventoryManagerDB.getPlayerInventoryItems().contains(8)) {
                    System.out.println("You click your boots  together and the magnetic focuses deactivate. You float peacefully away, soaking in the beauty of space. \nAs you gaze upon the stars you can see the ship getting smaller in the distance. \nAfter a while your oxygen tank runs out and you suffocate to death. \n[GAME OVER]");
                    System.exit(0);
                }
            } else if (cmd.startsWith("use")) {
                String object = cmd.substring(3).trim();
                if (object.isEmpty()) {
                    System.out.println("Unrecognized command. Type ‘help’ for a list of commands.");
                } else {
                    System.out.println("A standard spacesuit for maintenance work or long walks on the outer hull of a ship. \nKeeps you insulated from the low temperatures and radiation of space, and provides you with oxygen.");
                }
            } else if (cmd.startsWith("drop")) {
                String object = cmd.substring(4).trim();
                int itemID = inventoryManagerDB.getItemIDByName(object);
                inventoryManagerDB.removeItemFromInventory(itemID);
                System.out.println("You drop the item and watch as it floats away into space. Hope it wasn’t important. ");
            } else if (cmd.equals("move down") || cmd.equals("move below") || cmd.equals("move d")) {
                currentRoom = 22;
                running = false;
            } else if (cmd.equals("move bow") || cmd.equals("move b")) {
                System.out.println("You walk toward the Bow (front) of the ship.");
                currentRoom = 24;
                running = false;
            } else if (cmd.equals("move stern") || cmd.equals("move s")) {
                currentRoom = 22;
                running = false;
            } else if (cmd.equals("move port") || cmd.equals("move p")) {
                System.out.println("You walk toward the Port side of the ship with no particular goal in mind. You are now on the Port side of the ship. \nToward the Bow you see a massive chunk torn out of the outer casing of the ship. On the other side of that chasm, you can see another airlock.");
                System.out.print("Enter Command: ");
                String secondCommand = sc.nextLine().toLowerCase();
                if (secondCommand.equals("move hull") || secondCommand.equals("move h")) {
                    System.out.println("You walk toward the Port side of the ship with no particular goal in mind. You are now on the Port side of the ship. \nToward the Bow you see a massive chunk torn out of the outer casing of the ship. On the other side of that chasm, you can see another airlock.");
                } else if (secondCommand.equals("move bow") || secondCommand.equals("move b")) {
                    System.out.println("You walk toward the chasm and hop off, floating into space. \nYou immediately realize this was a terrible idea, but it takes about an hour for your body to realize it. You slowly suffocate to death. \n[GAME OVER]");
                    System.exit(0);
                } else if (secondCommand.equals("move stern") || secondCommand.equals("move s") || secondCommand.equals("move deck") || secondCommand.equals("move d")) {
                    System.out.println("You actually decide to keep things simple and not do that.");
                }
            } else if (cmd.equals("move starboard") || cmd.equals("move sb")) {
                System.out.println("You walk toward the Starboard side of the ship with no particular goal in mind. You continue walking for a while. Eventually you end up on the Port side of the ship. \nToward the Bow you see a massive chunk torn out of the outer casing of the ship. On the other side of that chasm, you can see another airlock.");
                System.out.print("Enter Command: ");
                String secondCommand = sc.nextLine().toLowerCase();
                if (secondCommand.equals("move hull") || secondCommand.equals("move h")) {
                    System.out.println("You walk toward the Starboard side of the ship with no particular goal in mind. You continue walking for a while. Eventually you end up on the Port side of the ship. \nToward the Bow you see a massive chunk torn out of the outer casing of the ship. On the other side of that chasm, you can see another airlock.");
                } else if (secondCommand.equals("move bow") || secondCommand.equals("move b")) {
                    System.out.println("You walk toward the chasm and hop off, floating into space. \nYou immediately realize this was a terrible idea, but it takes about an hour for your body to realize it. You slowly suffocate to death. \n[GAME OVER]");
                    System.exit(0);
                } else if (secondCommand.equals("move stern") || secondCommand.equals("move s") || secondCommand.equals("move deck") || secondCommand.equals("move d")) {
                    System.out.println("You actually decide to keep things simple and not do that.");
                }
            } else if (cmd.equals("move h") || cmd.equals("move hull")) {
                System.out.println("You are already on the Hull (bottom) of the ship.");
            } else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room24
     * Purpose: controls room 24 - Space - Outside Floor 2
     *
     * @throws GameException
     * @throws SQLException
     */
    private void room24() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom) + "\n");
        System.out.println(getRoomDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while (running) {
            System.out.println("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();
            if (cmd.equals("use mag boots") || cmd.equals("use boots") || cmd.equals("use magnetic boots")) {
                if (inventoryManagerDB.getPlayerInventoryItems().contains(8)) {
                    System.out.println("You click your boots  together and the magnetic focuses deactivate. You float peacefully away, soaking in the beauty of space. \nAs you gaze upon the stars you can see the ship getting smaller in the distance. \nAfter a while your oxygen tank runs out and you suffocate to death. \n[GAME OVER]");
                    System.exit(0);
                }
            } else if (cmd.startsWith("use")) {
                String object = cmd.substring(3).trim();
                if (object.isEmpty()) {
                    System.out.println("Unrecognized command. Type ‘help’ for a list of commands.");
                } else {
                    System.out.println("A standard spacesuit for maintenance work or long walks on the outer hull of a ship. \nKeeps you insulated from the low temperatures and radiation of space, and provides you with oxygen.");
                }
            } else if (cmd.startsWith("drop")) {
                String object = cmd.substring(4).trim();
                int itemID = inventoryManagerDB.getItemIDByName(object);
                inventoryManagerDB.removeItemFromInventory(itemID);
                System.out.println("You drop the item and watch as it floats away into space. Hope it wasn’t important. ");
            } else if (cmd.equals("move down") || cmd.equals("move below") || cmd.equals("move d")) {
                System.out.println("There is no longer any way to access this airlock.");
            } else if (cmd.equals("move port") || cmd.equals("move p")) {
                System.out.println("You decide life just isn’t worth living. You launch yourself toward the nothingness that was once the port side of the second floor. \nIt takes about an hour for you to suffocate, so the dramatic pause for effect is longer than you anticipated, but it’s all the same to you. \n[GAME OVER]");
                System.exit(0);
            } else if (cmd.equals("move starboard") || cmd.equals("move sb")) {
                System.out.println("You decide life just isn’t worth living. You launch yourself toward the nothingness that was once the starboard side of the second floor. \nIt takes about an hour for you to suffocate, so the dramatic pause for effect is longer than you anticipated, but it’s all the same to you. \nJust before your time in this universe is up, you spot a majestic space whale drifting across the currents of space. Just for that, it was all worth it in the end. \n[GAME OVER]");
                setAchievementCompleted("Aee Aee Uuee Eeaoiaiee");
                System.exit(0);
            } else if (cmd.equals("move stern") || cmd.equals("move s")) {
                currentRoom = 23;
                running = false;
            } else if (cmd.equals("move bow") || cmd.equals("move b")) {
                currentRoom = 25;
                running = false;
            } else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    private void room25() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(getRoomName(currentRoom) + " " + getRoomFloor(currentRoom) + "\n");
        System.out.println(getRoomDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while (running) {
            System.out.println("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();
            if (cmd.equals("mag boots") || cmd.equals("boots") || cmd.equals("magnetic boots")) {
                if (inventoryManagerDB.getPlayerInventoryItems().contains(8)) {
                    System.out.println("You click your boots  together and the magnetic focuses deactivate. You float peacefully away, soaking in the beauty of space. \nAs you gaze upon the stars you can see the ship getting smaller in the distance. \nAfter a while your oxygen tank runs out and you suffocate to death. \n[GAME OVER]");
                    System.exit(0);
                }
            } else if (cmd.startsWith("use")) {
                String object = cmd.substring(3).trim();
                if (object.isEmpty()) {
                    System.out.println("Unrecognized command. Type ‘help’ for a list of commands.");
                } else {
                    System.out.println("A standard spacesuit for maintenance work or long walks on the outer hull of a ship. \nKeeps you insulated from the low temperatures and radiation of space, and provides you with oxygen.");
                }
            } else if (cmd.startsWith("drop")) {
                String object = cmd.substring(4).trim();
                int itemID = inventoryManagerDB.getItemIDByName(object);
                inventoryManagerDB.removeItemFromInventory(itemID);
                System.out.println("You drop the item and watch as it floats away into space. Hope it wasn’t important. ");
            } else if (cmd.equals("move down") || cmd.equals("move below") || cmd.equals("move d")) {
                System.out.println("With a sigh of relief, you return to a the world of cardinal directions. ");
                currentRoom = 26;
                running = false;
            } else if (cmd.equals("move port") || cmd.equals("move p")) {
                System.out.println("You walk toward the Port side of the ship with no particular goal in mind. You continue walking for a while. \nEventually you end up back where you started, mildly confused and sad.");
            } else if (cmd.equals("move starboard") || cmd.equals("move sb")) {
                System.out.println("You walk toward the Starboard side of the ship with no particular goal in mind. You continue walking for a while. \nEventually you end up back where you started, mildly confused and sad.");
            } else if (cmd.equals("move bow") || cmd.equals("move b")) {
                System.out.println("You are already at the front of the ship.");
            } else if (cmd.equals("move stern") || cmd.equals("move s")) {
                currentRoom = 24;
                running = false;
            } else {
                commands(cmd);
            }
        }
        System.out.println();
    }

    /**
     * Method: room26
     * Purpose: handles the specific functions of room26
     *
     * @throws GameException
     * @throws SQLException
     */
    // FINISHED
    public void room26(String action, String argument) throws GameException, SQLException, ClassNotFoundException {
        if (argument.equalsIgnoreCase("east")) {
            if (markerDB.getMarkerByID(10).getActive() == 0) {
                System.out.println("You open the door. Without a spacesuit on, the air is immediately sucked from your lungs, \n" +
                        "and you are killed almost instantly by the vacuum of space. \n[GAME OVER]");
                System.exit(0);
            }
            if (markerDB.getMarkerByID(11).getActive() == 0) {
                System.out.println(": You float peacefully through the open airlock, soaking in the beauty of space. \n" +
                        "As you gaze upon the stars you can see the ship getting smaller in the distance. After a while your oxygen tank runs out and you suffocate to death. \n[GAME OVER]");
                System.exit(0);
            } else {
                currentRoom = 25;
            }
        } else if (action.equalsIgnoreCase("drop") || action.equalsIgnoreCase("pick up") || action.equalsIgnoreCase("use")) {
            handleInventoryCommands(action, argument);
        } else {
            System.out.println("Too many arguments entered for " + action + " command.");
        }
    }

    /**
     * Method: room28
     * Purpose: Controls room 28 - 28	Research Lab [Floor 1]
     *
     * @throws GameException
     */
    public void room28() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(currentRoom + " " + getRoomFloor(currentRoom) + "\n");
        System.out.println(getRoomDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while (running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();

            if (cmd.equals("look at trail") | cmd.equals("look at liquid")) {
                System.out.println("You follow the trail of blue liquid behind the desk \n" +
                        "that holds several specimen jars. Underneath the desk, a trash can lies knocked over.");
                cmd = sc.nextLine().toLowerCase();
                if (cmd.equals("use trash") | cmd.equals("use trash can")) {
                    System.out.println("As you approach the trashcan you hear a rustling and decide to check inside. \n" +
                            "There, feasting on the trash is a gelatinous blob of an alien, about the size of your shoe.\n" +
                            " As you watch it, you realize it’s actually kind of cute just happily eating its dinner. \n" +
                            "You must have said that aloud, because the alien turns to look at you. \n" +
                            "The alien begins to rapidly grow, surpassing you in height. It takes a deep \n" +
                            "breath while staring down on you, before spewing fluid that just misses you but covers the \n" +
                            "desk. The desk begins to corrode, leaving a hole in the center, as the alien moves towards you.");
                    cmd = sc.nextLine().toLowerCase();
                    if (cmd.equals("feel alien")) {
                        System.out.println("You reach out gingerly toward the alien. It pauses and trembles, making an odd " +
                                "cooing noise like some kind of extraterrestrial dove. You lock eyes, then begin gently " +
                                "stroking it. It feels smooth and… slimy. With a loud burping sound, the alien once again " +
                                "throws up the corrosive fluid, this time drenching you in it. [GAME OVER]");
                        System.exit(0);
                    } else if (cmd.equals("move west")) {
                        System.out.println("As you try to get the door to the Medical Bay open, a shower of corrosive vomit disintegrates \n" +
                                "the doorway, and you along with it. [GAME OVER] ");
                        System.exit(0);
                    }
                } else if (cmd.equals("look at jar") | cmd.equals("look at jars")) {
                    System.out.println("Among the sample jars you see one that is unused.");
                    inventoryManagement.pickUpItem(12, "Jar");
                }

                if (cmd.equals("use tranq on alien") | cmd.equals("use tranq gun on alien") | cmd.equals("use tranquilizer gun on alien")) {
                    System.out.println("You aim the tranq gun at the alien, just before it inhales to release more fluid. The tranquilizer\n" +
                            " pierces its gelatinous form, and it lets out a hiss before it begins to shrink down to its original size. \n " +
                            "Now sleeping, you need to find a way to trap it.");
                }

                if (cmd.equals("use jar")) {
                    System.out.println("You bend down to the alien who is now the size of a slug and scoop it up with the jar.\n" +
                            " You securely fasten the lid and stick it into your backpack for safe keeping. You don’t need \n" +
                            "that creature loose again… ");
                    setAchievementCompleted("Contained");
                }

                if (cmd.equals("move west")) {
                    currentRoom = 8;
                    running = false;
                }
                else {
                    commands(cmd);
                }
            }
        }
    }


    /**
     * Method: room31
     * Purpose: Controls room 31 - 31	Armory [Floor 1]
     *
     * @throws GameException
     */
    public void room31() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(currentRoom + " " + getRoomFloor(currentRoom) + "\n");
        System.out.println(getRoomDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while (running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();

            if (cmd.equals("move east")) {
                currentRoom = 32;
                running = false;
            }
            if (cmd.equals("move west")) {
                currentRoom = 33;
                running = false;
            }

            if (cmd.equals("look at case")) {
                System.out.println("There is a single case that is only slightly\n" +
                        " damaged. The case seems to have the same acrylic material as \n" +
                        "the window in the terrarium.");
                setMarkerActive("Blowtorch Used");
                cmd = sc.nextLine().toLowerCase();
                if (cmd.equals("pick up gun")) {
                    //if player does not have ammo
                    if (inventoryManagerDB.getPlayerInventoryItems().contains(16)) {
                        System.out.println("You pick up the gun. There’s gotta be some ammo around somewhere… ");
                        //adds gun to inventory
                        inventoryManagement.pickUpItem(6, "Gun");
                    } else {
                        System.out.println("You pick up the gun and load the ammo you have into it. ");
                        //adds gun to inventory and removes ammo from inventory
                        inventoryManagement.pickUpItem(6, "Gun");
                        handleInventoryCommands("drop", "ammo");
                    }
                }
            }
            else {
                commands(cmd);
            }
        }
    }

    /**
     * Method: room33
     * Purpose: Controls room 33 - Command Information Center (CIC) [Floor 1]
     *
     * @throws GameException
     */

    public void room33() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(currentRoom + " " + getRoomFloor(currentRoom) + "\n");
        System.out.println(getRoomDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while (running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();

            if (cmd.equals("use railgun on pirate")) {
                //if player has an item print this
                if (inventoryManagerDB.getPlayerInventoryItems().contains(6)) {
                    System.out.println("You take out the railgun you got from the armory. \n" +
                            "The pirate seems surprised that you have a weapon, and his aim momentarily wavers. \n" +
                            "You take the opportunity and fire your gun at the pirate");
                    setMarkerActive("Pirate Killed");
                } else {
                    //else
                    System.out.println("You take out the railgun you got from the armory.\n" +
                            " The pirate seems surprised that you have a weapon, and his aim \n" +
                            "momentarily wavers. However, you have no ammo for the gun, a fact you only\n " +
                            "realize when you attempt to fire it. The pirate shoots his railgun at you. [GAME OVER]");
                    System.exit(0);
                }
            }

            if (cmd.equals("move south")) {
                if (getMarkerActive("CIC Pirate Killed") == 0) {
                    System.out.println("You attempt to flee, but the pirate shoots \n" +
                            "and hits you before you reach the door. [GAME OVER]");
                    System.exit(0);
                } else {
                    //supposed to exit to hallway 7
                    currentRoom = 29;
                    running = false;
                }
            }

            if (cmd.equals("move north")) {
                if (getMarkerActive("CIC Pirate Killed") == 0) {
                    System.out.println("You attempt to flee, but the pirate shoots \n" +
                            "and hits you before you reach the door.");
                    running = false;
                } else {
                    currentRoom = 34;
                    running = false;
                }
            }
            else {
                commands(cmd);
            }
        }
    }


    /**
     * Method: room34
     * Purpose: Controls room 34 - Flight Deck [Floor 1]
     *
     * @throws GameException
     */
    public void room34() throws GameException, SQLException, ClassNotFoundException {
        System.out.println(currentRoom + " " + getRoomFloor(currentRoom) + "\n");
        System.out.println(getSecondDescription(currentRoom));
        boolean running = true;
        sc = new Scanner(System.in);
        while (running) {
            System.out.print("Enter Command: ");
            String cmd = sc.nextLine().toLowerCase();
            System.out.println();

            if (cmd.equals("use gun") || cmd.equals("use railgun")) {
                if (getMarkerActive("Boss Damaged") == 1) {
                    System.out.println("Without his bulletproof leathers to protect him, the gun lands a direct hit, \n" +
                            "and he falls to the floor. You have finally defeated the space raider! You notice a message\n" +
                            " appear on the flight deck’s main control panel. ");
                    setAchievementCompleted("Hostile Takeover");
                    setMarkerActive("Boss Killed");
                } else if (getMarkerActive("Boss Killed") == 0) {
                    System.out.println("You fire your gun at the space raider. It lands a direct hit,\n" +
                            " but when the smoke clears you see him still standing, unhurt. His leathers easily absorbed \n" +
                            "the shot. He aims his gun at you and fires, tearing a hole in your chest. [GAME OVER]");
                    System.exit(0);
                }
            }

            if (cmd.equals("use axe") || cmd.equals("use ax")) {
                if (getMarkerActive("Boss Damaged") == 1) {
                    System.out.println("You lunge toward the space raider, swinging your axe with reckless abandon. \n" +
                            "Without his leather to protect him, the axe cleaves him in two. You think he might be dead. \n" +
                            "You have finally defeated the space raider! You notice a message appear on the flight deck’s main control panel. ");
                    setAchievementCompleted("Hostile Takeover");
                    setMarkerActive("Boss Killed");
                } else if (getMarkerActive("Boss Killed") == 0) {
                    System.out.println("You lunge toward the space raider, swinging your axe with reckless abandon. \n" +
                            "It lands a direct hit, tearing off a large chunk of his leather armor. He leaps backward, \n" +
                            "aims his gun at you, and fires, tearing a hole in your chest. [GAME OVER]");
                    System.exit(0);
                }
            }

            if (cmd.equals("use alien") | cmd.equals("use jar")) {
                if (getMarkerActive("Boss Killed") == 1 & getMarkerActive("Injured Leg") == 1) {
                    System.out.println("You hurl the jar towards the raider, where it shatters on the ground near his\n" +
                            " feet. The alien lets out a hiss, as it waddles its way onto the floor. You swear it’s\n " +
                            "growing with every stride it makes. The raider looks annoyed and fires his railgun at the alien. \n" +
                            "The extraterrestrial’s gelatinous form swiftly flattens and rolls, dodging the shot as it lets out an ear-piercing\n" +
                            " scream. As the alien dodges the railgun blast, you try to dive out of the way. Your injured leg slows you \n" +
                            "down, and a smoldering hole blossoms from your chest as you hit the ground. You look up from the floor, seeing the \n" +
                            "alien now hovering over the raider as the life fades from your body. [GAME OVER]");
                    System.exit(0);
                } else if (getMarkerActive("Boss Killed") == 0 & getMarkerActive("Injured Leg") == 0) {
                    System.out.println("You hurl the jar towards the raider, where it shatters on the ground near his feet. The alien lets out a hiss,\n" +
                            " as it waddles its way onto the floor. You swear it’s growing with every stride it makes. The raider looks annoyed and fires his railgun\n " +
                            "at the alien. The extraterrestrial’s gelatinous form swiftly flattens and rolls, dodging the shot as it lets out \n" +
                            "an ear-piercing scream. As the alien dodges the railgun blast, you dive to the floor, barely dodging the shot yourself. It leaves a\n " +
                            "smoldering hole on the wall behind you. You look up from the floor, seeing the alien is now hovering over the raider. It appears the creature\n " +
                            "is still growing, inhaling deeply and making screeching noises. The raider asks it if it’s going to keep screaming with an uncertain chuckle. \n" +
                            "Instead of releasing another wail, the alien lets out a stream of its corrosive vomit. The space raider looks disgusted for a moment before he \n" +
                            "realizes his leathers are deteriorating. He throws off his protective leathers and begins blasting the alien frantically with his railgun. \n" +
                            "As smoldering holes blossom out of the alien it begins to disintegrate into a mass of bubbling, moaning goo. You stand to your feet as the pirate \n" +
                            "starts to reload his gun, throwing nervous glances toward you. ");
                    setMarkerActive("Boss Damaged");
                    //add command to remove jar from inventory
                    handleInventoryCommands("drop", "jar");
                }
            }

            if (cmd.equals("look at control panel")) {
                if (getMarkerActive("Boss Killed") == 1 & getMarkerActive("Reactor Fixed") == 1) {
                    System.out.println("You look at the control panel. You see a message: “Engine 1 online. Found 1 nearby station within range. Pilot ship to station?”");
                    sc = new Scanner(System.in);
                    if (cmd.equals("use control panel")) {
                        if (getMarkerActive("Boss Killed") == 1) {
                            System.out.println("You access the control panel, setting the ship to autopilot toward the nearby station. As you wearily make your way out of" +
                                    " the flight deck to find some food, you resolve that once you reach port, you’re going to retire from the gas hauling business.\n" +
                                    "\n" +
                                    "Congratulations, you won the game!!! \n");
                            setAchievementCompleted("Just Another Job");
                            System.exit(0);
                        } else {
                            System.out.println("The control panel is blank.");
                            running = false;
                        }
                    }
                }
            }
            else {
                commands(cmd);
            }
        }
    }

    /**
     * Method: displayRoomDetails
     * it display the correct description of a room
     */
    public void displayRoomDetails(int roomID) throws GameException {
        Room room = roomDB.getRoom(roomID);
        System.out.println("\n" + room.getName());
        System.out.println(room.getFloor());
        if (currentRoom == 5 && markerDB.getMarkerByID(7).getActive() == 1) {
            System.out.println(roomDB.getRoom(5).getSecondDescription().replace("|", "\n") + "\n");  // Display the second description if flashlight is on
        } else {
            System.out.println(room.getDescription().replace("|", "\n") + "\n");  // Display the normal room description
        }
    }

    /**
     * Method: getPlayerCurrentRoom
     * Purpose: gets the player's current room from database
     * @param playerID
     * @return
     * @throws GameException
     */
    private int getPlayerCurrentRoom(int playerID) throws GameException {
        Player player = new Player(dbName);
        player = player.getPlayer(dbName, playerID);
        return player.getCurrentRoom();
    }

    /**
     * Method: setPlayerCurrentRoom
     * Purpose: set player's current room
     * @param roomID
     * @throws GameException
     */
    private void setPlayerCurrentRoom(int roomID) throws GameException {
        Player player = new Player(dbName);
        player = player.getPlayer(dbName, 1);
        player.setCurrentRoom(roomID);
        player.updatePlayer(dbName);
    }


    /**
     * Method: getRoomName
     * Purpose: gets room's name data
     *
     * @param roomID
     * @return String
     * @throws GameException
     */
    private String getRoomName(int roomID) throws GameException {
        Room room = new Room(dbName);
        room = room.getRoom(dbName, roomID);
        return room.getName();
    }

    /**
     * Method: getRoomFloor
     * Purpose: gets room's floor data
     *
     * @param roomID
     * @return
     * @throws GameException
     */
    private String getRoomFloor(int roomID) throws GameException {
        Room room = new Room(dbName);
        room = room.getRoom(dbName, roomID);
        return room.getFloor();
    }

    /**
     * Method: getRoomDescription
     * Purpose: Gets the room's description data
     *
     * @param roomID
     * @return String
     * @throws GameException
     */
    private String getRoomDescription(int roomID) throws GameException {
        Room room = new Room(dbName);
        room = room.getRoom(dbName, roomID);
        return room.getDescription().replace("|", "\n") + "\n";
    }

    /**
     * Method: getSecondDescription
     * Purpose: gets second description for a room
     *
     * @param roomID
     * @return
     * @throws GameException
     */
    private String getSecondDescription(int roomID) throws GameException {
        Room room = new Room(dbName);
        room = room.getRoom(dbName, roomID);
        return room.getSecondDescription().replace("|", "\n") + "\n";
    }

    /**
     * Method: getThirdDescription
     * Purpose: gets third description for a room
     *
     * @param roomID
     * @return
     * @throws GameException
     */
    private String getThirdDescription(int roomID) throws GameException {
        Room room = new Room(dbName);
        room = room.getRoom(dbName, roomID);
        return room.getThirdDescription().replace("|", "\n") + "\n";
    }

    /**
     * Method: getMarkerActive
     * Purpose: checks the marker's active status
     *
     * @param markerName
     * @return
     * @throws GameException
     */
    private int getMarkerActive(String markerName) throws GameException {
        Marker marker = new Marker(dbName);
        marker = marker.getMarker(dbName, markerName);
        return marker.getActive();
    }

    /**
     * Method: setMarkerActive
     * Purpose: sets a marker active to 1
     *
     * @param markerName
     * @throws GameException
     */
    private void setMarkerActive(String markerName) throws GameException {
        MarkerDB mdb = new MarkerDB(dbName);
        mdb.updateMarker(markerName);
    }

    private void setMarkerOff(String markerName) throws GameException {
        MarkerDB mdb = new MarkerDB(dbName);
        mdb.markerOff(markerName);
    }

    /**
     * Method: setAchievementCompleted
     * Purpose: sets an achievement status to completed
     *
     * @param achievementName
     * @throws GameException
     */
    private void setAchievementCompleted(String achievementName) throws GameException {
        AchievementDB adb = new AchievementDB(dbName);
        adb.updateAchievement(achievementName);
    }

    /**
     * Method: calculateScore
     * Purpose: Calculates the player's score
     *
     * @return
     * @throws GameException, SQLException, ClassNotFoundException
     */
    public int calculateScore(int playerID) throws GameException, SQLException, ClassNotFoundException {
        Player player = new Player(dbName);
        player = player.getPlayer(dbName, playerID);
        int score = player.getScore();
        AchievementDB achievementDB = new AchievementDB(dbName);
        //goes through every achievement
        //updates the achievement based on if the marker is active or not
        //adds value to score

        // Aee Aee Uuee Eeaoiaiee
        if(achievementDB.getAchievement(1).getCompleted() == 1) {
            score += achievementDB.getAchievement(1).getScoreValue();
        }
        // Hoader
        if(achievementDB.getAchievement(2).getCompleted() == 1) {
            score += achievementDB.getAchievement(2).getScoreValue();
        }
        // Security Disabled
        if(achievementDB.getAchievement(3).getCompleted() == 1) {
            score += achievementDB.getAchievement(3).getScoreValue();
        }
        // Contained
        if(achievementDB.getAchievement(4).getCompleted() == 1) {
            score += achievementDB.getAchievement(4).getScoreValue();
        }
        // Hostile Takeover
        if(achievementDB.getAchievement(5).getCompleted() == 1) {
            score += achievementDB.getAchievement(5).getScoreValue();
        }
        //Just Another Job
        if(achievementDB.getAchievement(6).getCompleted() == 1) {
            score += achievementDB.getAchievement(6).getScoreValue();
        }

        //updates players score and returns the score after
        player.setScore(score);
        return score;
    }

    /**
     * Method: displayAchievements
     * Purpose: displays the player's current achievements
     *
     * @throws GameException, SQLException, ClassNotFoundException
     */

    public void displayAchievements() throws GameException, SQLException, ClassNotFoundException {
        AchievementDB adb = new AchievementDB(dbName);

        //Hostile Takeover
        //if boss is killed
        System.out.print("Hostile Takeover | ");
        if (adb.getAchievement(5).getCompleted() == 1) {
            System.out.println(adb.getAchievement(5).getDescription());
        }
        else {
            System.out.println("???");
        }

        //Security Disabled
        //if the security droid is killed
        System.out.print("Security Disabled | ");
        if (adb.getAchievement(3).getCompleted() == 1) {
            System.out.println(adb.getAchievement(3).getDescription());
        }
        else {
            System.out.println("???");
        }

        //Aee Aee Uuee Eeaoiaiee
        //if room 34 is visited and player uses starboard command
        System.out.print("Aee Aee Uuee Eeaoiaiee | ");
        if (adb.getAchievement(1).getCompleted() == 1) {
            System.out.println(adb.getAchievement(1).getDescription());
        }
        else {
            System.out.println("???");
        }

        //Just another job
        //player beats the game
        System.out.print("Just Another Job | ");
        if (adb.getAchievement(6).getCompleted() == 1) {
            System.out.println(adb.getAchievement(6).getDescription());
        }
        else {
            System.out.println("???");
        }

        //Hoarder
        //if all items are found
        System.out.print("Hoarder | ");
        if (adb.getAchievement(2).getCompleted() == 1) {
            System.out.println(adb.getAchievement(2).getDescription());
        }
        else {
            System.out.println("???");
        }

        //Contained
        //alien trapped
        System.out.print("Contained | ");
        if (adb.getAchievement(4).getCompleted() == 1) {
            System.out.println(adb.getAchievement(4).getDescription());
        }
        else {
            System.out.println("???");
        }
    }

    /**
     * Method: displayScore
     * Purpose: displays the player's score
     *
     * @throws GameException, SQLException, ClassNotFoundException
     */

    public int displayScore(int playerID) throws GameException, SQLException, ClassNotFoundException {
        return calculateScore(playerID);
    }

    /**
     * Method: displayHelp
     * Purpose: displays all commands
     *
     * @throws GameException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void displayHelp() throws GameException, SQLException, ClassNotFoundException {
        System.out.println("Commands\n" +
                "Move<direction> - move in a direction\n" +
                "Use<item> - use an item\n" +
                "Use<item> [on] <object> - use an item on an object\n" +
                "Pick Up <item> - pick up an item\n" +
                "Drop <item> - drop an item\n" +
                "Feel <object> - feel an object\n" +
                "Look At Room - look at the room around you\n" +
                "Look At <object> - look at an object\n" +
                "Inventory - list the items in your inventory\n" +
                "New Game - restart from the beginning\n" +
                "Exit - exits the game\n" +
                "Hint - get a hint\n" +
                "Achievements/Score - view achievements and score\n" +
                "Logout - logs out of your account\n" +
                "Help - this command"
        );
    }


    /**
     * Method: newGame
     * Purpose: create a new game by creating the tables again
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws GameException
     */
    private void newGame() throws SQLException, ClassNotFoundException, GameException {
        GameDBCreate gdb = new GameDBCreate(dbName);
        gdb.buildTables();
        currentRoom = 1;
        setPlayerCurrentRoom(currentRoom);
        displayRoom();
    }

    /**
     * Method: exitGame
     * Purpose: exits the game
     */
    private void exitGame() {
        System.out.println("Exiting the Game");
        System.exit(0);
    }

    /**
     * Method: commands
     * Purpose: main commands for the game
     *
     * @param cmd
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws GameException
     */
    public void commands(String cmd) throws SQLException, ClassNotFoundException, GameException {
        if (cmd.equals("help")) {
            displayHelp();
        } else if (cmd.equals("exit")) {
            exitGame();
        } else if (cmd.equals("inventory")) {
            showInventory();
        } else if (cmd.equals("hint")) {
            hint();
        } else if (cmd.equals("new game")) {
            newGame();
        } else if(cmd.equals("score") || cmd.equals("achievements")) {
            System.out.print("Your Score: ");
            System.out.println(displayScore(1));
            displayAchievements();
        } else if (cmd.equals("logout")) {
            Console c = new Console();
            c.startGame();
        } else if(cmd.equals("look at room")) {
            displayRoomDetails(currentRoom);
        }
        else if (cmd.startsWith("drop")) {
            String object = cmd.substring(4).trim();
            int itemID = inventoryManagerDB.getItemIDByName(object);
            System.out.println(inventoryManagement.dropItem(itemID, object));
        }
        else {
            System.out.println("Unrecognized command. Type `help` for a list of commands.");
        }
    }
}

