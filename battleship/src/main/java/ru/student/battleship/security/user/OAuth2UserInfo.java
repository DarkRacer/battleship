package ru.student.battleship.security.user;

import java.util.Map;

/**
 * Абстрактный класс описывающий информацию получаемую с внешнего ресурса о пользователе
 * @author Максим Щербаков
 */
public abstract class OAuth2UserInfo {
	/**
	 * Атрибуты пользователя с внешнего ресурса
	 */
	protected Map<String, Object> attributes;

	/**
	 * Конструктор для заполнения атрибутов
	 * @param attributes атрибуты пользователя
	 */
	public OAuth2UserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Метод для получения атрибутов
	 * @return атрибуты пользователя
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * Метод для получения идентификатора пользователя на внешнем ресурсе
	 * @return идентификатор пользователя на внешнем ресурсе
	 */
	public abstract String getId();

	/**
	 * Метод для получения имени пользователя на внешнем ресурсе
	 * @return имя пользователя на внешнем ресурсе
	 */
	public abstract String getName();

	/**
	 * Метод для получения фотографии пользователя на внешнем ресурсе
	 * @return фотография пользователя на внешнем ресурсе
	 */
	public abstract String getImage();
}
