package com.example.mdswebsite.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity @Table(name="todos")
public class Todo {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long ownerUserId;
    private Long teamId;                 // remove if not used
    private String title;
    @Column(length=2000) private String description;
    private String status;
    private String priority;
    private OffsetDateTime dueAt;
    @ElementCollection
    @CollectionTable(name="todo_tags", joinColumns=@JoinColumn(name="todo_id"))
    @Column(name="tag")
    private List<String> tags;
    @Version private Long version;
    private Integer orderIndex;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Todo() {}
    public Long getId(){ return id; }
    public Long getOwnerUserId(){ return ownerUserId; }
    public Long getTeamId(){ return teamId; }
    public String getTitle(){ return title; }
    public String getDescription(){ return description; }
    public String getStatus(){ return status; }
    public String getPriority(){ return priority; }
    public OffsetDateTime getDueAt(){ return dueAt; }
    public List<String> getTags(){ return tags; }
    public Integer getOrderIndex(){ return orderIndex; }

    public void setOwnerUserId(Long v){ ownerUserId=v; }
    public void setTeamId(Long v){ teamId=v; }
    public void setTitle(String v){ title=v; }
    public void setDescription(String v){ description=v; }
    public void setStatus(String v){ status=v; }
    public void setPriority(String v){ priority=v; }
    public void setDueAt(OffsetDateTime v){ dueAt=v; }
    public void setTags(List<String> v){ tags=v; }
    public void setOrderIndex(Integer v){ orderIndex=v; }
}
