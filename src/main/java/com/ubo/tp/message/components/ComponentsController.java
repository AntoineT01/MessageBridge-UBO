package com.ubo.tp.message.components;


import com.ubo.tp.message.app.MessageAppView;
import com.ubo.tp.message.components.directory.controller.DirectoryController;
import com.ubo.tp.message.components.message.MessageComponent;
import com.ubo.tp.message.components.navigation.NavigationComponent;
import com.ubo.tp.message.components.settings.notification.SettingsComponent;
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
import java.awt.Component;
import java.awt.event.ActionEvent;

import static com.ubo.tp.message.common.constants.Constants.MESSAGES;



public class ComponentsController implements ISessionObserver {
  // Vue principale
  protected MessageAppView mainView;

  // Panels
  protected JPanel contentPanel;
  protected JPanel centerPanel;

  // Gestionnaire de session
  protected SessionManager sessionManager;

  // Base de données
  protected IDatabase database;

  // Gestionnaire d'entités
  protected EntityManager entityManager;

  // Composants
  protected IAuthComponent authComponent;
  protected IProfileComponent profileComponent;
  protected NavigationComponent navigationComponent;
  protected UserSearchComponent userSearchComponent;
  protected UserProfileComponent userProfileComponent;
  protected MessageComponent messageComponent;
  protected SettingsComponent settingsComponent; // Nouveau composant de paramètres

  // Contrôleur de répertoire
  protected DirectoryController directoryController;


  // Constructeur
  public ComponentsController(MessageAppView mainView, IDatabase database, EntityManager entityManager) {
    this.mainView = mainView;
    this.database = database;
    this.entityManager = entityManager;

    // Initialiser le gestionnaire de session
    this.sessionManager = new SessionManager();

    // S'abonner aux notifications de session
    this.sessionManager.addSessionObserver(this);

    // Initialiser l'interface
    this.initGUI();

    // Configurer les événements de la vue
    this.setupViewEvents();

    // Afficher la vue de connexion
    this.showLoginView();
  }

  // Définir le contrôleur de répertoire
  public void setDirectoryController(DirectoryController directoryController) {
    this.directoryController = directoryController;

    // Propager au composant de navigation
    if (navigationComponent != null) {
      navigationComponent.setDirectoryController(directoryController);
    }
  }

  // Initialiser l'interface
  protected void initGUI() {
    // Initialiser le panel de contenu
    contentPanel = new JPanel(new CardLayout());
    mainView.setContentPanel(contentPanel);

    // Initialiser les composants
    authComponent = new AuthComponent(database, entityManager, sessionManager);
    profileComponent = new ProfileComponent(database, entityManager, sessionManager);

    // Initialiser le composant de navigation
    navigationComponent = new NavigationComponent(sessionManager);
    navigationComponent.setMainFrame(mainView);

    // Initialiser les composants de contenu
    messageComponent = new MessageComponent(database, sessionManager.getSession(), entityManager);
    userSearchComponent = new UserSearchComponent(database, sessionManager, entityManager);
    userProfileComponent = new UserProfileComponent(database, sessionManager, entityManager);
    settingsComponent = new SettingsComponent(); // Initialiser le composant de paramètres

    // Configurer les écouteurs d'événements
    authComponent.setAuthSuccessListener(_ -> showUserProfileView());

    // Configurer les actions de navigation
    setupNavigationActions();

    // Configurer les actions de recherche d'utilisateurs
    setupUserSearchActions();

    // Initialiser le panel de contenu principal
    JPanel mainContentPanel = new JPanel(new BorderLayout());

    // Initialiser le panel central
    centerPanel = new JPanel(new CardLayout());
    centerPanel.add((Component) profileComponent.getComponent(), "profile");

    // Panel de messages
    JPanel messagesContainer = new JPanel(new BorderLayout());
    messagesContainer.add((Component) messageComponent.getComponent(), BorderLayout.CENTER);
    centerPanel.add(messagesContainer, MESSAGES);
    centerPanel.add((Component) userSearchComponent.getComponent(), "user_search");
    centerPanel.add((Component) userProfileComponent.getComponent(), "user_profile");
    centerPanel.add((Component) settingsComponent.getComponent(), "settings"); // Ajouter le composant de paramètres

    mainContentPanel.add(centerPanel, BorderLayout.CENTER);

    // Ajouter les panels au panel de contenu
    contentPanel.add((Component) authComponent.getComponent(), "auth");
    contentPanel.add(mainContentPanel, "main");
  }

  // Configurer les actions de navigation
  private void setupNavigationActions() {
    // Action pour afficher le profil
    navigationComponent.setProfileActionListener(_ -> showUserProfileView());

    // Action pour afficher les messages
    navigationComponent.setMessagesActionListener(_ -> showMessagesView());

    // Action pour afficher la recherche d'utilisateurs
    navigationComponent.setSearchActionListener(_ -> { showSearchUserView(); userSearchComponent.initialize(); });

    // Action pour afficher les paramètres
    navigationComponent.setSettingsActionListener(_ -> showSettingsView());

    // Action pour se déconnecter
    navigationComponent.setLogoutActionListener(_ -> sessionManager.logout());

    // Action pour se connecter
    navigationComponent.setLoginActionListener(_ -> showLoginView());

    // Action pour s'inscrire
    navigationComponent.setRegisterActionListener(_ -> showRegisterView());

    // Action pour afficher "À propos"
    navigationComponent.setAboutActionListener(_ -> mainView.showAboutDialog());

    // Action pour quitter
    navigationComponent.setExitActionListener(_ -> {
      // Déléguer au gestionnaire de sortie de la vue principale
      if (mainView.getExitListener() != null) {
        mainView.getExitListener().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "exit"));
      }
    });
  }

  // Configurer les actions de recherche d'utilisateurs
  private void setupUserSearchActions() {
    // Action pour afficher le profil d'un utilisateur
    userSearchComponent.setViewProfileListener(e -> {
      // Vérifier que l'événement contient bien un utilisateur
      if (e.getSource() instanceof User userToDisplay) {
        showUserProfileView(userToDisplay);
      }
    });

    // Action pour revenir à la liste des utilisateurs
    userProfileComponent.setBackToListListener(_ -> showSearchUserView());
  }

  // Configurer les événements de la vue
  public void setupViewEvents() {
    // Configurer l'action de sortie
    mainView.setExitListener(_ -> {
      // Demander confirmation
      int response = JOptionPane.showConfirmDialog(
        mainView,
        "Voulez-vous vraiment quitter l'application ?",
        "Confirmation",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
      );

      if (response == JOptionPane.YES_OPTION) {
        // Arrêter le contrôleur de répertoire
        if (directoryController != null) {
          directoryController.shutdown();
        }
        System.exit(0);
      }
    });
  }

  // Afficher la vue de connexion
  public void showLoginView() {
    authComponent.showLoginView();
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    cl.show(contentPanel, "auth");

    // Mettre à jour la navigation
    navigationComponent.showDisconnectedView();
  }

  // Afficher la vue d'inscription
  public void showRegisterView() {
    authComponent.showRegisterView();
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    cl.show(contentPanel, "auth");

    // Mettre à jour la navigation
    navigationComponent.showDisconnectedView();
  }

  // Afficher la vue de profil de l'utilisateur connecté
  public void showUserProfileView() {
    // Vérifier qu'un utilisateur est connecté
    if (!sessionManager.isUserConnected()) {
      return;
    }

    // Rafraîchir les données du profil
    profileComponent.refreshProfileData();

    // Afficher le contenu principal
    CardLayout mainCl = (CardLayout) contentPanel.getLayout();
    mainCl.show(contentPanel, "main");

    // Afficher le profil
    CardLayout centerCl = (CardLayout) centerPanel.getLayout();
    centerCl.show(centerPanel, "profile");
  }

  // Afficher la vue de profil d'un utilisateur spécifique
  public void showUserProfileView(User user) {
    // Vérifier qu'un utilisateur est connecté
    if (!sessionManager.isUserConnected()) {
      return;
    }

    // Définir l'utilisateur à afficher
    userProfileComponent.setUserToDisplay(user);

    // Afficher le contenu principal
    CardLayout mainCl = (CardLayout) contentPanel.getLayout();
    mainCl.show(contentPanel, "main");

    // Afficher le profil de l'utilisateur
    CardLayout centerCl = (CardLayout) centerPanel.getLayout();
    centerCl.show(centerPanel, "user_profile");
  }

  // Afficher la vue de messages
  public void showMessagesView() {
    // Vérifier qu'un utilisateur est connecté
    if (!sessionManager.isUserConnected()) {
      return;
    }

    // Afficher le contenu principal
    CardLayout mainCl = (CardLayout) contentPanel.getLayout();
    mainCl.show(contentPanel, "main");

    // Afficher les messages
    CardLayout centerCl = (CardLayout) centerPanel.getLayout();
    centerCl.show(centerPanel, MESSAGES);
  }

  // Afficher la vue de recherche d'utilisateurs
  public void showSearchUserView() {
    // Vérifier qu'un utilisateur est connecté
    if (!sessionManager.isUserConnected()) {
      return;
    }

    // Afficher le contenu principal
    CardLayout mainCl = (CardLayout) contentPanel.getLayout();
    mainCl.show(contentPanel, "main");

    // Afficher la recherche d'utilisateurs
    CardLayout centerCl = (CardLayout) centerPanel.getLayout();
    centerCl.show(centerPanel, "user_search");
  }

  // Afficher la vue de paramètres
  public void showSettingsView() {
    // Vérifier qu'un utilisateur est connecté
    if (!sessionManager.isUserConnected()) {
      return;
    }

    // Afficher le contenu principal
    CardLayout mainCl = (CardLayout) contentPanel.getLayout();
    mainCl.show(contentPanel, "main");

    // Afficher les paramètres
    CardLayout centerCl = (CardLayout) centerPanel.getLayout();
    centerCl.show(centerPanel, "settings");
  }

  // Notification de connexion
  @Override
  public void notifyLogin(User connectedUser) {
    // Mettre à jour la navigation
    navigationComponent.showConnectedView();
    showUserProfileView();

    // Rafraîchir les messages si nécessaire
    if (messageComponent != null) {
      // Rafraîchir le modèle
      messageComponent.getMessageModel().refresh();
      // Rafraîchir l'affichage
      messageComponent.refreshMessages();
    }
  }

  // Notification de déconnexion
  @Override
  public void notifyLogout() {
    // Mettre à jour la navigation
    navigationComponent.showDisconnectedView();

    // Afficher la vue de connexion
    showLoginView();
  }

  // Obtenir le gestionnaire de session
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}