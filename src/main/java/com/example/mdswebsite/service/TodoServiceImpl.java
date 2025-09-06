package com.example.mdswebsite.service;

import com.example.mdswebsite.dto.TodoDTO;
import com.example.mdswebsite.entity.Todo;
import com.example.mdswebsite.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@Transactional
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todos;

    public TodoServiceImpl(TodoRepository todos) {
        this.todos = todos;
    }

    @Override
    public List<TodoDTO.Response> list(Long userId, String status, String tag, Long teamId, int limit, Long afterId) {
        var rs = todos.search(userId, teamId, status, tag, afterId, (Pageable) PageRequest.of(0, Math.max(1, limit)));
        return rs.stream().map(this::toResp).toList();
    }


    @Override @Transactional
    public TodoDTO.Response create(Long userId, TodoDTO.Create req) {
        Todo t = new Todo();
        t.setOwnerUserId(userId);
        t.setTeamId(req.teamId());
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setStatus(req.status() == null ? "open" : req.status());
        t.setPriority(req.priority() == null ? "M" : req.priority());
        t.setDueAt(req.dueAt());
        t.setTags(req.tags());
        t.setOrderIndex((int) todos.countByOwnerUserId(userId)); // simple order
        return toResp(todos.save(t));
    }

    @Override @Transactional
    public TodoDTO.Response update(Long userId, Long id, TodoDTO.Update req) {
        var t = todos.findByIdAndOwnerUserId(id, userId).orElseThrow();
        if (req.title() != null) t.setTitle(req.title());
        if (req.description() != null) t.setDescription(req.description());
        if (req.status() != null) t.setStatus(req.status());
        if (req.priority() != null) t.setPriority(req.priority());
        if (req.dueAt() != null) t.setDueAt(req.dueAt());
        if (req.tags() != null) t.setTags(req.tags());
        return toResp(todos.save(t));
    }

    @Override @Transactional
    public void delete(Long userId, Long id) {
        var t = todos.findByIdAndOwnerUserId(id, userId).orElseThrow();
        todos.delete(t);
    }

    @Override @Transactional
    public List<TodoDTO.Response> reorder(Long userId, List<Long> ids, int start) {
        var list = todos.findAllById(ids);
        // ownership check
        if (list.stream().anyMatch(t -> !Objects.equals(t.getOwnerUserId(), userId))) {
            throw new AccessDeniedException("forbidden");
        }
        int idx = start;
        var byId = list.stream().collect(Collectors.toMap(Todo::getId, Function.identity()));
        for (Long id : ids) { byId.get(id).setOrderIndex(idx++); }
        return todos.saveAll(list).stream().sorted(Comparator.comparingInt(Todo::getOrderIndex)).map(this::toResp).toList();
    }

    private TodoDTO.Response toResp(Todo t) {
        return new TodoDTO.Response(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(),
                t.getPriority(), t.getDueAt(), t.getTeamId(), t.getTags(), Optional.ofNullable(t.getOrderIndex()).orElse(0));
    }
}
