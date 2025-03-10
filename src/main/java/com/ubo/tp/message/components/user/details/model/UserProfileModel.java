package com.ubo.tp.message.components.user.details.model;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.SessionManager;

import java.util.Set;

/**
 * Modèle pour la gestion des données du profil d'un utilisateur spécifique.
 */
public class UserProfileModel {
  /**
   * Base de données.
   */
  protected final IDatabase database;

  /**
   * Gestionnaire de session.
   */
  protected final SessionManager sessionManager;

  /**
   * Utilisateur courant affiché.
   */
  protected User displayedUser;

  /**
   * Constructeur.
   * @param database La base de données.
   * @param sessionManager Le gestionnaire de session.
   */
  public UserProfileModel(IDatabase database, SessionManager sessionManager) {
    this.database = database;
    this.sessionManager = sessionManager;
  }

  /**
   * Définit l'utilisateur à afficher.
   * @param user L'utilisateur à afficher.
   */
  public void setDisplayedUser(User user) {
    this.displayedUser = user;
  }

  /**
   * Récupère l'utilisateur affiché.
   * @return L'utilisateur affiché.
   */
  public User getDisplayedUser() {
    return displayedUser;
  }

  /**
   * Récupère l'utilisateur connecté.
   * @return L'utilisateur connecté ou null si aucun utilisateur n'est connecté.
   */
  public User getConnectedUser() {
    return sessionManager.getConnectedUser();
  }

  /**
   * Récupère les messages de l'utilisateur affiché.
   * @return L'ensemble des messages de l'utilisateur affiché.
   */
  public Set<Message> getUserMessages() {
    if (displayedUser != null) {
      return database.getUserMessages(displayedUser);
    }
    return Set.of(); // Ensemble vide si aucun utilisateur n'est affiché
  }

  /**
   * Récupère le nombre de followers de l'utilisateur affiché.
   * @return Le nombre de followers.
   */
  public int getFollowersCount() {
    if (displayedUser != null) {
      return database.getFollowersCount(displayedUser);
    }
    return 0;
  }

  /**
   * Récupère les utilisateurs qui suivent l'utilisateur affiché.
   * @return L'ensemble des followers.
   */
  public Set<User> getFollowers() {
    if (displayedUser != null) {
      return database.getFollowers(displayedUser);
    }
    return Set.of(); // Ensemble vide si aucun utilisateur n'est affiché
  }

  /**
   * Récupère les utilisateurs que suit l'utilisateur affiché.
   * @return L'ensemble des utilisateurs suivis.
   */
  public Set<User> getFollowed() {
    if (displayedUser != null) {
      Set<User> allUsers = database.getUsers();
      Set<String> followedTags = displayedUser.getFollows();

      // Filtrer les utilisateurs dont le tag est dans la liste des tags suivis
      return allUsers.stream()
        .filter(user -> followedTags.contains(user.getUserTag()))
        .collect(java.util.stream.Collectors.toSet());
    }
    return Set.of(); // Ensemble vide si aucun utilisateur n'est affiché
  }

  /**
   * Vérifie si l'utilisateur connecté suit l'utilisateur affiché.
   * @return true si l'utilisateur connecté suit l'utilisateur affiché, false sinon.
   */
  public boolean isFollowing() {
    User connectedUser = getConnectedUser();
    if (connectedUser != null && displayedUser != null) {
      return connectedUser.isFollowing(displayedUser);
    }
    return false;
  }

  /**
   * Suit ou cesse de suivre l'utilisateur affiché.
   * @param follow true pour suivre, false pour ne plus suivre.
   * @return true si l'opération a réussi, false sinon.
   */
  public boolean toggleFollow(boolean follow) {
    User connectedUser = getConnectedUser();
    if (connectedUser != null && displayedUser != null) {
      if (follow) {
        connectedUser.addFollowing(displayedUser.getUserTag());
      } else {
        connectedUser.removeFollowing(displayedUser.getUserTag());
      }

      // Notification de mise à jour de l'utilisateur
      database.modifiyUser(connectedUser);
      return true;
    }
    return false;
  }
}