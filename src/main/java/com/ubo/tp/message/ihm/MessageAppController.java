package com.ubo.tp.message.ihm;

import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISessionObserver;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.ihm.components.LoginPanel;
import com.ubo.tp.message.ihm.components.RegisterPanel;
import com.ubo.tp.message.ihm.components.SearchUserPanel;
import com.ubo.tp.message.ihm.components.UserProfilePanel;
import com.ubo.tp.message.ihm.session.SessionManager;

import javax.swing.*;
import java.awt.*;

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
   * Panel de connexion
   */
  protected LoginPanel mLoginPanel;

  /**
   * Panel d'inscription
   */
  protected RegisterPanel mRegisterPanel;

  /**
   * Panel de profil utilisateur
   */
  protected UserProfilePanel mUserProfilePanel;

  /**
   * Panel de recherche d'utilisateurs
   */
  protected SearchUserPanel mSearchUserPanel;

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

    // Créer les différentes vues
    mLoginPanel = new LoginPanel(mDatabase, mSessionManager);
    mRegisterPanel = new RegisterPanel(mDatabase, mEntityManager, mSessionManager);

    // Connecter les boutons de navigation
    mLoginPanel.getRegisterButton().addActionListener(e -> showRegisterView());
    mRegisterPanel.getLoginButton().addActionListener(e -> showLoginView());

    // Ajouter les vues au panneau de contenu
    mContentPanel.add(mLoginPanel, "login");
    mContentPanel.add(mRegisterPanel, "register");
  }

  /**
   * Affiche la vue de connexion
   */
  public void showLoginView() {
    CardLayout cl = (CardLayout) mContentPanel.getLayout();
    cl.show(mContentPanel, "login");
  }

  /**
   * Affiche la vue d'inscription
   */
  public void showRegisterView() {
    CardLayout cl = (CardLayout) mContentPanel.getLayout();
    cl.show(mContentPanel, "register");
  }

  /**
   * Affiche la vue de profil utilisateur
   */
  public void showUserProfileView() {
    // Vérifier si l'utilisateur est connecté
    if (!mSessionManager.isUserConnected()) {
      return;
    }

    // Créer le panel de profil s'il n'existe pas
    if (mUserProfilePanel == null) {
      mUserProfilePanel = new UserProfilePanel(mDatabase, mSessionManager);
      mContentPanel.add(mUserProfilePanel, "profile");
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

    // Créer le panel de recherche s'il n'existe pas
    if (mSearchUserPanel == null) {
      mSearchUserPanel = new SearchUserPanel(mDatabase, mSessionManager);
      mContentPanel.add(mSearchUserPanel, "search");
    }

    // Afficher la recherche
    CardLayout cl = (CardLayout) mContentPanel.getLayout();
    cl.show(mContentPanel, "search");
  }

  @Override
  public void notifyLogin(User connectedUser) {
    // Mettre à jour le menu
    mMainView.updateMenuForConnectedUser(true);

    // Recréer le panel de profil pour s'assurer d'afficher les données à jour
    if (mUserProfilePanel != null) {
      mContentPanel.remove(mUserProfilePanel);
      mUserProfilePanel = null;
    }

    // Créer un nouveau panel avec l'utilisateur connecté
    mUserProfilePanel = new UserProfilePanel(mDatabase, mSessionManager, connectedUser);
    mContentPanel.add(mUserProfilePanel, "profile");

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