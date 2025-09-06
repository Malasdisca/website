package com.example.mdswebsite.service;

import com.example.mdswebsite.dto.TodoDTO;

import java.util.List;

public interface TodoService {
    List<TodoDTO.Response> list(Long userId, String status, String tag, Long teamId, int limit, Long afterId);
    TodoDTO.Response create(Long userId, TodoDTO.Create req);
    TodoDTO.Response update(Long userId, Long id, TodoDTO.Update req);
    void delete(Long userId, Long id);
    List<TodoDTO.Response> reorder(Long userId, List<Long> ids, int orderIndexStart);
}
