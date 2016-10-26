package com.atlassian.tutorial.ao.todo;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.user.UserManager;
import com.google.common.collect.ImmutableMap;
import net.java.ao.Query;

import java.util.List;

import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Lists.*;

public final class TodoServiceImpl implements TodoService
{
    private final ActiveObjects ao;
    private final UserManager userManager;

    public TodoServiceImpl(ActiveObjects ao, UserManager userManager)
    {
        this.ao = checkNotNull(ao);
        this.userManager = checkNotNull(userManager);
    }

    @Override
    public Todo add(String description)
    {
        final Todo todo = ao.create(Todo.class, ImmutableMap.<String, Object>of("USER_ID", currentUser()));
        todo.setDescription(description);
        todo.setComplete(false);
        todo.save();
        return todo;
    }

    @Override
    public List<Todo> all()
    {
        return newArrayList(ao.find(Todo.class, Query.select().where("USER_ID = ?", currentUser())));
    }

    private User currentUser()
    {
        return getOrCreateUser(userManager.getRemoteUsername());
    }

    private User getOrCreateUser(String userName)
    {
        User[] users = ao.find(User.class, Query.select().where("NAME = ?", userName));
        if (users.length == 0)
        {
            return createUser(userName);
        }
        else if (users.length == 1)
        {
            return users[0];
        }
        else
        {
            throw new IllegalStateException("Found multiple users for username: " + userName);
        }
    }

    private User createUser(String userName)
    {
        return ao.create(User.class, ImmutableMap.<String, Object>of("NAME", userName));
    }
}
