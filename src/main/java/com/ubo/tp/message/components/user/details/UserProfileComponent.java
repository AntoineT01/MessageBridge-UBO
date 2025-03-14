package com.ubo.tp.message.components.user.details;

import com.ubo.tp.message.components.IComponent;
import com.ubo.tp.message.components.user.details.controller.UserProfileController;
import com.ubo.tp.message.components.user.details.model.UserProfileModel;
import com.ubo.tp.message.components.user.details.view.IUserProfileView;
import com.ubo.tp.message.components.user.details.view.UserProfileView;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.SessionManager;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

/**
 * Composant pour afficher le profil détaillé d'un utilisateur (autre que l'utilisateur connecté).
 */
public class UserProfileComponent implements IComponent<JPanel> {

  /**
   * Panneau principal.
   */
  private final JPanel mainPanel;

  /**
   * Vue du profil utilisateur.
   */
  private final IUserProfileView profileView;

  /**
   * Contrôleur du profil utilisateur.
   */
  private final UserProfileController profileController;

  /**
   * Modèle du profil utilisateur.
   */
  private final UserProfileModel profileModel;

  /**
   * Constructeur.
   * @param database La base de données.
   * @param sessionManager Le gestionnaire de session.
   * @param entityManager Le gestionnaire d'entités.
   */
  public UserProfileComponent(IDatabase database, SessionManager sessionManager, EntityManager entityManager) {
    // Initialisation du panneau principal
    this.mainPanel = new JPanel(new BorderLayout());

    // Création des composants MVC
    this.profileModel = new UserProfileModel();

    // Création de la vue concrète
    UserProfileView concreteView = new UserProfileView();
    this.profileView = concreteView;

    // Création du contrôleur avec l'interface de vue
    this.profileController = new UserProfileController(profileView, profileModel,
      database, sessionManager, entityManager);

    // Ajout de la vue au panneau principal
    this.mainPanel.add(concreteView, BorderLayout.CENTER);

    // Initialisation
    initialize();
  }

  @Override
  public JPanel getComponent() {
    return mainPanel;
  }

  @Override
  public void initialize() {
    // Rien à faire ici, le profil sera chargé quand setUserToDisplay sera appelé
  }

  @Override
  public void reset() {
    profileController.reset();
  }

  @Override
  public void setEnabled(boolean enabled) {
    profileController.setEnabled(enabled);
    mainPanel.setEnabled(enabled);
  }

  /**
   * Définit l'utilisateur à afficher.
   * @param user L'utilisateur à afficher.
   */
  public void setUserToDisplay(User user) {
    profileController.setUserToDisplay(user);
  }

  /**
   * Définit l'écouteur pour l'action de retour à la liste.
   * @param listener L'écouteur à définir.
   */
  public void setBackToListListener(ActionListener listener) {
    profileController.setBackToListListener(listener);
  }

  /**
   * Affiche un message d'erreur.
   * @param message Le message d'erreur à afficher.
   */
  public void showError(String message) {
    profileController.showError(message);
  }
}