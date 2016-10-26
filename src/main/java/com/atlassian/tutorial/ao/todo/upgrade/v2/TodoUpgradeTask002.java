package com.atlassian.tutorial.ao.todo.upgrade.v2;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.external.ActiveObjectsUpgradeTask;
import com.atlassian.activeobjects.external.ModelVersion;
import com.google.common.collect.ImmutableMap;
import net.java.ao.Query;

public final class TodoUpgradeTask002 implements ActiveObjectsUpgradeTask
{
    @Override
    public ModelVersion getModelVersion()
    {
        return ModelVersion.valueOf("2");
    }

    @Override
    public void upgrade(ModelVersion currentVersion, ActiveObjects ao)
    {
        ao.migrate(Todo.class, User.class);

        Todo[] todos = ao.find(Todo.class);
        for (Todo todo : todos)
        {
            todo.setUser(getOrCreateUser(ao, todo.getUserName()));
            todo.save();
        }
    }

    private User getOrCreateUser(ActiveObjects ao, String userName)
    {
        User[] users = ao.find(User.class, Query.select().where("NAME = ?", userName));
        if (users.length == 0)
        {
            return createUser(ao, userName);
        }
        else if (users.length == 1)
        {
            return users[0];
        }
        else
        {
            throw new IllegalStateException("There shouldn't be 2 users with the same username! " + userName);
        }
    }

    private User createUser(ActiveObjects ao, String userName)
    {
        return ao.create(User.class, ImmutableMap.<String, Object>of("NAME", userName));
    }
}
