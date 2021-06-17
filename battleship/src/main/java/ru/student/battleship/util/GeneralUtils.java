package ru.student.battleship.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.student.battleship.model.Users;
import ru.student.battleship.model.projections.UserInfo;
import ru.student.battleship.security.LocalUser;
import ru.student.battleship.security.SocialProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для заполнения основных сведений о пользователе
 * @author Максим Щербаков
 */
public class GeneralUtils {
	/**
	 * Метод для задания роли пользователя
	 * @param roles роль
	 * @return полномочия пользователя
	 */
	public static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final String roles) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		return authorities;
	}

	/**
	 * Метод для установления провайдера пользователя
	 * @param providerId идентификатор провайдера
	 * @return провайдер пользователя
	 */
	public static SocialProvider toSocialProvider(String providerId) {
		for (SocialProvider socialProvider : SocialProvider.values()) {
			if (socialProvider.getProviderType().equals(providerId)) {
				return socialProvider;
			}
		}
		return SocialProvider.VK;
	}

	/**
	 * Метод для создания общей информации о пользователя
	 * @param localUser информация о пользователе
	 * @return общая информация о пользователе
	 */
	public static UserInfo buildUserInfo(LocalUser localUser) {
		List<String> roles = localUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
		Users user = localUser.getUser();
		return new UserInfo(user.getUserId(), user.getUserName(), roles);
	}
}
