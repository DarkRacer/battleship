package ru.student.battleship.security;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import ru.student.battleship.model.Users;

import java.util.Map;

/**
 * Интерфейс для описания методов сервиса UserService
 * @author Максим Щербаков
 */
public interface UserService {

	/**
	 * Метод для регистрации нового пользователя
	 * @param signUpRequest запрос на регистрацию
	 * @return зарегистрированный пользователь
	 */
	public Users registerNewUser(SignUpRequest signUpRequest);

	/**
	 * Поиск пользователя по его id на внешнем ресурсе
	 * @param id идентификатор пользователя на внешнем ресурсе
	 * @return найденный пользователь
	 */
	Users findUsersByProviderUserId(String id);

	/**
	 * Метод для процесса регистрации пользователя
	 * @param registrationId идентификатор сервиса
	 * @param attributes атрибуты регистрации
	 * @param idToken идентификатор токена
	 * @param userInfo информация о пользователе
	 * @return локальный пользователь
	 */
	LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
}
