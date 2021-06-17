package ru.student.battleship.security.user;

import ru.student.battleship.security.SocialProvider;

import java.util.Map;

/**
 * Фабрика формирования информации о пользователе
 * @author Максим Щербаков
 */
public class OAuth2UserInfoFactory {
	/**
	 * Метод для формирования информации о пользователя на основании данных от VKontakte
	 * @param registrationId идентификатор провайдера при регистрации
	 * @param attributes атрибуты от VKontakte
	 * @return информация о пользователя
	 */
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		if (registrationId.equalsIgnoreCase(SocialProvider.VK.getProviderType())) {
			return new VkOAuth2UserInfo(attributes);
		}
		return null;
	}
}
