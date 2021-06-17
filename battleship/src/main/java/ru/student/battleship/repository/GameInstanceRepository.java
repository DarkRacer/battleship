package ru.student.battleship.repository;

import ru.student.battleship.model.GameInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Репозиторий расширяющий MongoRepository для работы с коллекцией GameInstance
 * @author Максим Щербаков
 */
public interface GameInstanceRepository extends MongoRepository<GameInstance, String> {

    /**
     * Находит один GameInstance по идентификатору пользователя
     * @param userId User-Id игрока
     * @return GameInstance
     */
    public List<GameInstance> findGameInstanceByUserId(String userId);

    /**
     * Находит все записи GameInstance для таблицы очков
     * @return Список всех записей игровых экземпляров
     */
    public List<GameInstance> findAll();

    /**
     * Находит, существует ли имя пользователя в репозитории
     * @param userName username игрока
     * @return Логическое значение существует ли пользователь
     */
    public boolean existsByUserName(String userName);

    /**
     * Находит одну запись по имени пользователя игрока
     * @param username username игрока
     * @return Игровой экземпляр пользователя
     */
    public List<GameInstance> findGameInstanceByUserName(String username);
}
