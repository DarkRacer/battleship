package ru.student.battleship.model.projections;

import lombok.Value;

import java.util.List;

/**
 * Проекция информации о пользователе
 * @author максим Щербаков
 */
@Value
public class UserInfo {
    /**
     * Id пользователя
     */
    private String id;

    /**
     * Имя пользователя
     */
    private String displayName;

    /**
     * Роли пользователя
     */
    private List<String> roles;
}
