package ru.student.battleship.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с свойствами приложения
 * @author Максим Щербаков
 */
@Component
@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {
    /**
     * Экземпляр класса Auth
     */
    private final Auth auth = new Auth();

    /**
     * Экземпляр класса OAuth2
     */
    private final OAuth2 oauth2 = new OAuth2();

    /**
     * Статический класс для работы с авторизацией
     * @author Максим Щербаков
     */
    @Getter
    @Setter
    public static class Auth {
        /**
         * Пароль токена
         */
        private String tokenSecret;

        /**
         * Время истечения токена
         */
        private long tokenExpirationMsec;
    }

    /**
     * Статический класс для работы с авторизацией через OAuth2
     * @author Максим Щербаков
     */
    @Getter
    @Setter
    public static final class OAuth2 {
        /**
         * Список ссылок для редиректа
         */
        private List<String> authorizedRedirectUris = new ArrayList<>();
    }
}

