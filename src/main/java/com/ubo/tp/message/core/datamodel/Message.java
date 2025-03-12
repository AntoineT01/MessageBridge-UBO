package com.ubo.tp.message.core.datamodel;

import com.ubo.tp.message.common.constants.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe représentant un message, étendue pour prendre en charge les pièces jointes
 */
public class Message {
	// UUID du message
	protected final UUID mUuid;

	// Utilisateur expéditeur du message
	protected final User mSender;

	// Date d'émission du message
	protected final long mEmissionDate;

	// Texte du message
	protected final String mText;

	// Tags utilisateurs
	protected final Set<String> mUserTags;

	// Tags mots
	protected final Set<String> mTags;

	// Liste des pièces jointes (chemin vers les fichiers)
	protected final List<String> mAttachments;

	/**
	 * Constructeur.
	 */
	public Message(User sender, String text) {
		this(UUID.randomUUID(), sender, System.currentTimeMillis(), text, new ArrayList<>());
	}

	/**
	 * Constructeur avec pièces jointes.
	 */
	public Message(User sender, String text, List<String> attachments) {
		this(UUID.randomUUID(), sender, System.currentTimeMillis(), text, attachments);
	}

	/**
	 * Constructeur complet.
	 */
	public Message(UUID messageUuid, User sender, long emissionDate, String text) {
		this(messageUuid, sender, emissionDate, text, new ArrayList<>());
	}

	/**
	 * Constructeur complet avec pièces jointes.
	 */
	public Message(UUID messageUuid, User sender, long emissionDate, String text, List<String> attachments) {
		mUuid = messageUuid;
		mSender = sender;
		mEmissionDate = emissionDate;
		mText = text;
		mTags = new HashSet<>();
		mUserTags = new HashSet<>();

		// Normaliser les chemins des pièces jointes
		mAttachments = new ArrayList<>();
		if (attachments != null) {
			for (String attachment : attachments) {
				if (attachment != null && !attachment.isEmpty()) {
					// Normaliser le chemin (remplacer les antislashes par des slashes)
					mAttachments.add(attachment.replace('\\', '/'));
				}
			}
		}

		// Initialisation des tags
		this.initTags(mText);
	}

	/**
	 * Initialise les tags.
	 */
	protected void initTags(String text) {
		if (text != null) {
			// Initialisation des tags utilisateurs
			mUserTags.addAll(this.extractTags(text, Constants.USER_TAG_DELIMITER));

			// Initialisation des tags mots
			mTags.addAll(this.extractTags(text, Constants.WORD_TAG_DELIMITER));
		}
	}

	/**
	 * Extrait les tags en fonction du délimiteur.
	 */
	protected Set<String> extractTags(String text, String tagDelimiter) {
		Set<String> tags = new HashSet<>();
		// Extraction des tags
		String regex = Pattern.quote(tagDelimiter) + "(\\w+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			tags.add(matcher.group(1));
		}
		return tags;
	}

	/**
	 * Renvoie l'uuid du message.
	 */
	public UUID getUuid() {
		return mUuid;
	}

	/**
	 * Renvoie l'utilisateur expéditeur.
	 */
	public User getSender() {
		return mSender;
	}

	/**
	 * Renvoie le texte du message.
	 */
	public String getText() {
		return mText;
	}

	/**
	 * Renvoie la date d'émission du message.
	 */
	public long getEmissionDate() {
		return this.mEmissionDate;
	}

	/**
	 * Renvoie les tags du message.
	 */
	public Set<String> getTags() {
		return new HashSet<>(mTags);
	}

	/**
	 * Renvoie les tags utilisateurs.
	 */
	public Set<String> getUserTags() {
		return new HashSet<>(mUserTags);
	}

	/**
	 * Renvoie la liste des pièces jointes.
	 */
	public List<String> getAttachments() {
		// Retourner une copie de la liste pour éviter les modifications directes
		return new ArrayList<>(mAttachments);
	}

	/**
	 * Vérifie si le message contient des pièces jointes.
	 */
	public boolean hasAttachments() {
		return !mAttachments.isEmpty();
	}

	/**
	 * Vérifie si le message contient un tag.
	 */
	public boolean containsTag(String aTag) {
		return this.getTags().contains(aTag);
	}

	/**
	 * Vérifie si le message contient un tag utilisateur.
	 */
	public boolean containsUserTag(String anUserTag) {
		return this.getUserTags().contains(anUserTag);
	}

	/**
	 * Calcul du hash code.
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;

		if (this.mUuid != null) {
			hashCode = this.mUuid.hashCode();
		}

		return hashCode;
	}

	/**
	 * Equals.
	 */
	@Override
	public boolean equals(Object other) {
		boolean equals = false;

		if (other != null) {
			if (other instanceof Message) {
				Message othermessage = (Message) other;
				equals = (this.getUuid().equals(othermessage.getUuid()));
			}
		}

		return equals;
	}

	/**
	 * Représentation texte.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("[");
		sb.append(this.getClass().getName());
		sb.append("] : ");
		sb.append(this.getUuid());
		sb.append(" {");
		sb.append(this.getText());
		sb.append("}");
		if (hasAttachments()) {
			sb.append(" [Pièces jointes: ").append(mAttachments.size()).append("]");
		}

		return sb.toString();
	}
}