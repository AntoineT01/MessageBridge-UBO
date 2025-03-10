package com.ubo.tp.message.core.session;

import com.ubo.tp.message.core.datamodel.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Gestionnaire de session pour l'interface graphique
 * Fait le lien entre la session métier et les composants graphiques
 */
public class SessionManager implements ISessionObserver {
  /**
   * Session métier
   */
  protected final ISession mSession;

  /**
   * Observateurs de la session GUI
   */
  protected final Set<ISessionObserver> mObservers;

  /**
   * Constructeur
   */
  public SessionManager() {
    this.mSession = new Session();
    this.mObservers = new HashSet<>();

    // S'enregistrer comme observateur de la session métier
    this.mSession.addObserver(this);
  }

  /**
   * Ajoute un observateur à la session GUI
   */
  public void addSessionObserver(ISessionObserver observer) {
    this.mObservers.add(observer);

    // Si un utilisateur est déjà connecté, notifier le nouvel observateur
    if (this.mSession.getConnectedUser() != null) {
      observer.notifyLogin(this.mSession.getConnectedUser());
    }
  }

  /**
   * Retire un observateur de la session GUI
   */
  public void removeSessionObserver(ISessionObserver observer) {
    this.mObservers.remove(observer);
  }

  /**
   * Connecte un utilisateur
   */
  public void login(User user) {
    this.mSession.connect(user);
  }

  /**
   * Déconnecte l'utilisateur actuel
   */
  public void logout() {
    this.mSession.disconnect();
  }

  /**
   * Retourne l'utilisateur connecté
   */
  public User getConnectedUser() {
    return this.mSession.getConnectedUser();
  }

  /**
   * Vérifie si un utilisateur est connecté
   */
  public boolean isUserConnected() {
    return this.mSession.getConnectedUser() != null;
  }

  /**
   * Retourne l'objet de session
   * @return L'objet ISession sous-jacent
   */
  public ISession getSession() {
    return this.mSession;
  }

  @Override
  public void notifyLogin(User connectedUser) {
    // Quand la session métier notifie une connexion, propager à tous les observateurs GUI
    for (ISessionObserver observer : this.mObservers) {
      observer.notifyLogin(connectedUser);
    }
  }

  @Override
  public void notifyLogout() {
    // Quand la session métier notifie une déconnexion, propager à tous les observateurs GUI
    for (ISessionObserver observer : this.mObservers) {
      observer.notifyLogout();
    }
  }
}