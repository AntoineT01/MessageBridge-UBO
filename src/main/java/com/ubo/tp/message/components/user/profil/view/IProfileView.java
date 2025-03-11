package com.ubo.tp.message.components.user.profil.view;

import com.ubo.tp.message.core.datamodel.Message;

import java.awt.event.ActionListener;
import java.util.Set;

/**
 * Interface pour la vue du profil utilisateur.
 */
public interface IProfileView {

  /**
   * Met à jour les informations affichées de l'utilisateur.
   */
  void updateUserInfo(String tag, String name, int followersCount);

  /**
   * Met à jour la liste des messages de l'utilisateur.
   */
  void updateUserMessages(Set<Message> messages);

  /**
   * Affiche un message d'erreur.
   */
  void setErrorMessage(String message);

  /**
   * Affiche un message de succès.
   */
  void setSuccessMessage(String message);

  /**
   * Récupère le nouveau nom saisi.
   */
  String getNewName();

  /**
   * Récupère le nouveau mot de passe saisi.
   */
  String getNewPassword();

  /**
   * Récupère la confirmation du mot de passe saisi.
   */
  String getConfirmPassword();

  /**
   * Réinitialise les champs de formulaire.
   */
  void resetFields();

  /**
   * Active ou désactive tous les composants de la vue.
   */
  void setEnabled(boolean enabled);

  /**
   * Définit l'écouteur d'événements pour le bouton de mise à jour.
   */
  void setUpdateButtonListener(ActionListener listener);
}