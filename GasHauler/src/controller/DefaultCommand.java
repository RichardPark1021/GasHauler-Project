package controller;

import model.DefaultCommandDB;

import java.util.ArrayList;

/**
 * Class: DefaultCommand
 * @author: Erika Attidzah
 * @version: 1.0
 * Course: ITEC 3860 FALL 2024
 * Written: 4 Nov, 2024
 *
 * This class creates a DefaultObject Object
 */
public class DefaultCommand {
    private int defaultID;
    private String name;
    private String defaultAction;


    public DefaultCommand(int defaultID, String name, String defaultAction) {
        this.defaultID = defaultID;
        this.name = name;
        this.defaultAction = defaultAction;
    }

    public int getDefaultID() {
        return defaultID;
    }

    public String getName() {
        return name;
    }

    public String getDefaultAction() {
        return defaultAction;
    }


}
