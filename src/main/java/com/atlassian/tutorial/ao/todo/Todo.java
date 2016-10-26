package com.atlassian.tutorial.ao.todo;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.NotNull;

@Preload
public interface Todo extends Entity
{
    @NotNull
    void setUser(User user);

    @NotNull
    User getUser();

    String getDescription();

    void setDescription(String description);

    boolean isComplete();

    void setComplete(boolean complete);
}
