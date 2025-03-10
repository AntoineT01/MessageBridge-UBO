package com.ubo.tp.message.components;

import com.ubo.tp.message.app.MessageAppView;
import com.ubo.tp.message.components.directory.controller.DirectoryController;
import com.ubo.tp.message.components.message.MessageComponent;
import com.ubo.tp.message.components.navigation.NavigationComponent;
import com.ubo.tp.message.components.user.auth.AuthComponent;
import com.ubo.tp.message.components.user.auth.IAuthComponent;
import com.ubo.tp.message.components.user.details.UserProfileComponent;
import com.ubo.tp.message.components.user.profil.IProfileComponent;
import com.ubo.tp.message.components.user.profil.ProfileComponent;
import com.ubo.tp.message.components.user.search.UserSearchComponent;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.ISessionObserver;
import com.ubo.tp.message.core.session.SessionManager;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Contrôleur principal de l'application
 * Gère les transitions entre les différentes vues et les composants
 * Version mise à jour pour intégrer la recherche et les profils utilisateurs
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
   * Panneau central avec CardLayout
   */
  protected JPanel centerPanel;

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
   * Composant de recherche d'utilisateurs
   */
  protected UserSearchComponent userSearchComponent;

  /**
   * Composant de profil utilisateur détaillé
   */
  protected UserProfileComponent userProfileComponent;

  /**
   * Panel de messages
   */
  protected MessageComponent messageComponent;

  /**
   * Contrôleur de répertoire
   */
  protected DirectoryController directoryController;

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
   * Configure le contrôleur de répertoire
   */
  public void setDirectoryController(DirectoryController directoryController) {
    this.directoryController = directoryController;

    // Connecter le contrôleur de répertoire au composant de navigation
    if (navigationComponent != null) {
      navigationComponent.setDirectoryController(directoryController);
    }
  }

  /**
   * Initialisation de l'interface graphique
   */
  protected void initGUI() {
    // Initialisation du panneau de contenu principal avec CardLayout
    contentPanel = new JPanel(new CardLayout());
    mainView.setContentPanel(contentPanel);

    // Création des composants d'authentification et de profil
    authComponent = new AuthComponent(database, entityManager, sessionManager);
    profileComponent = new ProfileComponent(database, entityManager, sessionManager);

    // Création du composant de navigation
    navigationComponent = new NavigationComponent(sessionManager);
    navigationComponent.setMainFrame(mainView);

    // ***** Modification ici : on stocke le MessageComponent dans un champ *****
    messageComponent = new MessageComponent(database, sessionManager.getSession(), entityManager);
    // Créer le composant de recherche d'utilisateurs
    userSearchComponent = new UserSearchComponent(database, sessionManager, entityManager);

    // Créer le composant de profil utilisateur détaillé
    userProfileComponent = new UserProfileComponent(database, sessionManager, entityManager);

    // Création du panneau principal pour le contenu (profil et messages)
    // Configurer les actions pour le composant d'authentification
    authComponent.setAuthSuccessListener(_ -> showUserProfileView());

    // Configurer les actions pour le composant de navigation
    setupNavigationActions();

    // Configurer les actions pour le composant de recherche d'utilisateurs
    setupUserSearchActions();

    // Créer un panneau pour le contenu principal
    JPanel mainContentPanel = new JPanel(new BorderLayout());

    // Panneau central avec CardLayout pour les différentes vues
    centerPanel = new JPanel(new CardLayout());
    centerPanel.add(profileComponent.getComponent(), "profile");

    // Panneau pour afficher les messages
    JPanel messagesContainer = new JPanel(new BorderLayout());
    messagesContainer.add(messageComponent.getMessagePanel(), BorderLayout.CENTER);
    centerPanel.add(messagesContainer, "messages");
    centerPanel.add(messageComponent.getMessagePanel(), "messages");
    centerPanel.add(userSearchComponent.getComponent(), "user_search");
    centerPanel.add(userProfileComponent.getComponent(), "user_profile");

    mainContentPanel.add(centerPanel, BorderLayout.CENTER);

    // Ajout des vues au panneau de contenu
    contentPanel.add(authComponent.getComponent(), "auth");
    contentPanel.add(mainContentPanel, "main");
  }

  /**
   * Configure les actions pour le composant de navigation
   */
  private void setupNavigationActions() {
    // Action pour afficher le profil
    navigationComponent.setProfileActionListener(_ -> showUserProfileView());

    // Action pour afficher les messages
    navigationComponent.setMessagesActionListener(_ -> showMessagesView());

    // Action pour la recherche
    navigationComponent.setSearchActionListener(_ -> showSearchUserView());

    // Action pour la déconnexion
    navigationComponent.setLogoutActionListener(_ -> sessionManager.logout());

    // Action pour la connexion
    navigationComponent.setLoginActionListener(_ -> showLoginView());

    // Action pour l'inscription
    navigationComponent.setRegisterActionListener(_ -> showRegisterView());

    // Action pour "À propos"
    navigationComponent.setAboutActionListener(_ -> mainView.showAboutDialog());
  }

  /**
   * Configure les actions pour le composant de recherche d'utilisateurs
   */
  private void setupUserSearchActions() {
    // Action pour afficher le profil d'un utilisateur
    userSearchComponent.setViewProfileListener(e -> {
      // L'événement contient l'utilisateur à afficher
      if (e.getSource() instanceof User) {
        User userToDisplay = (User) e.getSource();
        showUserProfileView(userToDisplay);
      }
    });

    // Action pour revenir à la liste des utilisateurs
    userProfileComponent.setBackToListListener(e -> showSearchUserView());
  }

  /**
   * Configure les événements pour la vue principale
   */
  public void setupViewEvents() {
    // Configurer l'action de sortie
    mainView.setExitListener(_ -> {
      // Demander confirmation à l'utilisateur
      int response = JOptionPane.showConfirmDialog(
        mainView,
        "Voulez-vous vraiment quitter l'application ?",
        "Confirmation",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
      );

      if (response == JOptionPane.YES_OPTION) {
        // Arrêter proprement l'application
        if (directoryController != null) {
          directoryController.shutdown();
        }
        System.exit(0);
      }
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
    CardLayout centerCl = (CardLayout) centerPanel.getLayout();
    centerCl.show(centerPanel, "profile");
  }

  /**
   * Affiche le profil d'un utilisateur spécifique
   * @param user L'utilisateur dont le profil doit être affiché
   */
  public void showUserProfileView(User user) {
    // Vérifier si l'utilisateur est connecté
    if (!sessionManager.isUserConnected()) {
      return;
    }

    // Définir l'utilisateur à afficher
    userProfileComponent.setUserToDisplay(user);

    // Afficher la vue principale
    CardLayout mainCl = (CardLayout) contentPanel.getLayout();
    mainCl.show(contentPanel, "main");

    // Afficher le profil utilisateur détaillé dans le panneau central
    CardLayout centerCl = (CardLayout) centerPanel.getLayout();
    centerCl.show(centerPanel, "user_profile");
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

    // Afficher la vue principale
    CardLayout mainCl = (CardLayout) contentPanel.getLayout();
    mainCl.show(contentPanel, "main");

    // Afficher la recherche d'utilisateurs dans le panneau central
    CardLayout centerCl = (CardLayout) centerPanel.getLayout();
    centerCl.show(centerPanel, "user_search");
  }

  /**
   * Notification de connexion réussie
   */
  @Override
  public void notifyLogin(User connectedUser) {
    // Afficher la vue de navigation connectée et le profil
    navigationComponent.showConnectedView();
    showUserProfileView();

    // Forcer le rafraîchissement des messages filtrés
    // Ici, on suppose que le MessageComponent détient une référence vers le MessageModel
    if (messageComponent != null) {
      // Dans votre MessageComponent, vous pouvez exposer le modèle pour rafraîchir :
      messageComponent.getMessageModel().refresh();
      // Ou, si vous avez une méthode refreshMessages() qui recrée l'affichage à partir de getMessages(),
      // vous l'appelez ici.
      messageComponent.refreshMessages();
    }
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

  /**
   * Récupère le composant de navigation
   */
  public NavigationComponent getNavigationComponent() {
    return navigationComponent;
  }
}