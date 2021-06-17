package ru.student.battleship.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.student.battleship.model.Users;

import java.util.List;

/**
 * Репозиторий расширяющий MongoRepository для работы с коллекцией Users
 * @author Максим Щербаков
 */
public interface UserRepository extends MongoRepository<Users, String> {
    /**
     * Метод для поиска пользователя по id на внешнем ресурсе
     * @param userId идентификатор пользователя на внешнем ресурсе
     * @return Пользователь
     */
    List<Users> findUsersByUserId(String userId);

    /**
     * Метод для поиска пользователя по id
     * @param id идентификатор пользователя
     * @return Пользователь
     */
    Users findUsersById(String id);
}
