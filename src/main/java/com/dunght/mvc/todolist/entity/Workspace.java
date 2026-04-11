package com.dunght.mvc.todolist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "workspaces")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id")
    private Integer workspaceId;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "workspace_members")
    private List<User> members; // Danh sách thành viên trong nhóm

    @OneToMany(mappedBy = "workspace")
    private List<Task> tasks; // Danh sách việc trong nhóm
}
