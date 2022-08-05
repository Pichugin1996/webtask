package com.dimastik.webtask.repository;

import com.dimastik.webtask.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Optional<Task>> findByUserNameId(Long userNameId);
}
