package ru.student.battleship.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Класс для работы с токеном доступа
 * @author Максим Щербаков
 */
@Service
public class TokenProvider {

	/**
	 * Экземпляр интерфейса Logger для логирования
	 */
	private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

	/**
	 * Экземпляр класса AppProperties
	 */
	private AppProperties appProperties;

	/**
	 * Конструктор класса для работы с токеном доступа
	 * @param appProperties свойства приложения
	 */
	public TokenProvider(AppProperties appProperties) {
		this.appProperties = appProperties;
	}

	/**
	 * Метод для создания токена доступа
	 * @param authentication аутентификация
	 * @return токен доступа
	 */
	public String createToken(Authentication authentication) {
		LocalUser userPrincipal = (LocalUser) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

		return Jwts.builder().setSubject(userPrincipal.getUser().getUserId()).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret()).compact();
	}

	/**
	 * Метод возвращающий идентификатор пользователя из токена
	 * @param token токен доступа
	 * @return Идентификатор пользователя из токена
	 */
	public Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(token).getBody();

		return Long.parseLong(claims.getSubject());
	}

	/**
	 * Метод для валидации токена доступа
	 * @param authToken токен доступа
	 * @return результат валидации
	 */
	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			logger.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty.");
		}
		return false;
	}
}
