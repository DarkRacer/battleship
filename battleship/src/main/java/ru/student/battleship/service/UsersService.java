package ru.student.battleship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.student.battleship.model.Users;
import ru.student.battleship.repository.UserRepository;

/**
 * Сервис для работы с пользователем
 * @author Максим Щербаков
 */
@Service
public class UsersService {

    /**
     * Экземпляр репозитория UserRepository
     */
    private final UserRepository userRepository;

    @Autowired
    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Метод для получения информации о пользователе
     * @param id идентификатор пользователя
     * @return информация о пользователе
     */
    public Users getInfo(String id) {
        return userRepository.findUsersById(id);
    }

    /**
     * Метод для сохранения и обновления информации о пользователе
     * @param users пользователь для сохранения или обновления
     */
    public void saveUser(Users users) {
        userRepository.save(users);
    }
}
