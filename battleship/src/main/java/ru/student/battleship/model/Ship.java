package ru.student.battleship.model;

import lombok.Data;

/**
 * Класс корабля, содержащий имя shp, размер корабля и количество атак на корабль. Если
 * количество атак равно размеру корабля, это означает, что корабль полностью уничтожен.
 * Он также содержит корабль переменная координат, содержащая координаты места нахождения
 * корабля на доске.
 * @author Максим Щербаков
 */
@Data
public class Ship {

    /**
     * Перечень названий кораблей.
     */
    public enum ShipNameList {
        FIRST_ATTACKER,SECOND_ATTACKER,THIRD_ATTACKER,FOURTH_ATTACKER,
        FIRST_DESTROYER,SECOND_DESTROYER,THIRD_DESTROYER,
        FIRST_SUBMARINE,SECOND_SUBMARINE,FIRST_CRUISER
    }

    /**
     * Размер корабля.
     */
    private int size;

    /**
     * Координаты на доске, где стоит корабль.
     */
    private int[] coordinates;

    /**
     * Количество атак на корабль.
     */
    private int attackCount;

    /**
     * Название корабля из указанного выше перечня кораблей.
     */
    private ShipNameList shipName;

    /**
     * Базовый конструктор, инициализирующий класс.
     * Координаты установлены на 0.
     * @param size Размер корабля.
     * @param shipName Название корабля.
     */
    public Ship (int size, ShipNameList shipName) {
        this.size = size;
        this.attackCount = 0;
        coordinates = new int[size];
        for (int i = 0; i<size; i++) {
            coordinates[i] = 0;
        }
        this.shipName = shipName;
    }
}
