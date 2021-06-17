package ru.student.battleship.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  Основной класс который содержит все игровые данные. Содержит игровое поле, корабли,
 *  имя пользователя. Инициализирует доску случайными позициями кораблей.
 *  Эта модель обновляется каждый раз, когда игрок или их противник делает ход.
 *  Он также сообщает клиенту, выиграл ли игрок матч или нет.
 *  @author Максим Щербаков
 */
@Data
@NoArgsConstructor
public class GameInstance {

    /**
     * Id игровых данных
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * UserId игрока
     */
    private String userId;

    /**
     * Имя пользователя игрока.
     */
    private String userName;

    /**
     * Экземпляр игрового поля, на котором будет 100 экземпляра клеток.
     */
    private BattleBoard battleBoard;

    /**
     * Лист кораблей
     */
    private List<Ship> ships;

    /**
     * Содержит значение от 0 до 20.
     */
    private int attackedShips;

    /**
     * Показывает, сколько раз этот пользователь выигрывал игры.
     */
    private int wonGames = 0;

    /**
     * Показывает, сколько раз этот пользователь проигрывал игры.
     */
    private int lostGames = 0;

    /**
     * Инициализация доски случайными позициями кораблей. Используется обоими игроками 1 и 2.
     * Вызывается только когда новый пользователь играет в игру.
     * @param userId User-Id игрока, который генерируется в контроллерах.
     * @param userName Имя пользователя игрока, которое дает сам пользователь во внешнем интерфейсе.
     */
    public GameInstance(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.attackedShips = 0;
        this.wonGames = 0;
        this.lostGames = 0;
        this.battleBoard = new BattleBoard();
        ships = new ArrayList<Ship>() {{
            add(new Ship(1, Ship.ShipNameList.FIRST_ATTACKER));
            add(new Ship(1, Ship.ShipNameList.SECOND_ATTACKER));
            add(new Ship(1, Ship.ShipNameList.THIRD_ATTACKER));
            add(new Ship(1, Ship.ShipNameList.FOURTH_ATTACKER));
            add(new Ship(2, Ship.ShipNameList.FIRST_DESTROYER));
            add(new Ship(2, Ship.ShipNameList.SECOND_DESTROYER));
            add(new Ship(2, Ship.ShipNameList.THIRD_DESTROYER));
            add(new Ship(3, Ship.ShipNameList.FIRST_SUBMARINE));
            add(new Ship(3, Ship.ShipNameList.SECOND_SUBMARINE));
            add(new Ship(4, Ship.ShipNameList.FIRST_CRUISER));
        }};
        for (Object ship : ships) {
            setRandomPositions(battleBoard, (Ship) ship);
        }
    }

    /**
     * Инициализация доски случайными позициями кораблей. Этот конструктор используется, если к игре присоединяется повторяющийся игрок.
     * @param userId User-Id игрока, который генерируется контроллерами.
     * @param userName Имя пользователя игроков, которое дает сам пользователь во внешнем интерфейсе.
     * @param wonGames Количество раз, когда пользователь выигрывал игру, доступную для контроллера из уже существующего экземпляра.
     * @param lostGames Количество раз, когда пользователь проигрывал игру, доступную для контроллера из уже существующего экземпляра.
     */
    public GameInstance (String userId, String userName, int wonGames, int lostGames) {
        this.userId = userId;
        this.userName = userName;
        this.attackedShips = 0;
        this.wonGames = wonGames;
        this.lostGames = lostGames;
        this.battleBoard = new BattleBoard();
        ships = new ArrayList<Ship>() {{
            add(new Ship(1, Ship.ShipNameList.FIRST_ATTACKER));
            add(new Ship(1, Ship.ShipNameList.SECOND_ATTACKER));
            add(new Ship(1, Ship.ShipNameList.THIRD_ATTACKER));
            add(new Ship(1, Ship.ShipNameList.FOURTH_ATTACKER));
            add(new Ship(2, Ship.ShipNameList.FIRST_DESTROYER));
            add(new Ship(2, Ship.ShipNameList.SECOND_DESTROYER));
            add(new Ship(2, Ship.ShipNameList.THIRD_DESTROYER));
            add(new Ship(3, Ship.ShipNameList.FIRST_SUBMARINE));
            add(new Ship(3, Ship.ShipNameList.SECOND_SUBMARINE));
            add(new Ship(4, Ship.ShipNameList.FIRST_CRUISER));
        }};
        for (Object ship : ships) {
            setRandomPositions(battleBoard, (Ship) ship);
        }
    }

    /**
     * Используется конструктором для установки случайных позиций корабля на доске.
     * @param battleBoard игровое поле
     * @param ship корабль, которому нужно присвоить случайную позицию.
     */
    @SneakyThrows(NullPointerException.class)
    public void setRandomPositions(BattleBoard battleBoard, Ship ship) {
        boolean isHorizontal;
        BattleCell oneCell = null;
        Random random = new Random();
        int x = 0;
        int y = 0;
        do {
            isHorizontal = returnRandomBoolean();
            if (isHorizontal) {
                x = random.nextInt(10 - ship.getSize() + 1);
                y = random.nextInt(10);
            } else {
                x = random.nextInt(10);
                y = random.nextInt(10 - ship.getSize() + 1);
            }

        } while (!areCellsEmpty(x, y, isHorizontal, battleBoard, ship.getSize()));
        if (isHorizontal)
            for (int i = 0; i < ship.getSize(); i++) {
                ship.getCoordinates()[i] = (x + i) * 10 + y;
                oneCell = battleBoard.getBattleCells()[x + i][y];
                oneCell.setContainsShip(true);
                for (int j = 0; j<3; j++) {
                    for (int k = 0; k<3; k++) {
                        try {
                            BattleCell theCell = battleBoard.getBattleCells()[x+i-1+j][y-1+k];
                            theCell.setGreyArea(true);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        else
            for (int i = 0; i < ship.getSize(); i++) {
                ship.getCoordinates()[i] = x * 10 + (y + i);
                oneCell = battleBoard.getBattleCells()[x][y + i];
                oneCell.setContainsShip(true);
                for (int j = 0; j<3; j++) {
                    for (int k = 0; k<3; k++) {
                        try {
                            BattleCell theCell = battleBoard.getBattleCells()[x-1+j][y+i-1+k];
                            theCell.setGreyArea(true);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        oneCell.setShipName(ship.getShipName());
    }

    /**
     * Проверяет, пуста ли последовательность ячеек на поле боя,
     * чтобы туда поместился корабль определенного размера.
     * @param x Координата X крайнего верхнего или крайнего левого положения корабля в зависимости от их положения,
     * либо по вертикали, либо по горизонтали соответственно.
     * @param y Координата Y крайней верхней или крайней левой позиции корабля в зависимости от их положения,
     * либо по вертикали, либо по горизонтали соответственно.
     * @param isHorizontal горизонтально или вертикально размещался корабль.
     * @param battleBoard игровое поле
     * @param shipSize размер корабля
     * @return Логическое значение, независимо от того, пусты ли все ячейки или нет, чтобы в них можно было разместить корабль.
     */
    private boolean areCellsEmpty(int x, int y, boolean isHorizontal, BattleBoard battleBoard, int shipSize) {
        boolean flag = true;
        BattleCell oneCell = null;
        if (isHorizontal)
            for (int i = 0; i < shipSize; i++) {
                oneCell = battleBoard.getBattleCells()[x + i][y];
                if (oneCell.isContainsShip() || oneCell.isGreyArea()) flag = false;
            }
        else
            for (int i = 0; i < shipSize; i++) {
                oneCell = battleBoard.getBattleCells()[x][y + i];
                if (oneCell.isContainsShip() || oneCell.isGreyArea()) flag = false;
            }
        return flag;
    }

    /**
     * Возвращает случайные истинные или ложные значения
     * @return Случайное логическое значение
     */
    public boolean returnRandomBoolean() {
        return (Math.random() < 0.5);
    }

    /**
     * Выполняет набор действий, когда противник атакует это поле боя.
     * Для переменной setAttackedByEnemy установлено значение true.
     * Если ячейка содержит корабль, переменная attackedShips увеличивается на единицу.
     * Количество атак корабля, находившегося в этой ячейке, также увеличивается.
     * Если ячейка содержала корабль, возвращается значение true. Иначе возвращается false
     * @param coordinate Координата ячейки.
     * @return boolean содержит клетка корабль
     */
    public boolean enemyTurn (int coordinate) {
        int x = coordinate/10;
        int y = coordinate%10;
        BattleCell battleCell = this.getBattleBoard().getBattleCells()[x][y];
        battleCell.setAttackedByEnemy(true);
        if (battleCell.isContainsShip()) {
            this.attackedShips++;

            Ship.ShipNameList shipName = battleCell.getShipName();
            for (Object ship : ships) {
                if (((Ship)ship).getShipName() == shipName) {
                    ((Ship)ship).setAttackCount(((Ship)ship).getAttackCount()+1);
                }
            }
            return true;
        }
        else return false;
    }
}
