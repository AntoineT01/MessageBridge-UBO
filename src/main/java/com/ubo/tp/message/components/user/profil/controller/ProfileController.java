package com.ubo.tp.message.components.user.profil.controller;

import com.ubo.tp.message.components.user.profil.model.ProfileModel;
import com.ubo.tp.message.components.user.profil.view.IProfileView;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.datamodel.Message;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class ProfileController {
  // Interfaces pour découpler les dépendances
  private final IProfileView view;
  private final ProfileModel model;
  private final EntityManager entityManager;

  public ProfileController(IProfileView view, ProfileModel model, EntityManager entityManager) {
    this.view = view;
    this.model = model;
    this.entityManager = entityManager;

    // Initialisation des écouteurs
    initializeListeners();

    // Initialisation des données
    refreshProfileData();
  }

  /**
   * Initialise les écouteurs d'événements
   */
  private void initializeListeners() {
    view.setUpdateButtonListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateProfile();
      }
    });
  }

  /**
   * Actualise les données du profil
   */
  public void refreshProfileData() {
    User connectedUser = model.getConnectedUser();
    if (connectedUser != null) {
      // Mise à jour des informations utilisateur
      view.updateUserInfo(
        connectedUser.getUserTag(),
        connectedUser.getName(),
        model.getFollowersCount(),
        connectedUser.getAvatarPath()
      );

      // Mise à jour des messages
      Set<Message> userMessages = model.getUserMessages();
      view.updateUserMessages(userMessages);
    }
  }

  /**
   * Mise à jour du profil utilisateur
   */
  private void updateProfile() {
    // Récupération des données du formulaire
    String newName = view.getNewName();
    String newPassword = view.getNewPassword();
    String confirmPassword = view.getConfirmPassword();

    // Validation des données
    if (newName.isEmpty() && newPassword.isEmpty() && view.getAvatarPath().isEmpty()) {
      view.setErrorMessage("Veuillez remplir au moins un champ pour effectuer une mise à jour.");
      return;
    }

    // Vérification de la correspondance des mots de passe
    if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
      view.setErrorMessage("Les mots de passe ne correspondent pas.");
      return;
    }

    // Tentative de mise à jour du profil
    try {
      // Mise à jour via le modèle
      boolean updateResult = model.updateUserInfo(newName, newPassword, view.getAvatarPath());

      if (updateResult) {
        // Enregistrement des modifications
        entityManager.writeUserFile(model.getConnectedUser());

        // Réinitialisation et actualisation
        view.resetFields();
        view.setSuccessMessage("Profil mis à jour avec succès.");
        refreshProfileData();
      } else {
        view.setErrorMessage("Erreur lors de la mise à jour du profil.");
      }
    } catch (Exception e) {
      view.setErrorMessage("Erreur lors de l'enregistrement : " + e.getMessage());
    }
  }

  /**
   * Affichage d'un message d'erreur
   * @param message Message d'erreur
   */
  public void showError(String message) {
    view.setErrorMessage(message);
  }

  /**
   * Réinitialisation du contrôleur
   */
  public void reset() {
    view.resetFields();
    refreshProfileData();
  }

  /**
   * Activation/désactivation du contrôleur
   * @param enabled État d'activation
   */
  public void setEnabled(boolean enabled) {
    view.setEnabled(enabled);
  }
}