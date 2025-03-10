package com.ubo.tp.message.core.datamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Classe du modèle représentant un utilisateur.
 *
 * @author S.Lucas
 */
public class User {
	/**
	 * Identifiant unique de l'utilisateur.
	 */
	protected final UUID mUuid;

	/**
	 * Tag non modifiable correspondant à l'utilisateur. <br/>
	 * <i>Doit être unique dans le système</i>
	 */
	protected final String mUserTag;

	/**
	 * Mot de passe de l'utilisateur.
	 */
	protected String mUserPassword;

	/**
	 * Nom de l'utilisateur.
	 */
	protected String mName;

	/**
	 * Liste des tags suivis par l'utilisateur.
	 */
	protected final Set<String> mFollows;

	/**
	 * Chemin d'accès à l'image de l'avatar de l'utilisateur.
	 */
	protected String mAvatarPath;

	// Nouvelle liste d’observateurs
	private final List<IUserObserver> observers = new ArrayList<>();

	public User(UUID uuid, String userTag, String userPassword, String name, Set<String> follows, String avatarPath) {
		mUuid = uuid;
		mUserTag = userTag;
		mUserPassword = userPassword;
		mName = name;
		mFollows = follows;
		mAvatarPath = avatarPath;
	}

	/**
	 * Retourne l'identifiant unique de l'utilisateur.
	 */
	public UUID getUuid() {
		return this.mUuid;
	}

	/**
	 * Retourne le nom de l'utilisateur.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Assigne le nom de l'utilisateur.
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.mName = name;
	}

	/**
	 * Retourne le tag correspondant à l'utilisateur.
	 */
	public String getUserTag() {
		return this.mUserTag;
	}

	/**
	 * Retourne le mot de passe de l'utilisateur.
	 */
	public String getUserPassword() {
		return this.mUserPassword;
	}

	/**
	 * Assigne le mot de passe de l'utilisateur.
	 *
	 * @param userPassword
	 */
	public void setUserPassword(String userPassword) {
		this.mUserPassword = userPassword;
	}

	/**
	 * Retourne la liste clonée des tag suivis par l'utilsateur.
	 */
	public Set<String> getFollows() {
		// Clonage pour éviter les modifications exterieures
		return new HashSet<>(this.mFollows);
	}

	/**
	 * Retire un tag de la liste des tags suivis.
	 *
	 * @param tagToRemove , tag à retirer.
	 */
	public void removeFollowing(String tagToRemove) {
		if(this.mFollows.remove(tagToRemove)) {
			notifyFollowChanged();
		}
	}

	public void addFollowing(String tagToFollow) {
		if(this.mFollows.add(tagToFollow)) {
			notifyFollowChanged();
		}
	}

	/**
	 * Retourne le chemin d'accès au fichier de l'avatar de l'utilisateur.
	 */
	public String getAvatarPath() {
		return this.mAvatarPath;
	}

	/**
	 * Assigne le chemin d'accès au fichier de l'avatar de l'utilisateur.
	 *
	 * @param avatarPath
	 */
	public void setAvatarPath(String avatarPath) {
		this.mAvatarPath = avatarPath;
	}

	/**
	 * Indique si l'utilisateur suit l'utilisateur donné.
	 */
	public boolean isFollowing(User userToCheck) {
		return this.getFollows().contains(userToCheck.getUserTag());
	}

	// Gestion des observateurs
	public void addObserver(IUserObserver observer) {
		if(!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	public void removeObserver(IUserObserver observer) {
		observers.remove(observer);
	}

	private void notifyFollowChanged() {
		for(IUserObserver observer : observers) {
			observer.followListChanged(this);
		}
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		if (this.mUuid != null) {
			hashCode = this.mUuid.hashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object other) {
		boolean equals = false;
		if (other instanceof User otherUser) {
			equals = this.getUuid().equals(otherUser.getUuid());
		}
		return equals;
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getName() + "] : " + this.getUuid() + " {@" + this.getUserTag() + " / " + this.getName() + "}";
	}
}
