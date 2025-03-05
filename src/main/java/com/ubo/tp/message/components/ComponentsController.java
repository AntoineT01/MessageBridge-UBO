package com.ubo.tp.message.components;

import com.ubo.tp.message.app.MessageAppView;
import com.ubo.tp.message.components.message.controller.MessageController;
import com.ubo.tp.message.components.message.service.IMessageService;
import com.ubo.tp.message.components.message.service.MessageService;
import com.ubo.tp.message.components.message.view.MessagePanel;
import com.ubo.tp.message.components.navigation.NavigationComponent;
import com.ubo.tp.message.components.user.auth.AuthComponent;
import com.ubo.tp.message.components.user.auth.IAuthComponent;
import com.ubo.tp.message.components.user.profil.IProfileComponent;
import com.ubo.tp.message.components.user.profil.ProfileComponent;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.ISessionObserver;
import com.ubo.tp.message.core.session.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Contrôleur principal de l'application
 * Gère les transitions entre les différentes vues et les composants
 * Version modifiée pour utiliser la navigation avec JMenus
 */
public class ComponentsController implements ISessionObserver {
  /**
   * Vue principale de l'application
   */
  protected MessageAppView mainView;

  /**
   * Panneau de contenu principal
   */
  protected JPanel contentPanel;

  /**
   * Gestionnaire de session
   */
  protected SessionManager sessionManager;

  /**
   * Base de données
   */
  protected IDatabase database;

  /**
   * Gestionnaire d'entités
   */
  protected EntityManager entityManager;

  /**
   * Composant d'authentification
   */
  protected IAuthComponent authComponent;

  /**
   * Composant de profil
   */
  protected IProfileComponent profileComponent;

  /**
   * Composant de navigation
   */
  protected NavigationComponent navigationComponent;

  /**
   * Panel de messages
   */
  protected MessagePanel messagePanel;

  /**
   * Contrôleur de messages
   */
  protected MessageController messageController;

  /**
   * Constructeur
   */
  public ComponentsController(MessageAppView mainView, IDatabase database, EntityManager entityManager) {
    this.mainView = mainView;
    this.database = database;
    this.entityManager = entityManager;

    // Créer le gestionnaire de session
    this.sessionManager = new SessionManager();

    // S'enregistrer comme observateur de session
    this.sessionManager.addSessionObserver(this);

    // Initialiser l'interface
    this.initGUI();

    // Configurer les événements pour la vue principale
    this.setupViewEvents();

    // Afficher la vue de connexion par défaut
    this.showLoginView();
  }

  /**
   * Initialisation de l'interface graphique
   */
  protected void initGUI() {
    // Initialiser le panneau de contenu principal avec CardLayout
    contentPanel = new JPanel(new CardLayout());
    mainView.setContentPanel(contentPanel);

    // Créer le composant d'authentification
    authComponent = new AuthComponent(database, entityManager, sessionManager);

    // Créer le composant de profil
    profileComponent = new ProfileComponent(database, entityManager, sessionManager);

    // Créer le composant de navigation
    navigationComponent = new NavigationComponent(sessionManager);
    navigationComponent.setMainView(mainView); // Important pour mettre à jour les menus

    // Créer le service et le panneau de messages
    IMessageService messageService = new MessageService(database);
    messagePanel = new MessagePanel();
    messageController = new MessageController(messageService, sessionManager.getSession(), messagePanel);

    // Configurer les actions pour le composant d'authentification
    authComponent.setAuthSuccessListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Cette méthode est appelée quand l'authentification réussit
        showUserProfileView();
      }
    });

    // Configurer les actions pour le composant de navigation
    setupNavigationActions();

    // Panneau central avec CardLayout pour les différentes vues
    JPanel centerPanel = new JPanel(new CardLayout());
    centerPanel.add(profileComponent.getComponent(), "profile");

    // Créer un panneau pour afficher les messages
    JPanel messagesContainer = new JPanel(new BorderLayout());
    messagesContainer.add(messagePanel, BorderLayout.CENTER);
    centerPanel.add(messagesContainer, "messages");

    // Ajouter les vues au panneau de contenu
    contentPanel.add(authComponent.getComponent(), "auth");
    contentPanel.add(centerPanel, "main");
  }

  /**
   * Configure les actions pour le composant de navigation
   */
  private void setupNavigationActions() {
    // Action pour afficher le profil
    navigationComponent.setProfileActionListener(e -> showUserProfileView());

    // Action pour afficher les messages
    navigationComponent.setMessagesActionListener(e -> showMessagesView());

    // Action pour la recherche
    navigationComponent.setSearchActionListener(e -> showSearchUserView());

    // Action pour la déconnexion
    navigationComponent.setLogoutActionListener(e -> sessionManager.logout());

    // Action pour la connexion
    navigationComponent.setLoginActionListener(e -> showLoginView());

    // Action pour l'inscription
    navigationComponent.setRegisterActionListener(e -> showRegisterView());

    // Action pour "À propos"
    navigationComponent.setAboutActionListener(e -> mainView.showAboutDialog());

    // Action pour changer le répertoire
//    navigationComponent.setChangeDirectoryActionListener(e ->
//                                                           mainView.firePropertyChange("ACTION_CHANGE_DIRECTORY", false, true));
  }

  /**
   * Configure les événements pour la vue principale
   */
  public void setupViewEvents() {
    // Configurer les événements pour le contrôleur
    mainView.addPropertyChangeListener("ACTION_SHOW_PROFILE", evt -> {
      showUserProfileView();
    });

    mainView.addPropertyChangeListener("ACTION_SEARCH_USERS", evt -> {
      showSearchUserView();
    });

    mainView.addPropertyChangeListener("ACTION_LOGOUT", evt -> {
      sessionManager.logout();
    });
  }

  /**
   * Affiche la vue de connexion
   */
  public void showLoginView() {
    authComponent.showLoginView();
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    cl.show(contentPanel, "auth");

    // Afficher la vue de navigation déconnectée
    navigationComponent.showDisconnectedView();
  }

  /**
   * Affiche la vue d'inscription
   */
  public void showRegisterView() {
    authComponent.showRegisterView();
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    cl.show(contentPanel, "auth");

    // Afficher la vue de navigation déconnectée
    navigationComponent.showDisconnectedView();
  }

  /**
   * Affiche la vue de profil utilisateur
   */
  public void showUserProfileView() {
    // Vérifier si l'utilisateur est connecté
    if (!sessionManager.isUserConnected()) {
      return;
    }

    // Rafraîchir les données du profil
    profileComponent.refreshProfileData();

    // Afficher la vue principale
    CardLayout mainCl = (CardLayout) contentPanel.getLayout();
    mainCl.show(contentPanel, "main");

    // Afficher le profil dans le panneau central
    JPanel centerPanel = (JPanel) contentPanel.getComponent(1);
    CardLayout centerCl = (CardLayout) centerPanel.getLayout();
    centerCl.show(centerPanel, "profile");
  }

  /**
   * Affiche la vue de messages
   */
  public void showMessagesView() {
    // Vérifier si l'utilisateur est connecté
    if (!sessionManager.isUserConnected()) {
      return;
    }

    // Afficher la vue principale
    CardLayout mainCl = (CardLayout) contentPanel.getLayout();
    mainCl.show(contentPanel, "main");

    // Afficher les messages dans le panneau central
    JPanel centerPanel = (JPanel) contentPanel.getComponent(1);
    CardLayout centerCl = (CardLayout) centerPanel.getLayout();
    centerCl.show(centerPanel, "messages");
  }

  /**
   * Affiche la vue de recherche d'utilisateurs
   */
  public void showSearchUserView() {
    // Vérifier si l'utilisateur est connecté
    if (!sessionManager.isUserConnected()) {
      return;
    }

    // Pour l'instant, montrons la vue des messages
    // puisque la recherche d'utilisateurs n'est pas encore implémentée
    showMessagesView();
  }

  /**
   * Notification de connexion réussie
   */
  @Override
  public void notifyLogin(User connectedUser) {
    // Afficher la vue de navigation connectée
    navigationComponent.showConnectedView();

    // Afficher le profil
    showUserProfileView();
  }

  /**
   * Notification de déconnexion
   */
  @Override
  public void notifyLogout() {
    // Afficher la vue de navigation déconnectée
    navigationComponent.showDisconnectedView();

    // Revenir à la vue de connexion
    showLoginView();
  }

  /**
   * Récupère le gestionnaire de session
   */
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}