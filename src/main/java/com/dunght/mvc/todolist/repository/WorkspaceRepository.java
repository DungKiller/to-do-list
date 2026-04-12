package com.dunght.mvc.todolist.repository;

import com.dunght.mvc.todolist.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Integer> {
    boolean existsByName(String name);
    List<Workspace> findAllByMembers_UserId(Integer userId);

    @Query("SELECT w FROM Workspace w WHERE w.owner.userId = :userId OR :userId IN (SELECT m.userId FROM w.members m)")
    List<Workspace> findByUserId(@Param("userId") Integer userId);
}
