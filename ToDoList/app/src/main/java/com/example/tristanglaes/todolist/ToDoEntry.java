package com.example.tristanglaes.todolist;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by Tristan Glaes on 03.12.2017.
 */

public class ToDoEntry implements Comparator<ToDoEntry> {

    private String title;
    private Priority priority;
    private String description;

    public ToDoEntry(String title, Priority priority, String description){

        this.title = title;
        this.priority = priority;
        this.description = description;
    }

    @Override
    public String toString(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public Priority getPriority(){
        return this.priority;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setPriority(Priority priority){
        this.priority = priority;
    }

    @Override
    public int compare(ToDoEntry t1, ToDoEntry t2) {

        if(t1.getPriority() == Priority.high && t2.getPriority() == Priority.high){
            return 0;
        } else if(t1.getPriority() == Priority.middle && t2.getPriority() == Priority.middle){
            return 0;
        }else if(t1.getPriority() == Priority.low && t2.getPriority() == Priority.low){
            return 0;
        }else if(t1.getPriority() == Priority.high && t2.getPriority() == Priority.middle){
            return -1;
        }else if(t1.getPriority() == Priority.high && t2.getPriority() == Priority.low){
            return -1;
        }else if(t1.getPriority() == Priority.middle && t2.getPriority() == Priority.high){
            return 1;
        }else if(t1.getPriority() == Priority.middle && t2.getPriority() == Priority.low){
            return -1;
        }else if(t1.getPriority() == Priority.low && t2.getPriority() == Priority.high){
            return 1;
        }else if(t1.getPriority() == Priority.low && t2.getPriority() == Priority.middle){
            return 1;
        }
        return 0;
    }
}


