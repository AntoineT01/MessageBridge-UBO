package com.ubo.tp.message.components.user.search.model;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.SessionManager;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Modèle gérant les données pour la recherche d'utilisateurs.
 */
public class SearchModel {
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
   * @param database La base de données.
   * @param sessionManager Le gestionnaire de session.
   */
  public SearchModel(IDatabase database, SessionManager sessionManager) {
    this.database = database;
    this.sessionManager = sessionManager;
  }

  /**
   * Recherche des utilisateurs en fonction d'un critère.
   * @param criteria Le critère de recherche (nom ou tag).
   * @return L'ensemble des utilisateurs correspondant au critère.
   */
  public Set<User> searchUsers(String criteria) {
    Set<User> allUsers = database.getUsers();
    User currentUser = sessionManager.getConnectedUser();

    // Filtrer l'utilisateur courant des résultats
    Set<User> filteredUsers = allUsers.stream()
      .filter(user -> currentUser == null || !user.equals(currentUser))
      .collect(Collectors.toSet());

    // Si le critère est vide, retourner tous les utilisateurs
    if (criteria == null || criteria.trim().isEmpty()) {
      return filteredUsers;
    }

    // Filtrer en fonction du critère (nom ou tag)
    String lowerCriteria = criteria.toLowerCase();
    return filteredUsers.stream()
      .filter(user -> user.getName().toLowerCase().contains(lowerCriteria) ||
        user.getUserTag().toLowerCase().contains(lowerCriteria))
      .collect(Collectors.toSet());
  }

  /**
   * Vérifie si l'utilisateur courant suit l'utilisateur spécifié.
   * @param user L'utilisateur à vérifier.
   * @return true si l'utilisateur courant suit l'utilisateur spécifié, false sinon.
   */
  public boolean isFollowing(User user) {
    User currentUser = sessionManager.getConnectedUser();
    if (currentUser == null) {
      return false;
    }
    return currentUser.isFollowing(user);
  }

  /**
   * Suit ou cesse de suivre un utilisateur.
   * @param userToFollow L'utilisateur à suivre ou ne plus suivre.
   * @param follow true pour suivre, false pour ne plus suivre.
   * @return true si l'opération a réussi, false sinon.
   */
  public boolean toggleFollow(User userToFollow, boolean follow) {
    User currentUser = sessionManager.getConnectedUser();
    if (currentUser == null) {
      return false;
    }

    if (follow) {
      currentUser.addFollowing(userToFollow.getUserTag());
    } else {
      currentUser.removeFollowing(userToFollow.getUserTag());
    }

    // Notification de mise à jour de l'utilisateur
    database.modifiyUser(currentUser);
    return true;
  }

  /**
   * Récupère l'utilisateur connecté.
   * @return L'utilisateur connecté ou null si aucun utilisateur n'est connecté.
   */
  public User getConnectedUser() {
    return sessionManager.getConnectedUser();
  }

  public int getFollowersCount(User user) {
    return database.getFollowersCount(user);

  }
}