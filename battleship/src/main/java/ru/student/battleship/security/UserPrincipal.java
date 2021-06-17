package ru.student.battleship.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.student.battleship.model.Users;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Класс для описания пользователя на сервисе
 * @author Максим Щербаков
 */
@Data
public class UserPrincipal implements OAuth2User, UserDetails {
    /**
     * Идентификатор пользователя
     */
    private String id;

    /**
     * Провайдер пользователя
     */
    private String provider;

    /**
     * Права пользователя
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Атрибуты пользователя
     */
    private Map<String, Object> attributes;

    /**
     * Конструктор для создания объекта пользователя на сервисе
     * @param id идентификатор пользователя
     * @param provider провайдер пользователя
     * @param authorities права пользователя
     */
    public UserPrincipal(String id, String provider, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.provider = provider;
        this.authorities = authorities;
    }

    /**
     * Метод для создания пользователя в системе
     * @param users пользователь
     * @return объект пользователя в системе
     */
    public static UserPrincipal create(Users users) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                users.getUserId(),
                users.getProvider(),
                authorities
        );
    }

    /**
     * Метод для создания пользователя в системе
     * @param user пользователь
     * @param attributes атрибуты пользователя
     * @return объект пользователя в системе
     */
    public static UserPrincipal create(Users user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    /**
     * Метод возвращающий имя пользователя
     * @return имя пользователя
     */
    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    /**
     * Метод возвращающий информацию о не истечении срока действия учетной записи
     * @return информация о не истечении срока действия учетной записи
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Метод возвращающий информацию о не блокировки учетной записи
     * @return информация о не блокировки учетной записи
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Метод возвращающий информацию о не истечении полномочий учетной записи
     * @return информация о не истечении полномочий учетной записи
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Метод возвращающий информацию о включении учетной записи
     * @return информация о включении учетной записи
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Метод возвращающий информацию о правах учетной записи
     * @return информация о правах учетной записи
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Метод возвращающий пароль учетной записи
     * @return пароль учетной записи
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * Метод возвращающий атрибуты учетной записи
     * @return атрибуты учетной записи
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Метод возвращающий имя учетной записи
     * @return имя учетной записи
     */
    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
