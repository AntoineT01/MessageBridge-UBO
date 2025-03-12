package com.ubo.tp.message.common.constants;

import java.util.UUID;

/**
 * Classe de constantes
 */
public class Constants {
	// Extension des fichiers utilisateurs
	public static final String USER_FILE_EXTENSION = "usr";

	// Extension des fichiers messages
	public static final String MESSAGE_FILE_EXTENSION = "msg";

	// Répertoire temporaire du système
	public static final String SYSTEM_TMP_DIR = System.getProperty("java.io.tmpdir");

	// Séparateur de fichier du système
	public static final String SYSTEM_FILE_SEPARATOR = System.getProperty("file.separator");

	// Délimiteur de tag utilisateur
	public static final String USER_TAG_DELIMITER = "@";

	// Délimiteur de tag mot
	public static final String WORD_TAG_DELIMITER = "#";

	// UUID de l'utilisateur inconnu
	public static final UUID UNKNONWN_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

	// UUID du chatbot Gemini Flash
	public static final UUID GEMINI_FLASH_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");

	// Nom du fichier de configuration
	public static final String CONFIGURATION_FILE = "configuration.properties";

	// Clé de configuration pour le répertoire d'échange
	public static final String CONFIGURATION_KEY_EXCHANGE_DIRECTORY = "EXCHANGE_DIRECTORY";

	// Nom du sous-répertoire pour les messages
	public static final String MESSAGES = "messages";

	// Nom du sous-répertoire pour les pièces jointes
	public static final String ATTACHMENTS = "attachments";

	// Extensions supportées pour les pièces jointes d'images
	public static final String[] SUPPORTED_IMAGE_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "bmp"};
}