package com.atlassian.tutorial.ao.todo.upgrade.v2;

import net.java.ao.Entity;
import net.java.ao.Preload;

@Preload
public interface Todo extends Entity
{
    void setUserName(String userName);

    String getUserName();

    void setUser(User user);

    User getUser();

    String getDescription();

    void setDescription(String description);

    boolean isComplete();

    void setComplete(boolean complete);
}
