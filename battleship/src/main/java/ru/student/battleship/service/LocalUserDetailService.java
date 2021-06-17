package ru.student.battleship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.student.battleship.model.Users;
import ru.student.battleship.security.LocalUser;
import ru.student.battleship.security.UserService;
import ru.student.battleship.util.GeneralUtils;

import java.util.Objects;

/**
 * Сервис для работы с пользователем, который работает с системой
 * @author Максим Щербаков
 */
@Service("localUserDetailService")
public class LocalUserDetailService implements UserDetailsService {

	/**
	 * Экземпляр сервиса UserService
	 */
	@Autowired
	private UserService userService;

	/**
	 * Метод для загрузки локального пользователя по его имени
	 * @param username имя пользователя
	 * @return локальный пользователь
	 */
	@Override
	@Transactional
	public LocalUser loadUserByUsername(final String username) throws UsernameNotFoundException {
		Users user;
		user = userService.findUsersByProviderUserId(username);
		if (user == null) {
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}

		return createLocalUser(user);
	}

	/**
	 * Метод для загрузки локального пользователя по его идентификатору
	 * @param id идентификатор пользователя
	 * @return локальный пользователь
	 */
	@Transactional
	public LocalUser loadUserById(String id) {
		Users user = userService.findUsersByProviderUserId(id);
		return createLocalUser(user);
	}

	/**
	 * Метод для создания локального пользователя
	 * @param user объект пользователя
	 * @return локальный пользователь
	 */
	private LocalUser createLocalUser(Users user) {
		return new LocalUser(String.valueOf(user.getUserId()), true, true, true, GeneralUtils.buildSimpleGrantedAuthorities("ROLE_USER"), user);
	}
}
