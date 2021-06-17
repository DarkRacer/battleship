package ru.student.battleship.model;

import lombok.Data;

/**
 * Класс ячейки игрового поля
 * @author Максим Щербаков
 */
@Data
public class BattleCell {

    /**
     * Аттакована ли клетка противником
     */
    private boolean attackedByEnemy;

    /**
     * Аттакована ли клетка противника
     */
    private boolean attackedToEnemy;

    /**
     * Является клеткой содержащей корабль
     */
    private boolean containsEnemyShip;

    /**
     * Является клеткой содержащей корабль на поле противника.
     */
    private boolean containsShip;

    /**
     * Название корабля, который находится в этой ячейке.
     */
    private Ship.ShipNameList shipName;

    /**
     * Находится ли клетка рядом с кораблем или нет.
     */
    private boolean greyArea;

    /**
     * Конструктор, инициализирующий все с помощью false.
     */
    public BattleCell() {
        this.attackedByEnemy = false;
        this.containsShip = false;
        this.attackedToEnemy = false;
        this.containsEnemyShip = false;
    }
}
