package ru.student.battleship.security.user;

import java.util.Map;

/**
 * Класс для формирования информации о пользователе на основании полученной информации от VKontakte
 * @author Максим Щербаков
 */
public class VkOAuth2UserInfo extends OAuth2UserInfo {

    /**
     * Конструктор для заполнения атрибутов
     * @param attributes атрибуты
     */
    public VkOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     * Метод возвращающий идентификатор пользователя в VKontakte
     * @return идентификатор пользователя в VKontakte
     */
    @Override
    public String getId() {
        return  String.valueOf(attributes.get("id"));
    }

    /**
     * Метод возвращающий имя пользователя в VKontakte
     * @return имя в VKontakte
     */
    @Override
    public String getName() {
        return attributes.get("first_name") + " " + attributes.get("last_name");
    }

    /**
     * Метод возвращающий фотографию пользователя в VKontakte
     * @return фотографию в VKontakte
     */
    @Override
    public String getImage() {
        return (String) attributes.get("photo_max_orig");
    }
}
