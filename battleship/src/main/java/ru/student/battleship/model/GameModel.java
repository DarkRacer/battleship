package ru.student.battleship.model;

import lombok.Data;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * POJO - класс, представляющий модель GameInstance в более простом формате, который можно отправить клиенту.
 * Используется для рисования игровых полей на стороне клиента.
 * @author Максим Щербаков
 */
@Data
public class GameModel {

    /**
     * Идентификатор пользователя, созданный контроллером.
     */
    String userId;

    /**
     * Имя пользователя игрока.
     */
    @Id String userName;

    /**
     * Уникальный идентификатор, используемый для связи с сервером через веб-сокеты.
     */
    String socketUrl;

    /**
     * Координаты выстрела противник.
     */
    List<Integer> attackedByEnemyCoordinates;

    /**
     * Координаты выстрела игрока.
     */
    HashMap<Integer, Boolean> attackedCoordinates;

    /**
     * Координаты кораблей на доске.
     */
    HashMap<Ship.ShipNameList,int[]> shipPositions;

    /**
     * Логическая переменная, указывающая, выиграл ли игрок игру или нет.
     */
    boolean won;

    /**
     * Простой конструктор, заполняющий этот класс.
     * @param userId Идентификатор пользователя.
     * @param userName Имя пользователя.
     */
    public GameModel(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        attackedByEnemyCoordinates = new ArrayList<>();
        attackedCoordinates = new HashMap<>();
        shipPositions = new HashMap<>();
        this.won = false;
    }
}
