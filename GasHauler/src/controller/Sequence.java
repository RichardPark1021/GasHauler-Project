package controller;

import model.SequenceDB;

/**
 * Class: Sequence
 * @author: Erika Attidzah
 * @version: 1.0
 * Course: ITEC 3860 FALL 2024
 * Written: 2 Nov, 2024
 *
 * This class creates a Sequence Object
 */
public class Sequence {
    private int sequenceID;
    private int commandID;
    private int unlockCommand;

    public Sequence(int sequenceID, int commandID, int unlockCommand) {
        this.sequenceID = sequenceID;
        this.commandID = commandID;
        this.unlockCommand = unlockCommand;
    }

    public int getSequenceID() {
        return sequenceID;
    }

    public int getCommandID() {
        return commandID;
    }

    public int getUnlockCommand() {
        return unlockCommand;
    }
}
