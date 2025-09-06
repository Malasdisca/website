package com.example.mdswebsite.controller;

import com.example.mdswebsite.dto.TodoDTO;
import com.example.mdswebsite.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mds/todos")
@RequiredArgsConstructor
public class TodoController {
    private TodoService todoService;

    @GetMapping
    public ResponseEntity<List<TodoDTO.Response>> list(
            @AuthenticationPrincipal com.example.mdswebsite.entity.User user,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Long teamId,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false, name = "cursor") Long afterId) {
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(todoService.list(user.getId(), status, tag, teamId, limit, afterId));
    }

    @PostMapping
    public ResponseEntity<TodoDTO.Response> create(
            @AuthenticationPrincipal com.example.mdswebsite.entity.User user,
            @Valid @RequestBody TodoDTO.Create req) {
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(todoService.create(user.getId(), req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TodoDTO.Response> update(
            @AuthenticationPrincipal com.example.mdswebsite.entity.User user,
            @PathVariable Long id,
            @Valid @RequestBody TodoDTO.Update req) {
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(todoService.update(user.getId(), id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal com.example.mdswebsite.entity.User user,
            @PathVariable Long id) {
        if (user == null) return ResponseEntity.status(401).build();
        todoService.delete(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reorder")
    public ResponseEntity<List<TodoDTO.Response>> reorder(
            @AuthenticationPrincipal com.example.mdswebsite.entity.User user,
            @Valid @RequestBody TodoDTO.Reorder req) {
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(todoService.reorder(user.getId(), req.ids(), req.orderIndexStart()));
    }
}
