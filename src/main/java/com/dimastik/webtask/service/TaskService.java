package com.dimastik.webtask.service;

import com.dimastik.webtask.model.Task;
import com.dimastik.webtask.repository.TaskRepository;
import com.dimastik.webtask.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllTasksByUserNameId(String userName) {
        List<Optional<Task>> optionals = taskRepository.findByUserNameId(userRepository.findByUsername(userName).get().getId());
        List<Task> list = optionals.stream().map(task -> task.get()).toList();
        return list;
    }

}
