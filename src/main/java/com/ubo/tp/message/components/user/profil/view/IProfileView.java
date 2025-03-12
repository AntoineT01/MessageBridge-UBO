package com.ubo.tp.message.components.user.profil.view;

import com.ubo.tp.message.core.datamodel.Message;

import java.awt.event.ActionListener;
import java.util.Set;

public interface IProfileView {
  /**
   * Met à jour les informations de base de l'utilisateur
   * @param tag Tag de l'utilisateur
   * @param name Nom de l'utilisateur
   * @param followersCount Nombre de followers
   */
  void updateUserInfo(String tag, String name, int followersCount);

  /**
   * Met à jour la liste des messages de l'utilisateur
   * @param messages Ensemble des messages à afficher
   */
  void updateUserMessages(Set<Message> messages);

  /**
   * Définit l'écouteur pour le bouton de mise à jour
   * @param listener Écouteur d'événement
   */
  void setUpdateButtonListener(ActionListener listener);

  /**
   * Affiche un message d'erreur
   * @param message Message d'erreur à afficher
   */
  void setErrorMessage(String message);

  /**
   * Affiche un message de succès
   * @param message Message de succès à afficher
   */
  void setSuccessMessage(String message);

  /**
   * Récupère le nouveau nom saisi
   * @return Nouveau nom
   */
  String getNewName();

  /**
   * Récupère le nouveau mot de passe saisi
   * @return Nouveau mot de passe
   */
  String getNewPassword();

  /**
   * Récupère la confirmation du nouveau mot de passe
   * @return Confirmation du mot de passe
   */
  String getConfirmPassword();

  /**
   * Réinitialise tous les champs du formulaire
   */
  void resetFields();

  /**
   * Active ou désactive tous les composants de la vue
   * @param enabled État d'activation
   */
  void setEnabled(boolean enabled);
}