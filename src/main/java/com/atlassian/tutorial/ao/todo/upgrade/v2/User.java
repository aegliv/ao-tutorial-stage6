package com.atlassian.tutorial.ao.todo.upgrade.v2;

import net.java.ao.Entity;

public interface User extends Entity
{
    String getName();
    void setName(String name);
}
