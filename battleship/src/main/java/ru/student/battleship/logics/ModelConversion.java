package ru.student.battleship.logics;

import ru.student.battleship.model.BattleCell;
import ru.student.battleship.model.GameInstance;
import ru.student.battleship.model.GameModel;
import ru.student.battleship.model.Ship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Класс конвертирует GameInstance обьект в GameModel pojo класс.
 * @author Максим Щербаков
 */
public class ModelConversion {

    /**
     * Конвертирует GameInstance обьект в GameModel pojo класс.
     * @param gameModel GameModel обьект.
     * @param gameInstance GameInstance обьект.
     * @return GameModel обьект.
     */
    public static GameModel convertGameInstance(GameModel gameModel, GameInstance gameInstance) {
        int positiveAttacks = 0;
        for (int i = 0; i<10; i++)
            for (int j = 0; j<10; j++) {
                BattleCell battleCell = gameInstance.getBattleBoard().getBattleCells()[i][j];
                if (battleCell.isAttackedByEnemy()) gameModel.getAttackedByEnemyCoordinates().add(i*10+j);
                if (battleCell.isAttackedToEnemy()) gameModel.getAttackedCoordinates().put(i*10+j, battleCell.isContainsEnemyShip());
            }
        ArrayList<Ship> ships = (ArrayList<Ship>) gameInstance.getShips();
        for (Ship ship : ships) {
            Ship.ShipNameList shipName = ship.getShipName();
            int[] shipPositions = ship.getCoordinates();
            gameModel.getShipPositions().put(shipName, shipPositions);
        }
        Collection<Boolean> shipAttackedCells = gameModel.getAttackedCoordinates().values();
        Iterator<Boolean> shipAttackedCellsIterator = shipAttackedCells.iterator();
        while (shipAttackedCellsIterator.hasNext()) {
            if (shipAttackedCellsIterator.next()) positiveAttacks++;
        }
        if (positiveAttacks == 15) gameModel.setWon(true);
        else gameModel.setWon(false);
        return gameModel;
    }
}
