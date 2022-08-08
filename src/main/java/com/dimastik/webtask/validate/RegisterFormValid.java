package com.dimastik.webtask.validate;

import com.dimastik.webtask.model.User;
import com.dimastik.webtask.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class RegisterFormValid {

    public void validForm(User form, BindingResult result, UserService userService) {

        boolean error = false;
        //Удаление пробелов
        form.setUsername(form.getUsername().replaceAll("\\s+", ""));
        form.setPassword(form.getPassword().replaceAll("\\s+", ""));

        //Проверка паролей
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
        Matcher m = pattern.matcher(form.getPassword());

        if (!m.matches()) {
            result.rejectValue("passwordConfirm", "error.password",
                    "Пароль должен состоять из латинских букв в разных регистрах, и иметь как минимум 1 цифру");
            error = true;
        }

        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm",
                    "error.passwordConfirm", "Пароли не совпадают!.");
            error = true;
        }
        if (form.getPassword().length() >= 21 || form.getPassword().length() <= 4 || form.getPassword() == "") {
            result.rejectValue("password", "error.password",
                    "Пароль должен содержать от 5 до 20 символов.");
            error = true;
        }

        //Проверка пользователя
        if (form.getUsername().length() >= 21 || form.getUsername().length() <= 4) {
            result.rejectValue("username", "error.username",
                    "Логин должен содержать от 5 до 20 символов.");
            error = true;
        }

        if (!error) {
            User userIsExist = userService.findByUsername(form.getUsername());
            if (userIsExist != null) {
                result.rejectValue("username", "error.username",
                        "Пользователь с таким именем уже существует.");
            }
        }
    }
}
