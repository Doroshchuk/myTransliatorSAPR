package com.company;

/**
 * Created by Dasha on 23.04.2017.
 */
public class PriorityTable {
    private String name;
    private int priority;

    public PriorityTable(String name, int priority)
    {
        this.name = name;
        this.priority = priority;
    }

    public String getName() { return name; }
    public int getPriority() { return priority; }
}
