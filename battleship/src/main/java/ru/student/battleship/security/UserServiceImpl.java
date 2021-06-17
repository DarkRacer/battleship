package ru.student.battleship.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.student.battleship.model.Users;
import ru.student.battleship.repository.UserRepository;
import ru.student.battleship.security.user.OAuth2UserInfo;
import ru.student.battleship.security.user.OAuth2UserInfoFactory;
import ru.student.battleship.util.GeneralUtils;
import java.util.List;
import java.util.Map;

/**
 * Сервис расширяющий интерфейс UserService
 * @author Максим Щербаков
 */
@Service
public class UserServiceImpl implements UserService {

	/**
	 * Экземпляр репозитория UserRepository
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * Метод для регистрации нового пользователя
	 * @param signUpRequest запрос на регистрацию
	 * @return зарегистрированный пользователь
	 */
	@Override
	@Transactional(value = "transactionManager")
	public Users registerNewUser(final SignUpRequest signUpRequest) {
		Users user = buildUser(signUpRequest);
		user = userRepository.save(user);
		return user;
	}

	/**
	 * Метод для формирования пользователя
	 * @param formDTO запрос, который необходимо преобразовать в экземпляр пользователя
	 * @return пользователь
	 */
	private Users buildUser(final SignUpRequest formDTO) {
		Users user = new Users();
		user.setUserId(formDTO.getProviderUserId());
		user.setUserName(formDTO.getDisplayName());
		user.setPicture(formDTO.getPicture());
		user.setProvider(formDTO.getSocialProvider().getProviderType());
		user.setWin(0);
		user.setLost(0);
		return user;
	}

	/**
	 * Метод для процесса регистрации пользователя
	 * @param registrationId идентификатор сервиса
	 * @param attributes атрибуты регистрации
	 * @param idToken идентификатор токена
	 * @param userInfo информация о пользователе
	 * @return локальный пользователь
	 */
	@Override
	@Transactional
	public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);

		assert oAuth2UserInfo != null;
		SignUpRequest userDetails = toUserRegistrationObject(registrationId, oAuth2UserInfo);
		Users user;
		user = findUsersByProviderUserId(oAuth2UserInfo.getId());


		if (user != null) {
			user = updateExistingUser(user, oAuth2UserInfo);
		} else {
			user = registerNewUser(userDetails);
		}

		return LocalUser.create(user, attributes, idToken, userInfo);
	}

	/**
	 * Метод для обновления существующего пользователя
	 * @param existingUser существующий пользователь
	 * @param oAuth2UserInfo информация о пользователе с внешнего сервиса
	 * @return обновленный пользователь
	 */
	private Users updateExistingUser(Users existingUser, OAuth2UserInfo oAuth2UserInfo) {
		existingUser.setPicture(oAuth2UserInfo.getImage());

		return userRepository.save(existingUser);
	}

	/**
	 * Метод для преобразования информации с внешнего ресурса в объект для регистрации
	 * @param registrationId идентификатор провайдера
	 * @param oAuth2UserInfo информация с внешнего ресурса
	 * @return объект для регистрации
	 */
	private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
		return SignUpRequest.getBuilder().addProviderUserID(oAuth2UserInfo.getId()).addDisplayName(oAuth2UserInfo.getName())
				.addSocialProvider(GeneralUtils.toSocialProvider(registrationId)).addPicture(oAuth2UserInfo.getImage()).build();
	}

	/**
	 * Поиск пользователя по его id на внешнем ресурсе
	 * @param id идентификатор пользователя на внешнем ресурсе
	 * @return найденный пользователь
	 */
	@Override
	public Users findUsersByProviderUserId(String id) {
		List<Users> users = userRepository.findUsersByUserId(id);
		if (!users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}
}
