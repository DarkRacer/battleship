package ru.student.battleship.model;

import lombok.Data;

/**
 * Содержит 100 экземпляров боевой ячейки.
 * @author Максим Щербаков
 */
@Data
public class BattleBoard {
    /**
     * Поле, массив экземпляров класса BattleCell.
     */
    private BattleCell[][] battleCells;

    /**
     * Конструктор, инициализирующий 100 экземпляров класса BattleCell.
     */
    public BattleBoard() {
        battleCells = new BattleCell[10][10];
        for (int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++)
                battleCells[i][j] = new BattleCell();
        }
    }
}
