package ru.student.battleship.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.student.battleship.model.Users;
import ru.student.battleship.util.GeneralUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Класс описывающий локального пользователя в системе
 * @author Максим Щербаков
 */
public class LocalUser extends User implements OAuth2User, OidcUser {

    /**
     * UID серийной версии
     */
    private static final long serialVersionUID = -2845160792248762779L;

    /**
     * Идентификатор токена
     */
    private final OidcIdToken idToken;

    /**
     * Информация о пользователе
     */
    private final OidcUserInfo userInfo;

    /**
     * Атрибуты пользователя
     */
    private Map<String, Object> attributes;

    /**
     * Экземпляр пользователя
     */
    private Users user;

    /**
     * Конструктор создающий локального пользователя в системе
     * @param userID идентификатор пользователя на внешнем ресурсе
     * @param accountNonExpired переменная указывающая истекла ли учетная запись
     * @param credentialsNonExpired переменная указывающая истекли ли права учетной записи
     * @param accountNonLocked переменная указывающая заблокирована ли учетная запись
     * @param authorities права пользователя
     * @param user пользователь
     */
    public LocalUser(final String userID, final boolean accountNonExpired, final boolean credentialsNonExpired,
                     final boolean accountNonLocked, final Collection<? extends GrantedAuthority> authorities, final Users user) {
        this(userID, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, user, null, null);
    }

    /**
     * Конструктор создающий локального пользователя в системе
     * @param userID идентификатор пользователя на внешнем ресурсе
     * @param accountNonExpired переменная указывающая истекла ли учетная запись
     * @param credentialsNonExpired переменная указывающая истекли ли права учетной записи
     * @param accountNonLocked переменная указывающая заблокирована ли учетная запись
     * @param authorities права пользователя
     * @param user пользователь
     * @param idToken идентификатор пользователя
     * @param userInfo информация о пользователе
     */
    public LocalUser(final String userID, final boolean accountNonExpired, final boolean credentialsNonExpired,
                     final boolean accountNonLocked, final Collection<? extends GrantedAuthority> authorities, final Users user, OidcIdToken idToken,
                     OidcUserInfo userInfo) {
        super(userID, "",  true, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = user;
        this.idToken = idToken;
        this.userInfo = userInfo;
    }

    /**
     * Метод создающий локального пользователя в системе
     * @param user пользователь
     * @param attributes атрибуты пользователя
     * @param idToken идентификатор токена
     * @param userInfo информация о пользователе
     * @return локальный пользователь
     */
    public static LocalUser create(Users user, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        LocalUser localUser;
        localUser = new LocalUser(String.valueOf(user.getUserId()), true, true, true, GeneralUtils.buildSimpleGrantedAuthorities("ROLE_USER"),
                user, idToken, userInfo);

        localUser.setAttributes(attributes);
        return localUser;
    }

    /**
     * Метод записывающий атрибуты пользователя
     * @param attributes атрибуты пользователя
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * Метод возвращающий имя пользователя
     * @return имя пользователя
     */
    @Override
    public String getName() {
        return this.user.getUserName();
    }

    /**
     * Метод возвращающий атрибуты пользователя
     * @return атрибуты пользователя
     */
    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    /**
     * Метод возвращающий атрибуты пользователя
     * @return атрибуты пользователя
     */
    @Override
    public Map<String, Object> getClaims() {
        return this.attributes;
    }

    /**
     * Метод возвращающий информацию о пользователе
     * @return информация о пользователе
     */
    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

    /**
     * Метод возвращающий идентификатор токена
     * @return идентификатор токена
     */
    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }

    /**
     * Метод возвращающий пользователя
     * @return пользователь
     */
    public Users getUser() {
        return user;
    }
}

