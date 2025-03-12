package com.ubo.tp.message.common.utils;

import com.ubo.tp.message.common.constants.Constants;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import static com.ubo.tp.message.common.utils.PropertiesManager.loadProperties;
import static com.ubo.tp.message.common.utils.PropertiesManager.writeProperties;

/**
 * Classe d'utilitaires pour les fichiers de données
 */
public class DataFilesManager {

	public static final User UNKNOWN_USER = new User(Constants.UNKNONWN_USER_UUID, "<Inconnu>", "--", "<Inconnu>",
	                                                 new HashSet<>(), "");

	public static final User CHATBOT_USER = new User(Constants.GEMINI_FLASH_UUID, "@gemini", "test", "<Gemini>",
	                                                 new HashSet<>(), "");

	// Clé de l'UUID d'un user
	public static final String PROPERTY_KEY_USER_UUID = "UUID";

	// Clé du tag d'un user
	public static final String PROPERTY_KEY_USER_TAG = "Tag";

	// Clé du mot de passe d'un user
	public static final String PROPERTY_KEY_USER_PASSWORD = "This_is_not_the_password";

	// Clé du nom d'un user
	public static final String PROPERTY_KEY_USER_NAME = "Name";

	// Clé de la liste des abonnements d'un user
	public static final String PROPERTY_KEY_USER_FOLLOWS = "Follows";

	// Clé de l'avatar d'un user
	public static final String PROPERTY_KEY_USER_AVATAR = "Avatar";

	// Séparateur des abonnements dans les properties
	public static final String PROPERTY_FOLLOW_SEPARATOR = ";";

	// Clé de l'UUID d'un message
	public static final String PROPERTY_KEY_MESSAGE_UUID = "UUID";

	// Clé de l'émetteur d'un message
	public static final String PROPERTY_KEY_MESSAGE_SENDER = "Sender";

	// Clé de la date d'émission d'un message
	public static final String PROPERTY_KEY_MESSAGE_DATE = "Date";

	// Clé du texte d'un message
	public static final String PROPERTY_KEY_MESSAGE_TEXT = "Text";

	// Clé pour les pièces jointes d'un message
	public static final String PROPERTY_KEY_MESSAGE_ATTACHMENTS = "Attachments";

	// Séparateur des pièces jointes dans les properties
	public static final String PROPERTY_ATTACHMENT_SEPARATOR = "|";

	/**
	 * Charge un utilisateur depuis un fichier
	 */
	public static User readUser(File userFile) {
		User user = null;

		if (userFile != null && userFile.getName().endsWith(Constants.USER_FILE_EXTENSION) && userFile.exists()) {
			Properties properties = loadProperties(userFile.getAbsolutePath());

			String uuid = properties.getProperty(PROPERTY_KEY_USER_UUID, UUID.randomUUID().toString());
			String tag = properties.getProperty(PROPERTY_KEY_USER_TAG, "NoTag");
			String password = decrypt(properties.getProperty(PROPERTY_KEY_USER_PASSWORD, "NoPassword"));
			String name = properties.getProperty(PROPERTY_KEY_USER_NAME, "NoName");
			String follows = properties.getProperty(PROPERTY_KEY_USER_FOLLOWS, "");
			String avatar = properties.getProperty(PROPERTY_KEY_USER_AVATAR, "");

			String[] followsArray = follows.split(PROPERTY_FOLLOW_SEPARATOR);
			Set<String> followsAsSet = new HashSet<>();
			followsAsSet.addAll(Arrays.asList(followsArray));

			user = new User(UUID.fromString(uuid), tag, password, name, followsAsSet, avatar);
		}

		return user;
	}

	/**
	 * Écrit un fichier utilisateur
	 */
	public static void writeUserFile(User user, String destFileName) {
		Properties properties = new Properties();

		properties.setProperty(PROPERTY_KEY_USER_UUID, user.getUuid().toString());
		properties.setProperty(PROPERTY_KEY_USER_TAG, user.getUserTag());
		properties.setProperty(PROPERTY_KEY_USER_PASSWORD, encrypt(user.getUserPassword()));
		properties.setProperty(PROPERTY_KEY_USER_NAME, user.getName());
		StringBuilder follows = new StringBuilder();
		Set<String> followsAsSet = user.getFollows();
		for (String follow : followsAsSet) {
			follows.append(follow).append(PROPERTY_FOLLOW_SEPARATOR);
		}
		properties.setProperty(PROPERTY_KEY_USER_FOLLOWS, follows.toString());

		// Normaliser le chemin de l'avatar
		String avatarPath = user.getAvatarPath();
		if (avatarPath != null && !avatarPath.isEmpty()) {
			avatarPath = avatarPath.replace('\\', '/');
		}
		properties.setProperty(PROPERTY_KEY_USER_AVATAR, avatarPath);

		writeProperties(properties, destFileName);
	}

	/**
	 * Charge un message depuis un fichier
	 */
	public static Message readMessage(File messageFile, Map<UUID, User> userMap) {
		Message message = null;

		if (messageFile != null && messageFile.getName().endsWith(Constants.MESSAGE_FILE_EXTENSION)
			&& messageFile.exists()) {
			Properties properties = loadProperties(messageFile.getAbsolutePath());

			String uuid = properties.getProperty(PROPERTY_KEY_MESSAGE_UUID, UUID.randomUUID().toString());
			String senderUuid = properties.getProperty(PROPERTY_KEY_MESSAGE_SENDER,
			                                           Constants.UNKNONWN_USER_UUID.toString());
			String emissionDateStr = properties.getProperty(PROPERTY_KEY_MESSAGE_DATE, "0");
			String text = properties.getProperty(PROPERTY_KEY_MESSAGE_TEXT, "NoText");
			String attachmentsStr = properties.getProperty(PROPERTY_KEY_MESSAGE_ATTACHMENTS, "");

			User sender = getUserFromUuid(senderUuid, userMap);
			long emissionDate = Long.valueOf(emissionDateStr);

			// Traitement des pièces jointes avec normalisation des chemins
			List<String> attachments = new ArrayList<>();
			if (!attachmentsStr.isEmpty()) {
				String[] attachmentPaths = attachmentsStr.split(Pattern.quote(PROPERTY_ATTACHMENT_SEPARATOR));
				for (String path : attachmentPaths) {
					if (path != null && !path.isEmpty()) {
						// Normaliser le chemin
						attachments.add(path.replace('\\', '/'));
					}
				}
			}

			message = new Message(UUID.fromString(uuid), sender, emissionDate, text, attachments);
		}

		return message;
	}

	/**
	 * Écrit un fichier message
	 */
	public static void writeMessageFile(Message message, String destFileName) {
		Properties properties = new Properties();

		properties.setProperty(PROPERTY_KEY_MESSAGE_UUID, message.getUuid().toString());
		properties.setProperty(PROPERTY_KEY_MESSAGE_SENDER, message.getSender().getUuid().toString());
		properties.setProperty(PROPERTY_KEY_MESSAGE_DATE, String.valueOf(message.getEmissionDate()));
		properties.setProperty(PROPERTY_KEY_MESSAGE_TEXT, message.getText());

		// Enregistrement des pièces jointes avec normalisation des chemins
		if (message.hasAttachments()) {
			StringBuilder attachmentsStr = new StringBuilder();
			for (String attachment : message.getAttachments()) {
				if (attachment != null && !attachment.isEmpty()) {
					// Normaliser le chemin
					String normalizedPath = attachment.replace('\\', '/');
					attachmentsStr.append(normalizedPath).append(PROPERTY_ATTACHMENT_SEPARATOR);
				}
			}
			properties.setProperty(PROPERTY_KEY_MESSAGE_ATTACHMENTS, attachmentsStr.toString());
		}

		PropertiesManager.writeProperties(properties, destFileName);
	}

	/**
	 * Récupère un user en fonction de l'UUID
	 */
	protected static User getUserFromUuid(String uuid, Map<UUID, User> userMap) {
		// Recherche si l'utilisateur est présent dans la base
		User user = userMap.get(UUID.fromString(uuid));
		if (user == null) {
			user = userMap.get(Constants.UNKNONWN_USER_UUID);
		}

		return user;
	}

	public static String encrypt(String data) {
		return Base64.getEncoder().encodeToString(data.getBytes());
	}

	public static String decrypt(String encryptedData) {
		byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
		return new String(decodedBytes);
	}
}