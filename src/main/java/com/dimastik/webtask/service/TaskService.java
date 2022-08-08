package com.dimastik.webtask.service;

import com.dimastik.webtask.model.Task;
import com.dimastik.webtask.repository.TaskRepository;
import com.dimastik.webtask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

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

        //Если репозиторий возращает не пустой список, то этот список из optional преобразутся в Array List и сортируется по id
        if (!optionals.isEmpty()) {
            ArrayList<Task> list = optionals.stream().map(task -> task.get()).collect(Collectors.toCollection(ArrayList::new));
            Collections.sort(list, new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });
            //Возращаем список задач, где статус равен ACTIVE
            return list.stream().filter(task -> task.getStatus() == Task.Status.ACTIVE).toList();
        }

        return new ArrayList<>();
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
            task.setStatus(Task.Status.ACTIVE);
            taskRepository.save(task);
        }
    }

    private Long getUserNameId(String userName) {
        return userRepository.findByUsername(userName).get().getId();
    }

    public void deleteTask(Long id, String name) {
        if (getAllTasksByUserNameId(name)
                .stream().map(task -> task.getId()).toList()
                .contains(id)) {
            Task task = taskRepository.findById(id).get();
            task.setStatus(Task.Status.DELETE);
            taskRepository.save(task);
        }
    }
}
















