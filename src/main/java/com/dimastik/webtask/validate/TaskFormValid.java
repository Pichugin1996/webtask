package com.dimastik.webtask.validate;

import com.dimastik.webtask.model.Task;
import com.dimastik.webtask.model.User;
import com.dimastik.webtask.service.TaskService;
import com.dimastik.webtask.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class TaskFormValid {

    public void validForm(Task task, BindingResult result) {

        //Удаляем пробелы
        String title = task.getTitle().replaceAll("\\s+", "");
        String description = task.getDescription().replaceAll("\\s+", "");

        if (title.length() < 1) {
            result.rejectValue("title", "error.title",
                    "Введите название!");
        }

        if (description.length() < 1) {
            result.rejectValue("description", "error.description",
                    "Введите описание!");
        }

    }
}
