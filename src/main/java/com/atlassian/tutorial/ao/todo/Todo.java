package com.atlassian.tutorial.ao.todo;

import net.java.ao.Entity;
import net.java.ao.Preload;

@Preload
public interface Todo extends Entity {
    String getDescription();

    void setDescription(String description);

    boolean isComplete();

    void setComplete(boolean complete);
}
