package ru.student.battleship.repository;

import ru.student.battleship.model.PlayerMatches;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Репозиторий расширяющий MongoRepository для работы с коллекцией PlayerMatches.
 * @author Максим Щербаков
 */
public interface PlayerMatchesRepository extends MongoRepository<PlayerMatches, String> {

    /**
     * Находит одну запись совпадений игроков по user id игрока 1.
     * @param player1 user id игрока 1
     * @return Экземпляр playerMatches, содержащий игрока 1.
     */
    public List<PlayerMatches> findPlayerMatchesByPlayer1(String player1);

    /**
     * Находит одну запись совпадений игрока по адресу сокета, который является уникальным идентификатором для этой модели.
     * @param webSocketAddress Первичный ключ, или socket id.
     * @return Экземпляр PlayerMatches, содержащий socket id.
     */
    public PlayerMatches findOneByWebSocketAddress(String webSocketAddress);
}
