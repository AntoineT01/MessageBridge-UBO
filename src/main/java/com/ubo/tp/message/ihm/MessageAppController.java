package com.ubo.tp.message.ihm;

import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISessionObserver;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.components.user.auth.AuthComponent;
import com.ubo.tp.message.components.user.auth.IAuthComponent;
import com.ubo.tp.message.core.session.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Contrôleur principal de l'application
 * Gère les transitions entre les différentes vues
 */
public class MessageAppController implements ISessionObserver {
  /**
   * Fenêtre principale de l'application
   */
  protected MessageAppMainView mMainView;

  /**
   * Panneau de contenu principal
   */
  protected JPanel mContentPanel;

  /**
   * Gestionnaire de session
   */
  protected SessionManager mSessionManager;

  /**
   * Base de données
   */
  protected IDatabase mDatabase;

  /**
   * Gestionnaire d'entités
   */
  protected EntityManager mEntityManager;

  /**
   * Composant d'authentification
   */
  protected IAuthComponent mAuthComponent;

  /**
   * Constructeur
   */
  public MessageAppController(MessageAppMainView mainView, IDatabase database, EntityManager entityManager) {
    this.mMainView = mainView;
    this.mDatabase = database;
    this.mEntityManager = entityManager;

    // Créer le gestionnaire de session
    this.mSessionManager = new SessionManager();

    // S'enregistrer comme observateur de session
    this.mSessionManager.addSessionObserver(this);

    // Initialiser l'interface
    this.initGUI();

    // Afficher la vue de connexion par défaut
    this.showLoginView();
  }

  /**
   * Initialisation de l'interface graphique
   */
  protected void initGUI() {
    // Récupérer le panneau de contenu de la fenêtre principale
    mContentPanel = new JPanel(new CardLayout());
    mMainView.setContentPanel(mContentPanel);

    // Créer le composant d'authentification
    mAuthComponent = new AuthComponent(mDatabase, mEntityManager, mSessionManager);

    // Configurer les actions pour le composant d'authentification
    mAuthComponent.setAuthSuccessListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Cette méthode est appelée quand l'authentification réussit
        showUserProfileView();
      }
    });

    // Ajouter les vues au panneau de contenu
    mContentPanel.add(mAuthComponent.getComponent(), "auth");
  }

  /**
   * Affiche la vue de connexion
   */
  public void showLoginView() {
    mAuthComponent.showLoginView();
    CardLayout cl = (CardLayout) mContentPanel.getLayout();
    cl.show(mContentPanel, "auth");
  }

  /**
   * Affiche la vue d'inscription
   */
  public void showRegisterView() {
    mAuthComponent.showRegisterView();
    CardLayout cl = (CardLayout) mContentPanel.getLayout();
    cl.show(mContentPanel, "auth");
  }

  /**
   * Affiche la vue de profil utilisateur
   */
  public void showUserProfileView() {
    // Vérifier si l'utilisateur est connecté
    if (!mSessionManager.isUserConnected()) {
      return;
    }

    // Afficher le profil
    CardLayout cl = (CardLayout) mContentPanel.getLayout();
    cl.show(mContentPanel, "profile");
  }

  /**
   * Affiche la vue de recherche d'utilisateurs
   */
  public void showSearchUserView() {
    // Vérifier si l'utilisateur est connecté
    if (!mSessionManager.isUserConnected()) {
      return;
    }

    // Afficher la recherche
    CardLayout cl = (CardLayout) mContentPanel.getLayout();
    cl.show(mContentPanel, "search");
  }

  @Override
  public void notifyLogin(User connectedUser) {
    // Mettre à jour le menu
    mMainView.updateMenuForConnectedUser(true);

    // Afficher le profil
    CardLayout cl = (CardLayout) mContentPanel.getLayout();
    cl.show(mContentPanel, "profile");
  }

  @Override
  public void notifyLogout() {
    // Mettre à jour le menu
    mMainView.updateMenuForConnectedUser(false);

    // Revenir à la vue de connexion
    showLoginView();
  }
}