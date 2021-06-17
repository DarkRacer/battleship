package ru.student.battleship.controller;

import com.google.gson.Gson;
import ru.student.battleship.logics.ModelConversion;

import ru.student.battleship.model.PlayerMatches;
import ru.student.battleship.model.Users;
import ru.student.battleship.repository.GameInstanceRepository;
import ru.student.battleship.repository.PlayerMatchesRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.student.battleship.model.GameInstance;
import ru.student.battleship.model.GameModel;
import ru.student.battleship.repository.UserRepository;

import java.util.*;

/**
 * Класс, который обрабатывает все вызовы при игре RESTful API от клиента Angular.
 * @author Максим Щербаков
 */
@RestController
@RequestMapping("/api/v1/battle")
public class BattleController {

    /**
     * Используется Gson для работы с объектами JSON
     */
    private final Gson gson;

    /**
     * Экземпляр репозитория модели GameInstance
     */
    private final GameInstanceRepository gameInstanceRepository;

    /**
     * Экземпляр репозитория модели Users
     */
    private final UserRepository userRepository;
    /**
     * Экземпляр репозитория модели PlayerMatches
     */
    private final PlayerMatchesRepository playerMatchesRepository;

    @Autowired
    public BattleController(Gson gson, GameInstanceRepository gameInstanceRepository, UserRepository userRepository, PlayerMatchesRepository playerMatchesRepository) {
        this.gson = gson;
        this.gameInstanceRepository = gameInstanceRepository;
        this.userRepository = userRepository;
        this.playerMatchesRepository = playerMatchesRepository;
    }

    /**
     * Эта конечная точка API используется компонентом Score-Table для отображения результатов.
     * @return Сведения обо всех пользователях в базе данных, выигравших хотя бы одну игру.
     */
    @RequestMapping(value = "/getAll")
    public List<Users> getAllItems() {
        List<Users> users = userRepository.findAll();
        users.sort(Comparator.comparing(Users::getLost));
        return users;
    }

    /**
     * Эта конечная точка API используется компонентом Battleboard1 из Angular.
     * Сначала он создает случайный UUID и назначает его в качестве уникального идентификатора пользователя игрока.
     * Кроме того, класс GameInstance создает экземпляр одного из своих экземпляров, который содержит доску со случайно распределенными кораблями.
     * Затем эта игровая модель возвращается клиенту.
     * @param userId Id первого игрока
     * @return игровая модель содержащая данные игрока 1
     */
    @RequestMapping(value ="/newgame/{userId}")
    public GameModel initPlayer1 (@PathVariable String userId) {
        GameInstance gameInstance;
        Users users = userRepository.findUsersByUserId(userId).get(0);
        GameModel gameModel = new GameModel(users.getUserId(), users.getUserName());
        if (gameInstanceRepository.existsByUserName(users.getUserName())){
            List<GameInstance> gameInstances = gameInstanceRepository.findGameInstanceByUserName(users.getUserName());
            gameInstance = gameInstances.get(gameInstances.size() - 1);
            gameInstance = new GameInstance(gameModel.getUserId(), gameInstance.getUserName(), gameInstance.getWonGames(), gameInstance.getLostGames());
        }
        else {
            gameInstance = new GameInstance(gameModel.getUserId(), users.getUserName());
        }
        gameModel = ModelConversion.convertGameInstance(gameModel, gameInstance);
        gameInstanceRepository.save(gameInstance);
        PlayerMatches playerMatches = new PlayerMatches(gameInstance.getUserId());
        playerMatchesRepository.save(playerMatches);
        List<PlayerMatches> playerMatches1 = playerMatchesRepository.findPlayerMatchesByPlayer1(gameInstance.getUserId());
        String socketUrl = playerMatches1.get(playerMatches1.size() - 1).getWebSocketAddress();
        gameModel.setSocketUrl(socketUrl);
        return gameModel;
    }

    /**
     * Эта конечная точка API используется компонентом Battleboard в Angular.
     * @param userId id пользователя для игрока
     * @return имя пользователя игрока
     */
    @SneakyThrows(NullPointerException.class)
    @RequestMapping(value = "/getUserName/{userId}")
    public String getUserName (@PathVariable String userId) {
        HashMap<String, String> userNameMap = new HashMap<>();
        System.out.println(userId);
        List<GameInstance> gameInstances = gameInstanceRepository.findGameInstanceByUserId(userId);
        userNameMap.put("userName", gameInstances.get(gameInstances.size() - 1).getUserName());
        return gson.toJson(userNameMap);

    }

    /**
     * Используется компонентом Battleboard.
     * @param sockId  Уникальный sockId используется для связи между двумя игроками через веб-сокеты на сервере.
     * @return id второго игрока
     */
    @RequestMapping(value = "/getPlayer2Id/{sockId}")
    public String getSecondPlayerName(@PathVariable String sockId) {
        PlayerMatches playerMatches = playerMatchesRepository.findOneByWebSocketAddress(sockId);
        System.out.println(playerMatches);
        System.out.println(playerMatches.getPlayer2());
        HashMap<String,String> userId = new HashMap<>();
        userId.put("userId", playerMatches.getPlayer2());
        return gson.toJson(userId);
    }
}
