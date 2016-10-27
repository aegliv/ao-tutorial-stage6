package com.atlassian.tutorial.ao.todo;

import com.atlassian.activeobjects.tx.Transactional;

import java.util.List;

@Transactional
public interface TodoService {
    Todo add(String description);

    List<Todo> all();
}
