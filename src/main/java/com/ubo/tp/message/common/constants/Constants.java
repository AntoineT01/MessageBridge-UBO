package com.ubo.tp.message.common.constants;

import java.util.UUID;

/**
 * Classe de contantes de l'appli.
 *
 * @author S.Lucas
 */
public class Constants {
	/**
	 * Extension des fichiers de propriété des User
	 */
	public static final String USER_FILE_EXTENSION = "usr";

	/**
	 * Extension des fichiers de propriété des Message
	 */
	public static final String MESSAGE_FILE_EXTENSION = "msg";

	/**
	 * Répertoire des fichiers temporaires du système.
	 */
	public static final String SYSTEM_TMP_DIR = System.getProperty("java.io.tmpdir");

	/**
	 * Séparateur de fichier du système.
	 */
	public static final String SYSTEM_FILE_SEPARATOR = System.getProperty("file.separator");

	/**
	 * Caractère pour délimiter les tags référencant des utilisateurs.
	 */
	public static final String USER_TAG_DELIMITER = "@";

	/**
	 * Caractère pour délimiter les tags référencant des mots-clés.
	 */
	public static final String WORD_TAG_DELIMITER = "#";

	/**
	 * Identifiant de l'utilisateur inconnu.
	 */
	public static final UUID UNKNONWN_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

	public static final UUID GEMINI_FLASH_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");

	/**
	 * Fichier de configuration de l'application.
	 */
	public static final String CONFIGURATION_FILE = "configuration.properties";

	/**
	 * Clé de configuration pour la sauvegarde du répertoire d'échange.
	 */
	public static final String CONFIGURATION_KEY_EXCHANGE_DIRECTORY = "EXCHANGE_DIRECTORY";

	public static final String MESSAGES = "messages";
}
