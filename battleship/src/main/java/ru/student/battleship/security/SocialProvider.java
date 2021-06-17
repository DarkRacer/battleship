package ru.student.battleship.security;

/**
 * Перечисление провайдеров
 * @author Максим Щербаков
 */
public enum SocialProvider {

	/**
	 * Провайдер Vkontakte
	 */
	VK("vk");

	/**
	 * Поле для храения типа провайдера
	 */
	private String providerType;

	/**
	 * Метод для получения типа провайдера
	 * @return тип провайдера
	 */
	public String getProviderType() {
		return providerType;
	}

	/**
	 * Конструктор для заполнения типа провайдера
	 * @param providerType тип провайдера
	 */
	SocialProvider(final String providerType) {
		this.providerType = providerType;
	}

}
