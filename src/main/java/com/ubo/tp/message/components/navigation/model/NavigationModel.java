package com.ubo.tp.message.components.navigation.model;

import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.ISessionObserver;
import com.ubo.tp.message.core.session.SessionManager;

/**
 * Modèle de données pour la navigation.
 * Gère l'état de connexion et les informations de l'utilisateur pour la navigation.
 */
public class NavigationModel implements ISessionObserver {

  /**
   * Indique si un utilisateur est connecté.
   */
  private boolean userConnected;

  /**
   * L'utilisateur connecté.
   */
  private User connectedUser;

  /**
   * Le gestionnaire de session.
   */
  private final SessionManager sessionManager;

  /**
   * Constructeur.
   * @param sessionManager Le gestionnaire de session.
   */
  public NavigationModel(SessionManager sessionManager) {
    this.sessionManager = sessionManager;
    this.userConnected = sessionManager.isUserConnected();
    this.connectedUser = sessionManager.getConnectedUser();

    // S'abonner aux événements de session
    sessionManager.addSessionObserver(this);
  }

  /**
   * Vérifie si un utilisateur est connecté.
   * @return true si un utilisateur est connecté, false sinon.
   */
  public boolean isUserConnected() {
    return userConnected;
  }

  /**
   * Récupère l'utilisateur connecté.
   * @return L'utilisateur connecté ou null si aucun utilisateur n'est connecté.
   */
  public User getConnectedUser() {
    return connectedUser;
  }

  /**
   * Récupère le nom de l'utilisateur connecté.
   * @return Le nom de l'utilisateur connecté ou une chaîne vide si aucun utilisateur n'est connecté.
   */
  public String getConnectedUserName() {
    return (connectedUser != null) ? connectedUser.getName() : "";
  }

  /**
   * Récupère le tag de l'utilisateur connecté.
   * @return Le tag de l'utilisateur connecté ou une chaîne vide si aucun utilisateur n'est connecté.
   */
  public String getConnectedUserTag() {
    return (connectedUser != null) ? connectedUser.getUserTag() : "";
  }

  /**
   * Déconnecte l'utilisateur actuel.
   */
  public void logout() {
    sessionManager.logout();
  }

  @Override
  public void notifyLogin(User connectedUser) {
    this.userConnected = true;
    this.connectedUser = connectedUser;
  }

  @Override
  public void notifyLogout() {
    this.userConnected = false;
    this.connectedUser = null;
  }
}