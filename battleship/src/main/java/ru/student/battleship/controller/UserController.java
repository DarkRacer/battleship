package ru.student.battleship.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.student.battleship.model.Users;
import ru.student.battleship.service.UsersService;

/**
 * Класс, который обрабатывает все вызовы для работы с клиентом RESTful API от клиента Angular.
 * @author Максим Щербаков
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * Экземпляр сервиса UsersService
     */
    private final UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * Эта конечная точка API для получения информации о пользователе по id
     * @param id id пользователя
     * @return информацию о пользователе
     */
    @GetMapping("/{id}")
    public Users getInfo (@PathVariable("id") String id) {
        return usersService.getInfo(id);
    }

    /**
     * Эта конечная точка API для сохранения информации о пользователе
     * @param users экземпляр пользователя для сохранения информации
     */
    @PostMapping("/save")
    public void saveUser (@RequestBody Users users) {
        usersService.saveUser(users);
    }
}
