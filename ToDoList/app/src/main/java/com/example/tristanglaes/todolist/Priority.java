package com.example.tristanglaes.todolist;

/**
 * Created by Tristan Glaes on 03.12.2017.
 */

public enum Priority {
    high,
    middle,
    low;

    public static Priority makeEnumFromString(String prio){

        Priority p = low;

        switch (prio){
            case "High": p = high;
            break;
            case "Middle": p = middle;
            break;
            case "Low": p = low;
            break;
        }

        return p;
    }
}
