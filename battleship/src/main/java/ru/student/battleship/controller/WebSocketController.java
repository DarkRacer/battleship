package ru.student.battleship.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import org.apache.catalina.User;
import ru.student.battleship.logics.ModelConversion;
import ru.student.battleship.model.GameInstance;
import ru.student.battleship.model.GameModel;
import ru.student.battleship.model.PlayerMatches;
import ru.student.battleship.model.Users;
import ru.student.battleship.repository.GameInstanceRepository;
import ru.student.battleship.repository.PlayerMatchesRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.student.battleship.repository.UserRepository;

/**
 * Класс взаимодействующий с WebSocket контроллерами.
 * @author Максим Щербаков
 */
@Controller
@RestController
public class WebSocketController {

    /**
     * Поле gson обьекта для работы с JSON обьектами
     */
    private final Gson gson;

    /**
     * репозиторий GameInstance
     */
    private final GameInstanceRepository gameInstanceRepository;

    private final UserRepository userRepository;

    /**
     * репозиторий PlayerMatches
     */
    private final PlayerMatchesRepository playerMatchesRepository;
    
    /**
     * обьект для отправки сообщений об ошибках
     */
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessageSendingOperations messagingTemplate,
                               Gson gson,
                               GameInstanceRepository gameInstanceRepository,
                               UserRepository userRepository, PlayerMatchesRepository playerMatchesRepository) {
        this.messagingTemplate = messagingTemplate;
        this.gson = gson;
        this.gameInstanceRepository = gameInstanceRepository;
        this.userRepository = userRepository;
        this.playerMatchesRepository = playerMatchesRepository;
    }

    /**
     * Эта конечная точка API инициализирует проигрыватель 2. Она вызывается, когда URL-адрес, созданный после Player 1
     * инициализировано, вызывается Player 2 в другом браузере.
     * @param userId Id пользователя Игрока 2 после того, как игрок введет его в форме приветствия.
     * @param socketUrl Socket id PlayerMatches.
     * @return обьект GameModel содержащий сведения об игроке 2,
     * включая позиции корабля.
     */
    @RequestMapping(value ="/playWithFriend/{userId}/{socketUrl}")
    @SneakyThrows(Exception.class)
    public GameModel initPlayer2 (@PathVariable String userId, @PathVariable String socketUrl) {
        GameInstance gameInstance;
        Users users = userRepository.findUsersById(userId);
        GameModel gameModel = new GameModel(users.getUserId(), users.getUserName());

        if (gameInstanceRepository.existsByUserName(users.getUserName())) {
            List<GameInstance> gameInstances = gameInstanceRepository.findGameInstanceByUserName(users.getUserName());
            gameInstance = gameInstances.get(gameInstances.size() - 1);
            gameInstance = new GameInstance(gameModel.getUserId(), gameInstance.getUserName(), gameInstance.getWonGames(), gameInstance.getLostGames());
        } else {
            gameInstance = new GameInstance(gameModel.getUserId(), users.getUserName());
        }
        gameModel = ModelConversion.convertGameInstance(gameModel, gameInstance);
        gameInstanceRepository.save(gameInstance);

        PlayerMatches playerMatches = playerMatchesRepository.findOneByWebSocketAddress(socketUrl);
        playerMatches.setPlayer2(gameModel.getUserId());
        playerMatchesRepository.save(playerMatches);
        return gameModel;
    }

    /**
     * Это основная конечная точка, которая обрабатывает все соединения WebSocket.
     * Он получает Socket id из URL-адреса и сообщение из Payload WebSocket.
     * Если тело сообщения содержит start, это означает, что игрок 2 инициализировал его. это доска и готова к игре.
     * @param message Получено сообщение от клиента о ходе игры.
     * @param socketId Уникальный идентификатор из  PlayerMatches который используется как Socket-Id
     * для подключения к WebSockets.
     * @return Ответ на входящее сообщение. Зависит от типа сообщения.
     * @throws Exception Если есть какие-то исключения, которые еще не обработаны.
     */
    @SneakyThrows(NullPointerException.class)
    @MessageMapping("/message/{id}")
    @SendTo("/topic/reply/{id}")
    public String processMessageFromClient(@Payload String message, @DestinationVariable("id") String socketId) throws Exception {
        String messageBody = gson.fromJson(message, Map.class).get("name").toString();
        String textPart = "", numberPart = "";
        String playerId = "";
        boolean isPlayerOne = false;
        if (!(messageBody.equals("start"))) {
            textPart = messageBody.replaceAll("\\d","");
            numberPart = messageBody.replace(textPart, "");
            HashMap<String, String> tempHashMap = new HashMap<>();
            if (textPart.equals("potheir")) {
                playerId = playerMatchesRepository.findOneByWebSocketAddress(socketId).getPlayer2();
                tempHashMap.put("turnBy", "p1");
                isPlayerOne = true;
            }
            else if (textPart.equals("pttheir")) {
                playerId = playerMatchesRepository.findOneByWebSocketAddress(socketId).getPlayer1();
                tempHashMap.put("turnBy", "p2");
                isPlayerOne = false;
            }
            List<GameInstance> gameInstances = gameInstanceRepository.findGameInstanceByUserId(playerId);
            GameInstance gameInstance = gameInstances.get(gameInstances.size() - 1);
            boolean isContainsShip = gameInstance.enemyTurn(Integer.parseInt(numberPart));
            boolean winningMove = gameInstance.getAttackedShips() >= 20;
            tempHashMap.put("attackedAt", numberPart);
            tempHashMap.put("isContainsShip", String.valueOf(isContainsShip));
            tempHashMap.put("winningMove", String.valueOf(winningMove));
            gameInstanceRepository.save(gameInstance);

            String wonPlayerId, lostPlayerId;
            if (gameInstance.getAttackedShips() >= 20){
                if (isPlayerOne) {
                    wonPlayerId = playerMatchesRepository.findOneByWebSocketAddress(socketId).getPlayer1();
                    lostPlayerId = playerMatchesRepository.findOneByWebSocketAddress(socketId).getPlayer2();
                } else  {
                    wonPlayerId = playerMatchesRepository.findOneByWebSocketAddress(socketId).getPlayer2();
                    lostPlayerId = playerMatchesRepository.findOneByWebSocketAddress(socketId).getPlayer1();
                }

                List<GameInstance> wonGameInstances = gameInstanceRepository.findGameInstanceByUserId(wonPlayerId);
                GameInstance wonGameInstance = wonGameInstances.get(wonGameInstances.size() - 1);
                List<GameInstance> lostGameInstances = gameInstanceRepository.findGameInstanceByUserId(lostPlayerId);
                GameInstance lostGameInstance= lostGameInstances.get(lostGameInstances.size() - 1);

                Users wonUsers = userRepository.findUsersByUserId(wonPlayerId).get(0);
                Users lostUsers = userRepository.findUsersByUserId(lostPlayerId).get(0);


                wonUsers.setWin(wonUsers.getWin() + 1);
                lostUsers.setLost(lostUsers.getLost() + 1);

                wonGameInstance.setWonGames(wonGameInstance.getWonGames() + 1);
                lostGameInstance.setLostGames(lostGameInstance.getLostGames() +1);

                userRepository.save(wonUsers);
                userRepository.save(lostUsers);
                gameInstanceRepository.save(wonGameInstance);
                gameInstanceRepository.save(lostGameInstance);
            }

            return gson.toJson(tempHashMap);
        }
        return new Gson().fromJson(message, Map.class).get("name").toString();
    }

    /**
     * Эта конечная точка отправляет сообщение об ошибке в случае возникновения какой-либо ошибки или исключения.
     * @param exception сообщение ошибки
     * @return сообщение
     */
    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        messagingTemplate.convertAndSend("/errors", exception.getMessage());
        return exception.getMessage();
    }

}
