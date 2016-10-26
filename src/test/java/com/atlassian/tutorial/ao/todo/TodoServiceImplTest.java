package com.atlassian.tutorial.ao.todo;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.sal.api.user.UserManager;
import com.google.common.collect.ImmutableMap;
import net.java.ao.EntityManager;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.DatabaseUpdater;
import net.java.ao.test.jdbc.DynamicJdbcConfiguration;
import net.java.ao.test.jdbc.Jdbc;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(ActiveObjectsJUnitRunner.class)
@Jdbc(DynamicJdbcConfiguration.class)
@Data(TodoServiceImplTest.TodoServiceImplTestDatabaseUpdater.class)
public class TodoServiceImplTest
{
    private static final String TODO_DESC = "This is a todo";
    private static final String USER_NAME_OTHER = "other";

    private EntityManager entityManager;

    private ActiveObjects ao;

    private UserManager userManager;
    private static User userAdmin;

    private TodoServiceImpl todoService;

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        userManager = mock(UserManager.class);
        when(userManager.getRemoteUsername()).thenReturn(userAdmin.getName());
        todoService = new TodoServiceImpl(ao, userManager);
    }

    @Test
    public void testAdd() throws Exception
    {
        final String description = TODO_DESC + "#1";

        assertEquals(2, ao.find(Todo.class).length);

        final Todo add = todoService.add(description);
        assertFalse(add.getID() == 0);

        ao.flushAll(); // clear all caches

        final Todo[] todos = ao.find(Todo.class);
        assertEquals(3, todos.length);
        final Todo addedTodo = todos[2];
        assertEquals(add.getID(), addedTodo.getID());
        assertEquals(description, addedTodo.getDescription());
        assertEquals(false, addedTodo.isComplete());
        assertEquals(userAdmin.getID(), addedTodo.getUser().getID());
    }

    @Test
    public void testAll() throws Exception
    {
        assertEquals(1, todoService.all().size());

        final Todo todo = ao.create(Todo.class, ImmutableMap.<String, Object>of("USER_ID", userAdmin));
        todo.setDescription("Some todo");
        todo.save();

        ao.flushAll(); // clear all caches

        final List<Todo> all = todoService.all();
        assertEquals(2, all.size());
        assertEquals(todo.getID(), all.get(1).getID());
    }

    public static final class TodoServiceImplTestDatabaseUpdater implements DatabaseUpdater
    {
        @Override
        public void update(EntityManager em) throws Exception
        {
            em.migrate(Todo.class);

            userAdmin = em.create(User.class, ImmutableMap.<String, Object>of("NAME", "admin"));

            final Todo todo = em.create(Todo.class, ImmutableMap.<String, Object>of("USER_ID", userAdmin));
            todo.setDescription(TODO_DESC);
            todo.save();

            final Todo todo1 = em.create(Todo.class, ImmutableMap.<String, Object>of("USER_ID", em.create(User.class, ImmutableMap.<String, Object>of("NAME", USER_NAME_OTHER))));
            todo1.setDescription("Some other description");
            todo1.save();
        }
    }
}
