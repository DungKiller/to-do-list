package com.dunght.mvc.todolist.repository;

import com.dunght.mvc.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByWorkspace_WorkspaceId(Integer workspaceId);

    @Query("SELECT t FROM Task t JOIN FETCH t.user WHERE t.workspace.workspaceId = :workspaceId")
    List<Task> findAllTasksWithUserByWorkspaceId(@Param("workspaceId") Integer workspaceId);

    void deleteByWorkspace_WorkspaceId(Integer workspaceId);
}
