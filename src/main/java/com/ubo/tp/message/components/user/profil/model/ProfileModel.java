package com.ubo.tp.message.components.user.profil.model;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.SessionManager;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Modèle gérant les données du profil utilisateur.
 */
public class ProfileModel {
  /**
   * Base de données.
   */
  protected final IDatabase database;

  /**
   * Gestionnaire de session.
   */
  protected final SessionManager sessionManager;

  /**
   * Constructeur.
   */
  public ProfileModel(IDatabase database, SessionManager sessionManager) {
    this.database = database;
    this.sessionManager = sessionManager;
  }

  /**
   * Récupère l'utilisateur connecté.
   * @return L'utilisateur connecté ou null si aucun utilisateur n'est connecté.
   */
  public User getConnectedUser() {
    return sessionManager.getConnectedUser();
  }

  /**
   * Récupère les messages de l'utilisateur connecté.
   * @return L'ensemble des messages de l'utilisateur connecté.
   */
  public Set<Message> getUserMessages() {
    User connectedUser = getConnectedUser();
    if (connectedUser != null) {
      return database.getUserMessages(connectedUser);
    }
    return Set.of(); // Ensemble vide si aucun utilisateur n'est connecté
  }

  /**
   * Récupère le nombre de followers de l'utilisateur connecté.
   * @return Le nombre de followers.
   */
  public int getFollowersCount() {
    User connectedUser = getConnectedUser();
    if (connectedUser != null) {
      return database.getFollowersCount(connectedUser);
    }
    return 0;
  }

  /**
   * Récupère les utilisateurs qui suivent l'utilisateur connecté.
   * @return L'ensemble des followers.
   */
  public Set<User> getFollowers() {
    User connectedUser = getConnectedUser();
    if (connectedUser != null) {
      return database.getFollowers(connectedUser);
    }
    return Set.of(); // Ensemble vide si aucun utilisateur n'est connecté
  }

  /**
   * Récupère les utilisateurs que suit l'utilisateur connecté.
   * @return L'ensemble des utilisateurs suivis.
   */
  public Set<User> getFollowed() {
    User connectedUser = getConnectedUser();
    if (connectedUser != null) {
      Set<String> followedTags = connectedUser.getFollows();
      return database.getUsers().stream()
        .filter(user -> followedTags.contains(user.getUserTag()))
        .collect(Collectors.toSet());
    }
    return Set.of(); // Ensemble vide si aucun utilisateur n'est connecté
  }

  /**
   * Commence à suivre un utilisateur.
   * @param userTag Le tag de l'utilisateur à suivre.
   * @return true si l'opération a réussi, false sinon.
   */
  public boolean followUser(String userTag) {
    User connectedUser = getConnectedUser();
    if (connectedUser != null) {
      connectedUser.addFollowing(userTag);
      database.modifiyUser(connectedUser);
      return true;
    }
    return false;
  }

  /**
   * Cesse de suivre un utilisateur.
   * @param userTag Le tag de l'utilisateur à ne plus suivre.
   * @return true si l'opération a réussi, false sinon.
   */
  public boolean unfollowUser(String userTag) {
    User connectedUser = getConnectedUser();
    if (connectedUser != null) {
      connectedUser.removeFollowing(userTag);
      database.modifiyUser(connectedUser);
      return true;
    }
    return false;
  }

  /**
   * Met à jour les informations de l'utilisateur connecté.
   * @param name Nouveau nom d'utilisateur.
   * @param password Nouveau mot de passe (peut être vide si pas de changement).
   * @return Vrai si la mise à jour a réussi, faux sinon.
   */
  public boolean updateUserInfo(String name, String password) {
    User connectedUser = getConnectedUser();
    if (connectedUser == null) {
      return false;
    }

    // Mise à jour du nom si fourni
    if (name != null && !name.isEmpty()) {
      connectedUser.setName(name);
    }

    // Mise à jour du mot de passe si fourni
    if (password != null && !password.isEmpty()) {
      connectedUser.setUserPassword(password);
    }

    // Notification de la base de données
    database.modifiyUser(connectedUser);
    return true;
  }
}