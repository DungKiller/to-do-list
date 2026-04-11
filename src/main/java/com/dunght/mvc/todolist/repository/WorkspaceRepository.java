package com.dunght.mvc.todolist.repository;

import com.dunght.mvc.todolist.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Integer> {
}
