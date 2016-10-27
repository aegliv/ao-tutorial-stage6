package com.atlassian.tutorial.ao.todo;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class TodoServiceImpl implements TodoService {
    private final ActiveObjects ao;

    public TodoServiceImpl(@ComponentImport ActiveObjects ao) {
        this.ao = ao;
    }

    public Todo add(String description) {
        final Todo todo = ao.create(Todo.class);
        todo.setDescription(description);
        todo.setComplete(false);
        todo.save();
        return todo;
    }

    public List<Todo> all() {
        Todo[] elements = ao.find(Todo.class);
        if (elements == null || elements.length == 0) return Collections.emptyList();
        return Arrays.asList(elements);
    }
}