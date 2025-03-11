package com.ubo.tp.message.common.utils;


import com.ubo.tp.message.app.MessageAppFxView;
import com.ubo.tp.message.components.directory.controller.DirectoryController;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.ISessionObserver;
import com.ubo.tp.message.core.session.SessionManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;


/**
 * Adaptateur pour utiliser les contrôleurs existants avec l'interface JavaFX.
 * Ce n'est pas un vrai contrôleur MVC, mais un pont entre l'UI JavaFX et les contrôleurs Swing.
 */
public class FxComponentsAdapter implements ISessionObserver {

  /**
   * Vue principale JavaFX.
   */
  private final MessageAppFxView mainView;

  /**
   * Base de données.
   */
  private final IDatabase database;

  /**
   * Gestionnaire d'entités.
   */
  private final EntityManager entityManager;

  /**
   * Contrôleur de répertoire.
   */
  private final DirectoryController directoryController;

  /**
   * Gestionnaire de session.
   */
  private final SessionManager sessionManager;

  /**
   * Vues JavaFX.
   */
  private final com.ubo.tp.message.fx.components.FxAuthView authView;
  private final com.ubo.tp.message.fx.components.FxProfileView profileView;
  private final com.ubo.tp.message.fx.components.FxMessageView messageView;

  /**
   * Constructeur.
   */
  public FxComponentsAdapter(MessageAppFxView mainView, IDatabase database,
                             EntityManager entityManager, DirectoryController directoryController) {
    this.mainView = mainView;
    this.database = database;
    this.entityManager = entityManager;
    this.directoryController = directoryController;

    // Création du gestionnaire de session (partagé)
    this.sessionManager = new SessionManager();
    this.sessionManager.addSessionObserver(this);

    // Création des vues JavaFX
    this.authView = new com.ubo.tp.message.fx.components.FxAuthView(database, entityManager, sessionManager);
    this.profileView = new com.ubo.tp.message.fx.components.FxProfileView(database, entityManager, sessionManager);
    this.messageView = new com.ubo.tp.message.fx.components.FxMessageView(database, sessionManager.getSession(), entityManager);

    // Configuration des actions
    setupEventHandlers();

    // Afficher la vue de connexion au démarrage
    showLoginView();
  }

  /**
   * Configure les gestionnaires d'événements entre les vues.
   */
  private void setupEventHandlers() {
    // Quand l'authentification réussit, on affiche le profil
    authView.setAuthSuccessListener(event -> showUserProfileView());

    // Navigation vers les messages depuis le profil
    profileView.setShowMessagesListener(event -> showMessagesView());

    // Navigation vers le profil depuis les messages
    messageView.setShowProfileListener(event -> showUserProfileView());
  }

  /**
   * Affiche la vue de connexion.
   */
  public void showLoginView() {
    authView.showLoginView();
    Platform.runLater(() -> mainView.setContent(authView));
  }

  /**
   * Affiche la vue d'inscription.
   */
  public void showRegisterView() {
    authView.showRegisterView();
    Platform.runLater(() -> mainView.setContent(authView));
  }

  /**
   * Affiche la vue de profil utilisateur.
   */
  public void showUserProfileView() {
    if (!sessionManager.isUserConnected()) {
      return;
    }

    profileView.refreshProfileData();
    Platform.runLater(() -> mainView.setContent(profileView));
  }

  /**
   * Affiche la vue des messages.
   */
  public void showMessagesView() {
    if (!sessionManager.isUserConnected()) {
      return;
    }

    messageView.refreshMessages();
    Platform.runLater(() -> mainView.setContent(messageView));
  }

  /**
   * Notification de connexion.
   */
  @Override
  public void notifyLogin(User connectedUser) {
    Platform.runLater(() -> {
      showUserProfileView();

      // Message de bienvenue
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Connexion réussie");
      alert.setHeaderText("Bienvenue " + connectedUser.getName());
      alert.setContentText("Vous êtes maintenant connecté.");
      alert.showAndWait();
    });
  }

  /**
   * Notification de déconnexion.
   */
  @Override
  public void notifyLogout() {
    Platform.runLater(this::showLoginView);
  }
}