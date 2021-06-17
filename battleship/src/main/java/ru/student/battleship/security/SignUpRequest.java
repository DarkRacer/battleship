package ru.student.battleship.security;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Класс описывающий запрос на регистрацию
 * @author Максим Щербаков
 */
@Data
public class SignUpRequest {

    /**
     * Идентификатор пользователя
     */
    private Long userID;

    /**
     * Идентификатор пользователя на внешнем ресурсе
     */
    private String providerUserId;

    /**
     * Имя пользователя на внешнем ресурсе
     */
    @NotEmpty
    private String displayName;

    /**
     * Провайдер пользователя
     */
    private SocialProvider socialProvider;

    /**
     * Фотография пользователя на внешнем ресурсе
     */
    private String picture;

    /**
     * Соответствующий пароль
     */
    @NotEmpty
    private String matchingPassword;

    /**
     * Конструктор запроса на регистрацию
     * @param providerUserId идентификатор пользователя на внешнем ресурсе
     * @param displayName имя пользователя на внешнем ресурсе
     * @param picture фотография пользователя на внешнем ресурсе
     * @param socialProvider провайдер
     */
    public SignUpRequest(String providerUserId, String displayName, String picture, SocialProvider socialProvider) {
        this.providerUserId = providerUserId;
        this.displayName = displayName;
        this.picture = picture;
        this.socialProvider = socialProvider;
    }

    /**
     * Метод возвращающий новый экземпляр статического класса Builder
     * @return nновый экземпляр статического класса Builder
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Статический класс для построения запроса на регистрации
     * @author Максим Щербаков
     */
    public static class Builder {
        /**
         * Идентификатор пользователя на внешнем ресурсе
         */
        private String providerUserID;

        /**
         * Имя пользователя на внешнем ресурсе
         */
        private String displayName;

        /**
         * Фотография пользователя на внешнем ресурсе
         */
        private String picture;

        /**
         * Провайдер пользователя
         */
        private SocialProvider socialProvider;

        /**
         * Метод для добавления идентификатора пользователя на внешнем ресурсе
         * @param userID идентификатор пользователя на внешнем ресурсе
         * @return экземпляр класса для построения запроса на регистрации
         */
        public Builder addProviderUserID(final String userID) {
            this.providerUserID = userID;
            return this;
        }

        /**
         * Метод для добавления имени пользователя на внешнем ресурсе
         * @param displayName имя пользователя на внешнем ресурсе
         * @return экземпляр класса для построения запроса на регистрации
         */
        public Builder addDisplayName(final String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Метод для добавления фотографии пользователя на внешнем ресурсе
         * @param picture фотография пользователя на внешнем ресурсе
         * @return экземпляр класса для построения запроса на регистрации
         */
        public Builder addPicture(final String picture) {
            this.picture = picture;
            return this;
        }

        /**
         * Метод для добавления провайдера пользователя
         * @param socialProvider провайдер пользователя
         * @return экземпляр класса для построения запроса на регистрации
         */
        public Builder addSocialProvider(final SocialProvider socialProvider) {
            this.socialProvider = socialProvider;
            return this;
        }

        /**
         * Метод для составления запроса на регистрацию
         * @return запрос на регистрацию
         */
        public SignUpRequest build() {
            return new SignUpRequest(providerUserID, displayName, picture, socialProvider);
        }
    }
}

