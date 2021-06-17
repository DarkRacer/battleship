package ru.student.battleship.util;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

/**
 * Класс для работы с cookie
 * @author Максим Щербаков
 */
public class CookieUtils {

	/**
	 * метод для получения cookie
	 * @param request запрос
	 * @param name название cookie
	 * @return cookie
	 */
	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return Optional.of(cookie);
				}
			}
		}

		return Optional.empty();
	}

	/**
	 * Метод для добавления cookie
	 * @param response ответ
	 * @param name название cookie
	 * @param value значение cookie
	 * @param maxAge продолжительность существования cookie
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	/**
	 * Метод для удаления cookie
	 * @param request запрос
	 * @param response ответ
	 * @param name название cookie
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setSecure(true);
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}

	/**
	 * Метод проводящий сериализацию
	 * @param object объект для сериализации
	 * @return сериализованный объект
	 */
	public static String serialize(Object object) {
		return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
	}

	/**
	 * Метод проводящий десериализацию
	 * @param cookie cookie
	 * @param cls класс в который десериализуется
	 * @return десериализованый объект
	 */
	public static <T> T deserialize(Cookie cookie, Class<T> cls) {
		return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
	}
}
