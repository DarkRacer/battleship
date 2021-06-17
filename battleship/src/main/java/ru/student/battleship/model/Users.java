package ru.student.battleship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Модель пользователя, которая сохраняется в MongoDB.
 * Содержит идентификатор пользователя, имя пользователя, ссылка на фотографию, количество побед, количество проигрышей,
 * статус пользователя, провайдер пользователя, id пользователя на внешнем ресурсе.
 * @author Максим Щербаков
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    /**
     * Идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * Имя пользователя
     */
    private String userName;

    /**
     * Ссылка на фотографию
     */
    private String picture;

    /**
     * Количество побед
     */
    private int win;

    /**
     * Количество проигрышей
     */
    private int lost;

    /**
     * Статус пользователя
     */
    private String status;

    /**
     * Провайдер пользователя
     */
    private String provider;

    /**
     * Id пользователя на внешнем ресурсе
     */
    private String userId;

    /**
     * Конструктор пользователя
     * @param id идентификатор пользователя
     * @param userName имя пользователя
     * @param picture ссылка на фотографию
     * @param status статус пользователя
     * @param provider провайдер пользователя
     * @param userId id пользователя на внешнем ресурсе
     */
    public Users(String id, String userName, String picture, String status, String provider, String userId) {
        this.id = id;
        this.userName = userName;
        this.picture = picture;
        this.win = 0;
        this.lost = 0;
        this.status = status;
        this.provider = provider;
        this.userId = userId;
    }
}
