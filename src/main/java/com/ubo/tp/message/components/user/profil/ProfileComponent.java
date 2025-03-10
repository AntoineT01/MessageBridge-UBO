package com.ubo.tp.message.components.user.profil;

import com.ubo.tp.message.components.user.profil.controller.ProfileController;
import com.ubo.tp.message.components.user.profil.model.ProfileModel;
import com.ubo.tp.message.components.user.profil.view.IProfileView;
import com.ubo.tp.message.components.user.profil.view.ProfileView;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.SessionManager;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Implémentation du composant de profil utilisateur.
 * Utilise le pattern MVC pour gérer l'affichage et la mise à jour du profil.
 */
public class ProfileComponent implements IProfileComponent<JPanel> {

  /**
   * Panneau principal.
   */
  protected final JPanel mainPanel;

  /**
   * Vue du profil.
   */
  protected final IProfileView profileView;

  /**
   * Contrôleur du profil.
   */
  protected final ProfileController profileController;

  /**
   * Modèle du profil.
   */
  protected final ProfileModel profileModel;

  /**
   * Constructeur.
   * @param database La base de données.
   * @param entityManager Le gestionnaire d'entités.
   * @param sessionManager Le gestionnaire de session.
   */
  public ProfileComponent(IDatabase database, EntityManager entityManager, SessionManager sessionManager) {
    // Initialisation du panneau principal
    this.mainPanel = new JPanel(new BorderLayout());

    // Création des composants MVC pour le profil
    this.profileModel = new ProfileModel(database, sessionManager);

    // Création de la vue concrète
    ProfileView concreteView = new ProfileView();
    this.profileView = concreteView;

    // Création du contrôleur avec l'interface de vue
    this.profileController = new ProfileController(profileView, profileModel, entityManager);

    // Ajout de la vue concrète au panneau principal
    this.mainPanel.add(concreteView, BorderLayout.CENTER);

    initialize();
  }

  @Override
  public JPanel getComponent() {
    return mainPanel;
  }

  @Override
  public void initialize() {
    // Initialisation des données du profil
    refreshProfileData();
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

  @Override
  public void refreshProfileData() {
    profileController.refreshProfileData();
  }

  @Override
  public void showError(String message) {
    profileController.showError(message);
  }
}