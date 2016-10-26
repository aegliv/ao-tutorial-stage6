package com.atlassian.tutorial.ao.todo.upgrade.v1;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.external.ActiveObjectsUpgradeTask;
import com.atlassian.activeobjects.external.ModelVersion;

public final class TodoUpgradeTask001 implements ActiveObjectsUpgradeTask
{
    @Override
    public ModelVersion getModelVersion()
    {
        return ModelVersion.valueOf("1");
    }

    @Override
    public void upgrade(ModelVersion currentVersion, ActiveObjects ao)
    {
        ao.migrate(Todo.class);

        for (Todo todo : ao.find(Todo.class))
        {
            todo.setUserName("admin");
            todo.save();
        }
    }
}
