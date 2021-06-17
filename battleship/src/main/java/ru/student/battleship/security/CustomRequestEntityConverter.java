package ru.student.battleship.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.net.URI;

/**
 * Кастомный конвертер для конвертирования запроса на получение токена
 * @author Максим Щербаков
 */
@Component
public
class CustomRequestEntityConverter implements
        Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {
    /**
     * Экзепляр стандартного конвертера
     */
    private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

    /**
     * Конструктор класса конвертера CustomRequestEntityConverter
     */
    public CustomRequestEntityConverter() {
        defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    }

    /**
     * Переопределенный метод для конвертирования запроса на получение токена доступа от VKontakte
     * @param req запрос
     * @return конвертированный запрос на получение токена доступа от VKontakte
     */
    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {

        RequestEntity<?> entity = defaultConverter.convert(req);
        MultiValueMap<String, String> params = (MultiValueMap<String, String>) entity.getBody();
        String url = String.valueOf(entity.getUrl());

        url += "?client_id=" + req.getClientRegistration().getClientId()
                + "&client_secret=" + "ppLdln3eGiVagIdkHYzA"
                + "&redirect_uri=" + params.get("redirect_uri").toString()
                + "&code=" + params.get("code").toString();
        url = url.replace("[", "").replace("]", "");

        return new RequestEntity<>(entity.getHeaders(),
                    HttpMethod.GET, URI.create(url));

    }

}
