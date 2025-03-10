package com.ubo.tp.message.components.user.profil.controller;

import com.ubo.tp.message.components.user.profil.model.ProfileModel;
import com.ubo.tp.message.components.user.profil.view.ProfileView;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;

/**
 * Contrôleur pour la gestion du profil utilisateur.
 */
public class ProfileController {
  /**
   * Vue associée.
   */
  private final ProfileView view;

  /**
   * Modèle associé.
   */
  private final ProfileModel model;

  /**
   * Gestionnaire d'entités pour écrire les fichiers utilisateur.
   */
  private final EntityManager entityManager;

  /**
   * Constructeur.
   */
  public ProfileController(ProfileView view, ProfileModel model, EntityManager entityManager) {
    this.view = view;
    this.model = model;
    this.entityManager = entityManager;

    // Initialiser les écouteurs d'événements
    this.initEventListeners();

    // Initialiser les données du profil
    this.refreshProfileData();
  }

  /**
   * Initialise les écouteurs d'événements.
   */
  private void initEventListeners() {
    // Écouteur pour le bouton de mise à jour
    view.setUpdateButtonListener(e -> updateProfile());
  }

  /**
   * Rafraîchit les données du profil.
   */
  public void refreshProfileData() {
    User connectedUser = model.getConnectedUser();
    if (connectedUser != null) {
      // Mettre à jour les informations utilisateur
      view.updateUserInfo(
        connectedUser.getUserTag(),
        connectedUser.getName(),
        model.getFollowersCount()
      );

      // Mettre à jour la liste des messages
      view.updateUserMessages(model.getUserMessages());
    }
  }

  /**
   * Met à jour le profil de l'utilisateur.
   */
  private void updateProfile() {
    String newName = view.getNewName();
    String newPassword = view.getNewPassword();
    String confirmPassword = view.getConfirmPassword();

    // Vérifier si un des champs est rempli
    if (newName.isEmpty() && newPassword.isEmpty()) {
      view.setErrorMessage("Veuillez remplir au moins un champ pour effectuer une mise à jour.");
      return;
    }

    // Vérifier que les mots de passe correspondent
    if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
      view.setErrorMessage("Les mots de passe ne correspondent pas.");
      return;
    }

    // Effectuer la mise à jour
    boolean updateResult = model.updateUserInfo(newName, newPassword);

    if (updateResult) {
      // Écrire les changements dans le fichier utilisateur
      try {
        entityManager.writeUserFile(model.getConnectedUser());
        view.setSuccessMessage("Profil mis à jour avec succès.");
        view.resetFields();
        refreshProfileData();
      } catch (Exception e) {
        view.setErrorMessage("Erreur lors de l'enregistrement du profil: " + e.getMessage());
      }
    } else {
      view.setErrorMessage("Erreur lors de la mise à jour du profil.");
    }
  }

  /**
   * Affiche un message d'erreur dans la vue.
   */
  public void showError(String message) {
    view.setErrorMessage(message);
  }

  /**
   * Affiche un message de succès dans la vue.
   */
  public void showSuccess(String message) {
    view.setSuccessMessage(message);
  }

  /**
   * Réinitialise le contrôleur et la vue.
   */
  public void reset() {
    view.resetFields();
    refreshProfileData();
  }

  /**
   * Active ou désactive la vue.
   */
  public void setEnabled(boolean enabled) {
    view.setEnabled(enabled);
  }
}