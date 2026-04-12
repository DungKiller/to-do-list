package com.dunght.mvc.todolist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "workspace_members",
            joinColumns = @JoinColumn(name = "workspace_id"), // Ép dùng tên này, không có chữ 's'
            inverseJoinColumns = @JoinColumn(name = "user_id") // Cột nối sang bảng User
    )
    private List<User> members = new ArrayList<>(); // Danh sách thành viên trong nhóm

    @OneToMany(mappedBy = "workspace")
    private List<Task> tasks; // Danh sách việc trong nhóm
}
