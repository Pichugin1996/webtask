package com.dimastik.webtask.service;

import com.dimastik.webtask.model.Task;
import com.dimastik.webtask.model.User;
import com.dimastik.webtask.repository.TaskRepository;
import com.dimastik.webtask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
        List<Optional<Task>> optionals = taskRepository.findByUserNameId(getUserNameId(userName));
        List<Task> list = optionals.stream().map(task -> task.get()).toList();
        return list;
    }


    public Task getTaskById(Long id, String username) {
        Task task = taskRepository.findById(id).orElse(new Task());
        if (task.getUserNameId() != getUserNameId(username)) {
            return new Task();
        }
        return task;
    }

    public void saveTask(Task task, Principal principal) {
        Long userNameId = getUserNameId(principal.getName());
        //Содержится ли в списке пользователя задача с текущим id если да, то сохраняем
        if (getAllTasksByUserNameId(principal.getName())
                .stream().map(task1 -> task1.getId()).toList()
                .contains(task.getId())) {
            task.setUserNameId(userNameId);
            taskRepository.save(task);
        } else {
            task.setId(null);
            task.setUserNameId(userNameId);
            taskRepository.save(task);
        }
    }

    private Long getUserNameId(String userName) {
        return userRepository.findByUsername(userName).get().getId();
    }
}
