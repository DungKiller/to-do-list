package com.dunght.mvc.todolist.repository;

import com.dunght.mvc.todolist.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Integer> {
    boolean existsByName(String name);
    List<Workspace> findAllByMembers_UserId(Integer userId);
}
