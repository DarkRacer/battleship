package ru.student.battleship.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.student.battleship.util.CookieUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static ru.student.battleship.security.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * Класс для описания обработчика удачной авторизации
 * @author Максим Щербаков
 */
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	/**
	 * Экземпляр класса TokenProvider
	 */
	private TokenProvider tokenProvider;

	/**
	 * Экземпляр репозитория HttpCookieOAuth2AuthorizationRequestRepository
	 */
	private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

	@Autowired
	OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
		this.tokenProvider = tokenProvider;
		this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
	}

	/**
	 * Метод для редиректа с токеном доступа и идентификатором пользователя после успешной авторизации
	 * @param request запрос на авторизацию
	 * @param response ответ на авторизацию
	 * @param authentication аутентификация
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		String targetUrl = determineTargetUrl(request);

		clearAuthenticationAttributes(request, response);
		LocalUser userPrincipal = (LocalUser) authentication.getPrincipal();

		Cookie token = new Cookie("Authorization", tokenProvider.createToken(authentication));
		Cookie userId = new Cookie("user_id", String.valueOf(userPrincipal.getUser().getId()));

		token.setMaxAge(180);
		token.setPath("/");
		token.setDomain("localhost");
		response.addCookie(token);

		userId.setMaxAge(360);
		userId.setPath("/");
		userId.setDomain("localhost");
		response.addCookie(userId);

		response.addHeader("user_id", String.valueOf(userPrincipal.getUser().getUserId()));
		response.addHeader("Authorization", tokenProvider.createToken(authentication));

		getRedirectStrategy().sendRedirect(request, response, targetUrl);

	}

	/**
	 * Метод для формирования ссылки для редиректа
	 * @param request запрос на авторизацию
	 * @return ссылка для редиректа
	 */
	protected String determineTargetUrl(HttpServletRequest request) {
		Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

		String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

		return UriComponentsBuilder.fromUriString(targetUrl).build().toUriString();
	}

	/**
	 * Метод для очистки авторизационных атрибутов
	 * @param request запрос на авторизацию
	 * @param response ответ на авторизацию
	 */
	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		super.clearAuthenticationAttributes(request);
		httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}
}
