package ru.student.battleship.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Модель, которая сохраняется в MongoDB.
 * Содержит идентификаторы пользователей обоих игроков,
 * которые в данный момент играют в игру, и основного ключ,
 * который является идентификатором сокета, используемым обоими игроками для связи с сервером.
 * @author Максим Щербаков
 */
@Data
@NoArgsConstructor
public class PlayerMatches {

    /**
     * Уникальный идентификатор, который также является адресом веб-сокета,
     * который используется для связи с сервером.
     */
    @Id String webSocketAddress;

    /**
     * ID пользователя игрока 1.
     */
    private String player1;

    /**
     * ID пользователя игрока 2.
     */
    private String player2;

    /**
     * Базовый конструктор, заполняющий модель.
     * Идентификатор сокета автоматически генерируется MongoDB.
     * @param player1 id первого игрока
     * @param player2 id второго игрока
     */
    public PlayerMatches(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Конструктор, который заполняет только идентификатор сокета и идентификатор пользователя первого игрока.
     * Модель создана изначально из этого конструктора и позже заполняется идентификатором игрока 2 из предыдущего конструктора.
     * @param player1 id второго игрока
     */
    public PlayerMatches(String player1) {
        this.player1 = player1;
    }
}
